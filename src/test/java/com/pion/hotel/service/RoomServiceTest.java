package com.pion.hotel.service;

import com.pion.hotel.model.Bedroom;
import com.pion.hotel.model.Room;
import com.pion.hotel.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    RoomService roomService;

    @Test
    void invokeMethodWithoutCriteria()
    {
        roomService.fetchRooms(null);
        verify(roomRepository, times(1)).findAllRoomsWithReservations();
    }

    @Test
    void invokeMethodWithBedroomCriteria()
    {
        roomService.fetchRooms(Bedroom.SINGLE);
        verify(roomRepository, times(1)).findByBedroomQuantityWithReservations(Bedroom.SINGLE.name());
    }

}