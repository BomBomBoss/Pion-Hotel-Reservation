package com.pion.hotel.controller;

import com.pion.hotel.model.*;
import com.pion.hotel.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/pion/api1")
@Tag(name = "Hotel reservation API")
public class RoomController implements RoomApi {


    private final ReservationService reservationService;

    public RoomController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping("/rooms/all")
    public ResponseEntity<RoomResponse> getAllRooms(String bedrooms)
    {
        Bedroom bedroom = bedrooms != null ? Bedroom.getBedroom(bedrooms) : null;
        return ResponseEntity.ok(reservationService.getRooms(bedroom));
    }

    @GetMapping("/rooms/available")
    public ResponseEntity<RoomResponse> getAvailableRooms(String reservationStartDate, String reservationEndDate)
    {
        return ResponseEntity.ok(reservationService.getAvailableRoomsForSpecificDates(reservationStartDate, reservationEndDate));
    }

    @PostMapping("/rooms/reserve")
    public ResponseEntity<Reservation> makeReservations(ReservationRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.performReservation(request));
    }

    @DeleteMapping("/rooms/cancel")
    public ResponseEntity<CancellationResponse> cancelReservation(CancellationRequest request) {
        return ResponseEntity.ok(reservationService.performCancellation(request.reservationId()));
    }

}
