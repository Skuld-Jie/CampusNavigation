package com.example.campus.enums;

import lombok.Getter;

public enum TravelMode {

    WALK(1.2),     // m/s
    BIKE(4.0),
    E_BIKE(6.0);
@Getter
    private final double speed;

    TravelMode(double speed) {
        this.speed = speed;
    }

}

