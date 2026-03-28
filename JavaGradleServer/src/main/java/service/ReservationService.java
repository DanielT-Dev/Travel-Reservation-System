package service;

import model.Reservation;
import model.SeatDTO;
import repository.ReservationDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationService(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public void saveReservation(Reservation reservation) {
        reservationDAO.add(reservation);
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservationDAO.getAll());
    }

    public Reservation getReservationById(int id) {
        return reservationDAO.findById(id);
    }

    public void updateReservation(Reservation reservation) {
        reservationDAO.update(reservation, reservation.getId());
    }

    public void deleteReservation(int id) {
        Reservation reservation = reservationDAO.findById(id);
        if (reservation != null) {
            reservationDAO.delete(reservation);
        }
    }

    public List<Reservation> getReservationsPage(int page) {
        return reservationDAO.findPage(page);
    }

    public int getTotalReservations() {
        return reservationDAO.getTotalReservations();
    }

    public List<SeatDTO> getSeatsForRace(int raceId) {
        List<SeatDTO> result = new ArrayList<>();

        Map<Integer, String> reserved = reservationDAO.getReservedSeats(raceId);

        for (int i = 1; i <= 18; i++) {
            String name = reserved.getOrDefault(i, "-");
            result.add(new SeatDTO(i, name));
        }

        return result;
    }

    public List<Integer> getFreeSeats(int raceId) {
        Map<Integer, String> reserved = reservationDAO.getReservedSeats(raceId);

        List<Integer> free = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            if (!reserved.containsKey(i)) {
                free.add(i);
            }
        }
        return free;
    }

    public void createReservation(int raceId, String name, List<Integer> seats) {
        Reservation reservation = new Reservation(
                0,
                1,
                raceId,
                name,
                seats
        );

        reservationDAO.add(reservation);
    }
}