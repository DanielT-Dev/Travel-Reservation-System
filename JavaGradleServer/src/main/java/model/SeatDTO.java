package model;

public class SeatDTO {
    private int seatNumber;
    private String clientName;

    public SeatDTO(int seatNumber, String clientName) {
        this.seatNumber = seatNumber;
        this.clientName = clientName;
    }

    public int getSeatNumber() { return seatNumber; }
    public String getClientName() { return clientName; }
}