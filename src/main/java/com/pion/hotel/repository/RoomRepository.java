package com.pion.hotel.repository;

import com.pion.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(nativeQuery = true, value = "SELECT ro.*, res.* FROM ROOMS ro LEFT JOIN RESERVATIONS res ON ro.room_number=res.room WHERE ro.bedroom_quantity = ?1")
    List<Room> findByBedroomQuantityWithReservations(String bedroom);
    Optional<Room> findByRoomNumber(int roomNumber);
    @Query(nativeQuery = true, value = "SELECT ro.*, res.* FROM ROOMS ro LEFT JOIN RESERVATIONS res ON ro.room_number=res.room")
    List<Room> findAllRoomsWithReservations();


}
