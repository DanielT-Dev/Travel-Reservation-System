package model;

import java.util.List;

public class Reservation implements Identifiable<Integer> {
    private Integer id;
    private Integer id_user;
    private Integer id_race;
    private String name;
    private List<Integer> seats;

    public Reservation(Integer id, Integer id_user, Integer id_race, String name, List<Integer> seats) {
        this.id = id;
        this.id_user = id_user;
        this.id_race = id_race;
        this.name = name;
        this.seats = seats;
    }

    public Reservation(Integer id_user, Integer id_race, String name, List<Integer> seats) {
        this.id_user = id_user;
        this.id_race = id_race;
        this.name = name;
        this.seats = seats;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId_user() {
        return id_user;
    }
    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }
    public Integer getId_race() {
        return id_race;
    }
    public void setId_race(Integer id_race) {
        this.id_race = id_race;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Integer> getSeats() {
        return seats;
    }
    public void setSeats(List<Integer> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return id +  " " + name + " " + id_user + " " + id_race + " " + seats;
    }

    @Override
    public Integer getID() {
        return 0;
    }

    @Override
    public void setID(Integer id) {

    }
}