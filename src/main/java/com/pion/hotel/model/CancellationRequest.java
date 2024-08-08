package com.pion.hotel.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record CancellationRequest(@Schema(name = "reservationId", description = "reservation id to cancel",example = "1", minimum = "1") String reservationId) {
}
