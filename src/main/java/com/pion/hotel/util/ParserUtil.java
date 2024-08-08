package com.pion.hotel.util;

import com.pion.hotel.exception.UserInputException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserUtil {

    public static Integer parseToNumber(String inputNumber, String field)
    {
        int id;

        try
        {
            id = Integer.parseInt(inputNumber);
        }
        catch (NumberFormatException e)
        {
            log.error("Can't parse input date: {} to number", inputNumber);
            throw new UserInputException(String.format("[%s] must be number for field '%s'", inputNumber, field));
        }

        return id;
    }
}
