package com.pion.hotel.service;

import com.pion.hotel.exception.ReservationException;
import com.pion.hotel.exception.UserInputException;
import com.pion.hotel.model.CancellationResponse;
import com.pion.hotel.model.Reservation;
import com.pion.hotel.model.ReservationRequest;
import com.pion.hotel.model.Room;
import com.pion.hotel.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReservationServiceTest {

    @Mock
    Room room;
    @Mock
    Reservation reservation;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    RoomService roomService;
    @InjectMocks
    ReservationService reservationService;

    ReservationRequest request;


    @BeforeEach
    void setUp()
    {
     request = new ReservationRequest("110", "01.09.2024", "10.09.2024");

     when(reservation.getRoom()).thenReturn(room);
     when(reservation.getStartDate()).thenReturn(LocalDate.of(2024, 9, 2));
     when(reservation.getEndDate()).thenReturn(LocalDate.of(2024, 9, 12));
    }

    @Test
    void makeReservation_Valid()
    {
        when(roomService.findRoomByNumber(anyInt())).thenReturn(Optional.of(new Room()));

        reservationService.performReservation(request);

        verify(reservationRepository, times(1)).save(any(Reservation.class));

    }

    @Test
    void makeReservationWithInvalidDate_throwException()
    {
        request.setEndDate("10.09.2023");

        Assertions.assertThrows(UserInputException.class, () -> reservationService.performReservation(request));
    }

    @Test
    void makeReservationWithCollisionDates_throwException()
    {
        when(reservationRepository.findAllByRoomOrderByStartDate(any(Room.class))).thenReturn(Collections.singletonList(reservation));
        when(roomService.findRoomByNumber(anyInt())).thenReturn(Optional.of(room));

        Assertions.assertThrows(ReservationException.class, () -> reservationService.performReservation(request));
    }

    @Test
    void performCancellation_Valid()
    {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        CancellationResponse response = reservationService.performCancellation("1");

        Assertions.assertEquals(response.message(), "Reservation was cancelled successfully");
    }

    @Test
    void performCancellationWithIncorrectId_Valid()
    {
        Assertions.assertThrows(UserInputException.class, () -> reservationService.performCancellation("someId"));
    }


}