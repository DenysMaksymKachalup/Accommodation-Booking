package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.exception.BookingException;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.mapper.BookingMapper;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import com.example.accommodationbooking.repository.AccommodationRepository;
import com.example.accommodationbooking.repository.BookingRepository;
import com.example.accommodationbooking.service.AccommodationService;
import com.example.accommodationbooking.service.BookingService;
import com.example.accommodationbooking.service.NotificationTelegramService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationService accommodationService;
    private final AccommodationRepository accommodationRepository;
    private final NotificationTelegramService notificationTelegramService;

    @Transactional
    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        if (!checkAvailableAccommodation(bookingRequestDto)) {
            throw new BookingException("All accommodation is occupied at this date.");
        }
        Booking booking = bookingRepository.save(
                bookingMapper.toModel(getUser().getId(),bookingRequestDto));
        BookingResponseDto dto = bookingMapper.toDto(booking);
        notificationTelegramService.sendSuccessBookingText(dto);
        return dto;
    }

    @Transactional(readOnly = true)
    @Override
    public BookingResponseDto findById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Booking with id: " + id + " not found!"));
        return bookingMapper.toDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> findUserBookingAll() {
        return bookingRepository.findAllByUserId(getUser().getId()).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookingResponseDto updateUserBookingById(Long id, BookingRequestDto bookingRequestDto) {
        findUserBookingAll().stream()
                .filter(booking -> booking.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Booking with id: " + id + " not found!"));
        Booking update = bookingMapper.toModel(getUser().getId(), bookingRequestDto);
        update.setId(id);
        return bookingMapper.toDto(bookingRepository.save(update));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> findAllByUserIdAndBookingStatus(Long id, String status) {
        return bookingRepository
                .findAllByUserIdAndBookingStatus(id,BookingStatus.valueOf(status))
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        BookingResponseDto bookingResponseDto = findUserBookingAll().stream()
                .filter(booking -> booking.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Booking with id: " + id + " not found!"));

        if (Objects.equals(bookingResponseDto.bookingStatus(), BookingStatus.CANCELED.name())) {
            throw new BookingException("Booking with id: " + id + " already is canceled");
        }
        Booking booking = updateStatus(id, BookingStatus.CANCELED);
        notificationTelegramService.sendCanceledBookingText(booking);
    }

    private boolean checkAvailableAccommodation(BookingRequestDto bookingRequestDto) {
        LocalDate in = bookingRequestDto.checkInDate();
        LocalDate out = bookingRequestDto.checkOutDate();
        List<Booking> bookings = bookingRepository.
                findAllByCheckOutDateBetween(bookingRequestDto.accommodationId(),in, out);
        Integer availability = accommodationService.findById(bookingRequestDto.accommodationId()).availability();
        return availability - bookings.size() >= 10;
    }

    private Booking updateStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Booking with id: " + id + " not found!"));
        if (booking.getBookingStatus() != BookingStatus.CANCELED
                && booking.getBookingStatus() != BookingStatus.EXPIRED) {
            booking.setBookingStatus(status);
            return bookingRepository.save(booking);
        } else {
            throw new BookingException("Booking is canceled or expired!");
        }
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
