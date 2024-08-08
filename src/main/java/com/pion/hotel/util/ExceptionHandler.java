package com.pion.hotel.util;

import com.pion.hotel.exception.ReservationException;
import com.pion.hotel.exception.RoomNotFoundException;
import com.pion.hotel.exception.UserInputException;
import com.pion.hotel.model.UserErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ReservationException.class, RoomNotFoundException.class, UserInputException.class})
    public ResponseEntity<UserErrorResponse> handleException1(RuntimeException ex)
    {
        return new ResponseEntity<>(new UserErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<UserErrorResponse> handleException2(DateTimeParseException ex)
    {
        String datePatters = String.join(", ", DateUtil.patterns.values());
        String unParsedDate = ex.getParsedString();
        String messageToClient = String.format("This date [%s], can't be parsed. Please use these date patterns [%s]", unParsedDate, datePatters);
        return new ResponseEntity<>(new UserErrorResponse(messageToClient), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MissingServletRequestParameterException.class, ConstraintViolationException.class})
    public ResponseEntity<UserErrorResponse> handleException3(Exception ex)
    {
        return new ResponseEntity<>(new UserErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
