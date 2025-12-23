package com.example.campus.enums;

public enum TravelMode {

    WALK(1.2),     // m/s
    BIKE(4.0),
    E_BIKE(6.0);

    private final double speed;

    TravelMode(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}

