package service;

import model.Reservation;
import repository.ReservationDAO;

import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationService(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public void saveReservation(Reservation reservation) {
        reservationDAO.save(reservation);
    }

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }

    public Reservation getReservationById(int id) {
        return reservationDAO.findById(id);
    }

    public void updateReservation(Reservation reservation) {
        reservationDAO.update(reservation);
    }

    public void deleteReservation(int id) {
        reservationDAO.deleteById(id);
    }

    public List<Reservation> getReservationsPage(int page) {
        return reservationDAO.findPage(page);
    }

    public int getTotalReservations() {
        return reservationDAO.getTotalReservations();
    }
}