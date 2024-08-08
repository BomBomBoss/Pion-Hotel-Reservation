package com.pion.hotel.model;

import java.util.Arrays;

public enum Bedroom {

    SINGLE,
    DOUBLE,
    TRIPLE;

    public static Bedroom getBedroom(String queryParam)
    {
        return Arrays.stream(Bedroom.values())
                .filter(bedroom -> bedroom.name().equals(queryParam.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
