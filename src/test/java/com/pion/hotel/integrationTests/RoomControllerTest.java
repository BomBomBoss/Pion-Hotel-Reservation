package com.pion.hotel.integrationTests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SqlGroup({
        @Sql(value = "classpath:init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "classpath:testdata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
})
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    void getAllRoomsWithoutBedroomCriteria_Valid_ResponseOk() throws Exception {
        mockMvc.perform(get("/pion/api1/rooms/all"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rooms").isArray())
                .andExpect(jsonPath("rooms", hasSize(6)))
                .andExpect(jsonPath("rooms[0].roomNumber").value("210"))
                .andExpect(jsonPath("rooms[1].roomNumber").value("220"))
                .andExpect(jsonPath("rooms[2].roomNumber").value("230"))
                .andExpect(jsonPath("rooms[3].roomNumber").value("240"))
                .andExpect(jsonPath("rooms[4].roomNumber").value("250"));

    }

    @Test
    @Order(2)
    void getAllRoomsWithBedroomCriteria_Valid_ResponseOk() throws Exception {
        mockMvc.perform(get("/pion/api1/rooms/all").queryParam("bedrooms", "single"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rooms").isArray())
                .andExpect(jsonPath("rooms", hasSize(3)))
                .andExpect(jsonPath("rooms[0].roomNumber").value("210"))
                .andExpect(jsonPath("rooms[1].roomNumber").value("220"))
                .andExpect(jsonPath("rooms[2].roomNumber").value("230"));

    }

    @Test
    @Order(3)
    void getAvailableRooms_Valid_ResponseOk() throws Exception {
        mockMvc.perform(get("/pion/api1/rooms/available")
                        .queryParam("startDate", "30.08.2024")
                        .queryParam("endDate", "02.09.2024"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rooms").isArray())
                .andExpect(jsonPath("rooms", hasSize(5)));
    }

    @Test
    @Order(4)
    void getAvailableRooms2_Valid_ResponseOk() throws Exception {
        mockMvc.perform(get("/pion/api1/rooms/available")
                        .queryParam("startDate", "08.09.2024")
                        .queryParam("endDate", "02.10.2024"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rooms").isArray())
                .andExpect(jsonPath("rooms", hasSize(4)));
    }

    @Test
    @Order(5)
    void getAvailableRoomsWithEmptyQueryParam_InValid_Response4xx() throws Exception {
        mockMvc.perform(get("/pion/api1/rooms/available")
                        .queryParam("startDate", "")
                        .queryParam("endDate", "02.10.2024"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorMessage").exists());
    }

    @Test
    @Order(6)
    void getAvailableRoomsWithoutQueryParams_InValid_Response4xx() throws Exception {
        mockMvc.perform(get("/pion/api1/rooms/available"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorMessage").exists());
    }

    @Test
    @Order(7)
    void getAvailableRoomsWithInCorrectQueryParams_InValid_Response4xx() throws Exception {
        mockMvc.perform(get("/pion/api1/rooms/available")
                .queryParam("startDate", "01.10.2024")
                .queryParam("endDate", "02.10.2023"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorMessage").exists());
    }

    @Test
    @Order(8)
    void makeReservation_Valid_ResponseIsCreated() throws Exception {
        mockMvc.perform(post("/pion/api1/rooms/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "roomNumber" : "220",
                                "startDate" : "09.09.2024",
                                "endDate" : "10.09.2024"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationId").isNumber())
                .andExpect(jsonPath("$.reservationStartDate").exists())
                .andExpect(jsonPath("$.reservationEndDate").exists())
                .andExpect(jsonPath("$.nightsCount").isNumber());
    }

    @Test
    @Order(9)
    void makeReservationWithNotExistingRoom_InValid_Response4xx() throws Exception {
        mockMvc.perform(post("/pion/api1/rooms/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "roomNumber" : "999",
                                "startDate" : "09.09.2024",
                                "endDate" : "10.09.2024"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @Order(10)
    void makeReservationWithToReservedDates_InValid_Response4xx() throws Exception {
        mockMvc.perform(post("/pion/api1/rooms/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "roomNumber" : "250",
                                "startDate" : "02.10.2024",
                                "endDate" : "05.10.2024"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @Order(11)
    void cancelReservation_Valid_ResponseOk() throws Exception {
        mockMvc.perform(delete("/pion/api1/rooms/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "reservationId" : "1"
                                }
                                """))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message").value("Reservation was cancelled successfully"));
    }

    @Test
    @Order(12)
    void cancelNotExistingReservation_InValid_Response4xx() throws Exception {
        mockMvc.perform(delete("/pion/api1/rooms/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "reservationId" : "10"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists());
    }
}
