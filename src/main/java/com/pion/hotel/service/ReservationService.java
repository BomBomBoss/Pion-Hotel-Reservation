package com.pion.hotel.service;

import com.pion.hotel.exception.ReservationException;
import com.pion.hotel.exception.RoomNotFoundException;
import com.pion.hotel.exception.UserInputException;
import com.pion.hotel.model.*;
import com.pion.hotel.repository.ReservationRepository;
import com.pion.hotel.util.DateUtil;
import com.pion.hotel.util.ParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }


    private List<Reservation> getRoomAllReservations(Room room)
    {
        log.info("Fetching all reservations by room number: {}", room.getRoomNumber());
        return reservationRepository.findAllByRoomOrderByStartDate(room);
    }


    public Reservation performReservation(ReservationRequest request)
    {
        int roomNumber = ParserUtil.parseToNumber(request.getRoomNumber(), "roomNumber");
        LocalDate reservationStartDate = DateUtil.mapToDate(request.getStartDate());
        LocalDate reservationEndDate = DateUtil.mapToDate(request.getEndDate());

        datesValidation(reservationStartDate, reservationEndDate);

        List<LocalDate> submittedReservationDaysRange = getDateRange(reservationStartDate, reservationEndDate);

        Room room = getRoomByNumber(roomNumber);
        List<Reservation> reservations = getRoomAllReservations(room);

        if (!reservations.isEmpty())
        {
            Optional<LocalDate> collisionDate = reservations.stream()
                    .flatMap(reservation -> getDateRange(reservation.getStartDate(), reservation.getEndDate()).stream())
                    .filter(submittedReservationDaysRange::contains)
                    .findAny();


            if (collisionDate.isPresent())
            {
                throw new ReservationException(String.format("Room number: [%s] is reserved during [%s] - [%s]",
                        room.getRoomNumber(), reservationStartDate, reservationEndDate));
            }

        }

        Reservation submitReservation = Reservation.builder()
                .room(room)
                .startDate(reservationStartDate)
                .endDate(reservationEndDate)
                .nightsCount(submittedReservationDaysRange.size()).build();

        return submitReservation(submitReservation);
    }


    public RoomResponse getRooms(Bedroom bedroom)
    {
        return new RoomResponse(roomService.fetchRooms(bedroom));
    }

    private Room getRoomByNumber(int roomNumber)
    {
        return roomService.findRoomByNumber(roomNumber)
                .orElseThrow(() -> new RoomNotFoundException(String.format("This room number [%d] is not present in our hotel", roomNumber)));
    }

    private List<LocalDate> getDateRange(LocalDate startDateReserved, LocalDate endDateReserved)
    {
        return startDateReserved.datesUntil(endDateReserved).collect(Collectors.toList());
    }

    private Reservation submitReservation(Reservation submit)
    {
        log.info("Performing reservation from [{}] till [{}] on room [{}]", submit.getStartDate(), submit.getEndDate(), submit.getRoom().getRoomNumber());
        return reservationRepository.save(submit);
    }

    private boolean isInputDateValid(LocalDate startDate, LocalDate endDate)
    {
        return DateUtil.isStartDateValid(startDate) && startDate.isBefore(endDate);
    }

    public CancellationResponse performCancellation(String reservationId)
    {
        long id = ParserUtil.parseToNumber(reservationId, "reservationId");

        Reservation reservationToCancel = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationException(String.format("This reservation [%s] does not exist", id)));

        reservationRepository.deleteById(id);

        return CancellationResponse.builder()
                .message("Reservation was cancelled successfully")
                .reservation(reservationToCancel)
                .build();

    }

    public RoomResponse getAvailableRoomsForSpecificDates(String startDate, String endDate)
    {
        LocalDate reservationStartDate = DateUtil.mapToDate(startDate);
        LocalDate reservationEndDate = DateUtil.mapToDate(endDate);

        datesValidation(reservationStartDate, reservationEndDate);

        List<LocalDate> daysRangeToReserve = getDateRange(reservationStartDate, reservationEndDate);

        List<Room> reservedRooms = reservationRepository.findAll().stream()
                .filter(reservation -> isContainsDates(getDateRange(reservation.getStartDate(), reservation.getEndDate()), daysRangeToReserve))
                .map(Reservation::getRoom)
                .toList();

        List<Room> availableRooms = roomService.fetchAll().stream()
                .filter(room -> reservedRooms.stream().noneMatch(res -> room.getRoomNumber() == res.getRoomNumber()))
                .toList();

        return new RoomResponse(availableRooms);

    }

    private void datesValidation(LocalDate reservationStartDate, LocalDate reservationEndDate) {
        if (!isInputDateValid(reservationStartDate, reservationEndDate))
        {
            log.error("Invalid dates. Start date: {}. End Date {}. Today {}.", reservationStartDate, reservationEndDate, LocalDate.now());
            throw new UserInputException("Invalid reservations dates");
        }
    }

    private boolean isContainsDates(List<LocalDate> reservedDates, List<LocalDate> toReserveDates)
    {
        return reservedDates.stream().anyMatch(toReserveDates::contains);
    }

}
