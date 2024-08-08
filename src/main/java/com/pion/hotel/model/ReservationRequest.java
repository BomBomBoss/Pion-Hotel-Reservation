package com.pion.hotel.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;
@Data
public class ReservationRequest {

    @NonNull
    @Schema(name = "roomNumber", example = "150", minimum = "110")
    private String roomNumber;

    @NonNull
    @Schema(name = "startDate", description = "reservation start date",example = "01.09.2024", pattern = "dd-MM-YYYY, dd.MM.yyyy, dd/MM/YYYY", minimum = "starting from today")
    private String startDate;

    @NonNull
    @Schema(name = "endDate", description = "reservation end date", example = "10.09.2024", pattern = "dd-MM-YYYY, dd.MM.yyyy, dd/MM/YYYY", minimum = "should be after start date")
    private String endDate;
}
