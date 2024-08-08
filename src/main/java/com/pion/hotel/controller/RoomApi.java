package com.pion.hotel.controller;

import com.pion.hotel.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
public interface RoomApi {

    @Operation(
            summary = "Fetch all rooms with existing reservation data",
            description = "Fetches all rooms by default. To fetch all rooms by bedroom use dedicated query param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))
            })
    })
    ResponseEntity<RoomResponse> getAllRooms(@RequestParam(required = false) @Parameter(name ="bedrooms", allowEmptyValue = true, description = "can be SINGLE, DOUBLE, TRIPLE", example = "SINGLE") String bedrooms);


    @Operation(
            summary = "Fetch available rooms",
            description = "Fetches available rooms by specific dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))
            })
    })
    ResponseEntity<RoomResponse> getAvailableRooms(@RequestParam (name = "startDate") @NotNull(message = "'startDate' parameter is missing") @NotBlank(message = "'startDate' cant be blank") @Parameter(name ="startDate", required = true, description = "reservation start date", example = "01.09.2024")  String reservationStartDate,
                                                   @RequestParam (name = "endDate") @NotNull(message = "'endDate' parameter is missing") @NotBlank(message = "'endDate' cant be blank")  @Parameter(name ="endDate", required = true, description = "reservation end date", example = "10.09.2024") String reservationEndDate);

    @Operation(
            summary = "Perform room reservation",
            description = "Performs reservation using room number and reservation start and end dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))
            }),
            @ApiResponse(responseCode = "400", description = "invalid room number, start date or end date",content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserErrorResponse.class))
            })
    })
    ResponseEntity<Reservation> makeReservations(@RequestBody ReservationRequest request);

    @Operation(
            summary = "Cancel reservation",
            description = "Cancel reservation using reservation Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CancellationResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "if reservation not exist in system",content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserErrorResponse.class))
            })
    })
    ResponseEntity<CancellationResponse> cancelReservation(@RequestBody CancellationRequest request);
}
