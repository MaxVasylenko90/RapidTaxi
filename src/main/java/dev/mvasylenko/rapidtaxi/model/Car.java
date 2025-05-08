package dev.mvasylenko.rapidtaxi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "driver_id")
    @NotNull(message = "Driver is required!")
    private Driver driver;

    @NotBlank(message = "Brand of car is required!")
    private String brand;

    @NotBlank(message = "Car number is required!")
    private String carNumber;

    public Car(Driver driver, String brand, String carNumber) {
        this.driver = driver;
        this.brand = brand;
        this.carNumber = carNumber;
    }

    public Car() {}

    public long getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
