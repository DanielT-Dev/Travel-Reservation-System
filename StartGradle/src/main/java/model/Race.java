package model;

import java.util.List;

public class Race {
    private Integer id;
    private String destination;
    private String date;
    private String time;
    private List<Boolean> avaiableSeats;

    public Race(Integer id, String destination, String date, String time, List<Boolean> avaiableSeats) {
        this.id = id;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.avaiableSeats = avaiableSeats;
    }

    public Race(String destination, String date, String time, List<Boolean> avaiableSeats) {
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.avaiableSeats = avaiableSeats;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public List<Boolean> getAvaiableSeats() {
        return avaiableSeats;
    }
    public void setAvaiableSeats(List<Boolean> avaiableSeats) {
        this.avaiableSeats = avaiableSeats;
    }
    @Override
    public String toString() {
        return id + " " + destination + " " + date + " " + time + " " + avaiableSeats;
    }
}
