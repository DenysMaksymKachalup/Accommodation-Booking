package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.mapper.BookingMapper;
import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumaration.BookingStatus;
import com.example.accommodationbooking.repository.AccommodationRepository;
import com.example.accommodationbooking.repository.BookingRepository;
import com.example.accommodationbooking.repository.UserRepository;
import com.example.accommodationbooking.security.CustomUserDetailsService;
import com.example.accommodationbooking.service.AccommodationService;
import com.example.accommodationbooking.service.BookingService;
import com.example.accommodationbooking.service.NotificationTelegramService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpls implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationService accommodationService;
    private final AccommodationRepository accommodationRepository;
    private final NotificationTelegramService notificationTelegramService;

    @Transactional
    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        if (!checkAvailableAccommodation(bookingRequestDto.accommodationId())) {
            throw new RuntimeException();
        }
        Booking booking = bookingRepository.save(
                bookingMapper.toModel(getUser().getId(),bookingRequestDto));
        decreaseAvailableAccommodation(bookingRequestDto.accommodationId());
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
        findUserBookingAll().stream()
                .filter(booking -> booking.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Booking with id: " + id + " not found!"));

        Booking booking = updateStatus(id, BookingStatus.CANCELED);
        increaseAvailableAccommodation(id);
        notificationTelegramService.sendCanceledBookingText(booking);
    }

    private boolean checkAvailableAccommodation(Long id) {
        return accommodationService.findById(id).availability() >= 1;
    }

    private void increaseAvailableAccommodation(Long id) {
        Accommodation accommodation = findAccommodationById(id);
        accommodation.setAvailability(accommodation.getAvailability() + 1);
        accommodationRepository.save(accommodation);
    }

    private void decreaseAvailableAccommodation(Long id) {
        Accommodation accommodation =
                accommodationRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Accommodation with id: " + id + " not found!"));
        accommodation.setAvailability(accommodation.getAvailability() - 1);
        accommodationRepository.save(accommodation);
    }

    private Accommodation findAccommodationById(Long id) {
        return accommodationRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Accommodation with id: " + id + " not found!"));
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
            throw new RuntimeException("Booking is canceled or expired!");
        }
    }

    private User getUser() {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return (User) principal;
    }
}
