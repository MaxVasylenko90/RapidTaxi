package dev.mvasylenko.rapidtaxi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(name = "ride")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @NotNull
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    @NotNull
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "pickup_address_id")
    @NotNull(message = "Pickup point is required!")
    private Address pickup;

    @ManyToOne
    @JoinColumn(name = "dropoff_address_id")
    @NotNull(message = "Drop-off point iis required!")
    private Address dropOff;

    @PositiveOrZero
    private double distance;

    @PositiveOrZero
    private BigDecimal cost;

    public Ride(Driver driver, Passenger passenger, Address pickup, Address dropOff, double distance, BigDecimal cost) {
        this.driver = driver;
        this.passenger = passenger;
        this.pickup = pickup;
        this.dropOff = dropOff;
        this.distance = distance;
        this.cost = cost;
    }

    public Ride() {
    }

    public long getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Address getPickup() {
        return pickup;
    }

    public void setPickup(Address pickup) {
        this.pickup = pickup;
    }

    public Address getDropOff() {
        return dropOff;
    }

    public void setDropOff(Address dropOff) {
        this.dropOff = dropOff;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
