package com.pion.hotel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PionHotelReservationApplication {

    public static void main(String[] args) {
        log.info("Swagger UI link with documentation and corresponding endpoints: http://localhost:8080/swagger-ui.html");
        SpringApplication.run(PionHotelReservationApplication.class, args);
    }

}
