package dev.mvasylenko.rapidtaxi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver")
public class Driver extends User {
    @NotBlank
    private String licence;

    @NotNull
    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "driver")
    private List<Ride> rides = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "salary_id")
    private Salary salary;

    public Driver(String name, String password, String phoneNumber, String email, String licence, Car car) {
        super(name, password, phoneNumber, email);
        this.licence = licence;
        this.car = car;
    }

    public Driver() {
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }
}
