package dev.mvasylenko.rapidtaxi.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passenger")
public class Passenger extends User {

    @OneToMany(mappedBy = "passenger")
    private List<Ride> rides = new ArrayList<>();

    public Passenger(String name, String password, String phoneNumber, String email) {
        super(name, password, phoneNumber, email);
    }

    public Passenger() {
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
