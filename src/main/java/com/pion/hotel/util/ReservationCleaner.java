package com.pion.hotel.util;


import com.pion.hotel.model.Reservation;
import com.pion.hotel.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
@Configuration
@Slf4j
public class ReservationCleaner {

    private final ReservationRepository reservationRepository;

    public ReservationCleaner(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanOldReservations()
    {
        List<Reservation> deprecatedReservations = reservationRepository.findAll().stream()
                .filter(reservation -> DateUtil.isEndDateBeforeToday(reservation.getEndDate()))
                .toList();

        reservationRepository.deleteAllInBatch(deprecatedReservations);
        log.info("Reservations " + deprecatedReservations + " were removed from DB");
    }
}
