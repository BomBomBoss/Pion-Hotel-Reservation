package com.pion.hotel.service;

import com.pion.hotel.model.Bedroom;
import com.pion.hotel.model.Room;
import com.pion.hotel.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    public Optional<Room> findRoomByNumber(int roomNumber)
    {
        return roomRepository.findByRoomNumber(roomNumber);
    }
    public List<Room> fetchAll()
    {
        return roomRepository.findAll();
    }
    public List<Room> fetchRooms(Bedroom bedroom)
    {
        log.info("Fetching all rooms from DB by bedroom criteria: {}", bedroom);
        return bedroom != null ?
                roomRepository.findByBedroomQuantityWithReservations(bedroom.name())
                : roomRepository.findAllRoomsWithReservations();
    }


}
