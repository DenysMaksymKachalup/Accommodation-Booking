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
import com.example.accommodationbooking.service.AccommodationService;
import com.example.accommodationbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpls implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationService accommodationService;
    private final AccommodationRepository accommodationRepository;

    @Transactional
    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        if (!checkAvailableAccommodation(bookingRequestDto.accommodationId())) {
            throw new RuntimeException();
        }
        Booking booking = bookingRepository.save(bookingMapper.toModel(bookingRequestDto));
        decreaseAvailableAccommodation(bookingRequestDto.accommodationId());
        return bookingMapper.toDto(booking);
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
    public List<BookingResponseDto> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> findUserBookingAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bookingRepository.findAllByUserId(user.getId()).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookingResponseDto updateUserBookingById(Long id, BookingRequestDto bookingRequestDto) {
        BookingResponseDto bookingResponseDto = findUserBookingAll().stream()
                .filter(booking -> booking.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + id + " not found!"));
        Booking update = bookingMapper.toModel(bookingRequestDto);
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

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    private boolean checkAvailableAccommodation(Long id) {
        return accommodationService.findById(id).availability() >= 1;
    }

    private void decreaseAvailableAccommodation(Long id) {
        Accommodation accommodation =
                accommodationRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Accommodation with id: " + id + " not found!"));
        accommodation.setAvailability(accommodation.getAvailability() - 1);
        accommodationRepository.save(accommodation);
    }
}
