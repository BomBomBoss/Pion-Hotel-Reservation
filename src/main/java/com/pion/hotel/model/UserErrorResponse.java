package com.pion.hotel.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserErrorResponse(@Schema(
        name = "errorMessage",
        description = "message with corresponding input error from client",
        example = "some error message" )
                                String errorMessage) {
}
