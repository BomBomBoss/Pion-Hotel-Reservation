package com.pion.hotel.repository;

import com.pion.hotel.model.Reservation;
import com.pion.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByRoomOrderByStartDate(Room room);

}
