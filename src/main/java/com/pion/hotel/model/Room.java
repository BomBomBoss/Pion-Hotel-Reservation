package com.pion.hotel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "rooms")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Room {

    @Id
    @Column(name = "room_number")
    @Schema(name = "room number", example = "150", minimum = "110")
    private int roomNumber;

    @Column(name = "bedroom_quantity")
    @Enumerated(value = EnumType.STRING)
    @Schema(name = "bedroomQuantity", description = "amount of beds in room",example = "SINGLE")
    private Bedroom bedroomQuantity;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Schema(name = "reservations", description = "list of reservations on this room")
    private List<Reservation> reservations;

}
