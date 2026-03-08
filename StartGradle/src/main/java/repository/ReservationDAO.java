package repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Database;
import model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private static final ObjectMapper mapper = new ObjectMapper();

    /* CREATE */
    public void save(Reservation reservation) {
        String sql = "INSERT INTO reservation(id_user, id_race, name, seats) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String seatsJson = mapper.writeValueAsString(reservation.getSeats());

            ps.setInt(1, reservation.getId_user());
            ps.setInt(2, reservation.getId_race());
            ps.setString(3, reservation.getName());
            ps.setString(4, seatsJson);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* READ ALL */
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String seatsJson = rs.getString("seats");

                List<Integer> seats = mapper.readValue(
                        seatsJson,
                        new TypeReference<List<Integer>>() {}
                );

                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_race"),
                        rs.getString("name"),
                        seats
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservations;
    }

    /* READ BY ID */
    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        Reservation reservation = null;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String seatsJson = rs.getString("seats");

                List<Integer> seats = mapper.readValue(
                        seatsJson,
                        new TypeReference<List<Integer>>() {}
                );

                reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_race"),
                        rs.getString("name"),
                        seats
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservation;
    }

    /* UPDATE */
    public void update(Reservation reservation) {
        String sql = "UPDATE reservation SET id_user = ?, id_race = ?, name = ?, seats = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String seatsJson = mapper.writeValueAsString(reservation.getSeats());

            ps.setInt(1, reservation.getId_user());
            ps.setInt(2, reservation.getId_race());
            ps.setString(3, reservation.getName());
            ps.setString(4, seatsJson);
            ps.setInt(5, reservation.getId());

            ps.executeUpdate();

        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /* DELETE */
    public void deleteById(int id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final int PAGE_SIZE = 5;

    /* PAGINATION */
    public List<Reservation> findPage(int pageNumber) {
        List<Reservation> reservations = new ArrayList<>();
        int offset = (pageNumber - 1) * PAGE_SIZE;

        String sql = "SELECT * FROM reservation ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, PAGE_SIZE);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String seatsJson = rs.getString("seats");

                List<Integer> seats = mapper.readValue(
                        seatsJson,
                        new TypeReference<List<Integer>>() {}
                );

                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_race"),
                        rs.getString("name"),
                        seats
                ));
            }

        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    /* TOTAL COUNT */
    public int getTotalReservations() {
        String sql = "SELECT COUNT(*) FROM reservation";
        int total = 0;

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}