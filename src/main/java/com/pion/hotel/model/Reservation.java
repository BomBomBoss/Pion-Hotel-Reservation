package com.pion.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = { "room" })
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "reservationId")
    @Schema(name = "reservationId", example = "1", description = "reservation id generating in DB")
    private Long id;

    @Column(name = "start_date")
    @JsonProperty(value = "reservationStartDate")
    @Schema(name = "startDate", description = "reservation start date",example = "01.09.2023", pattern = "dd-MM-YYYY, dd.MM.yyyy, dd/MM/YYYY")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonProperty(value = "reservationEndDate")
    @Schema(name = "reservationEndDate", description = "reservation end date", example = "10.09.2023", pattern = "dd-MM-YYYY, dd.MM.yyyy, dd/MM/YYYY")
    private LocalDate endDate;

    @Column(name = "nights_count")
    @JsonProperty(value = "nightsCount")
    @Schema(name = "nightsCount", description = "nights count between reservation start and end date", example = "9", minimum = "1")
    private int nightsCount;

    @ManyToOne
    @JoinColumn(name = "room", referencedColumnName = "room_number")
    private Room room;

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", nightsCount=" + nightsCount +
                '}';
    }
}
