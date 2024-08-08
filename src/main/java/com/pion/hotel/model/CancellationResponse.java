package com.pion.hotel.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CancellationResponse(@Schema(name = "message", description = "cancellation confirmation") String message, Reservation reservation) {
}
