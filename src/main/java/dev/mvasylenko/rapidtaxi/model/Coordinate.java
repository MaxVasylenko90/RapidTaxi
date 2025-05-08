package dev.mvasylenko.rapidtaxi.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class Coordinate {
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private long longitude;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private long latitude;

    public Coordinate(long longitude, long latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
