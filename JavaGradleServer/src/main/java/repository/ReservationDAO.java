package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Database;
import model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ReservationDAO extends AbstractRepository<Reservation, Integer> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int PAGE_SIZE = 5;

    @Override
    public void add(Reservation reservation) {
        String sql = "INSERT INTO reservation(id_user, id_race, name, seats) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getId_user());
            ps.setInt(2, reservation.getId_race());
            ps.setString(3, reservation.getName());
            ps.setString(4, mapper.writeValueAsString(reservation.getSeats()));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reservation reservation) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Reservation reservation, Integer id) {
        String sql = "UPDATE reservation SET id_user = ?, id_race = ?, name = ?, seats = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getId_user());
            ps.setInt(2, reservation.getId_race());
            ps.setString(3, reservation.getName());
            ps.setString(4, mapper.writeValueAsString(reservation.getSeats()));
            ps.setInt(5, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reservation findById(Integer id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public Iterable<Reservation> findAll() {
        return getAll();
    }

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
                reservations.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public int getTotalReservations() {
        String sql = "SELECT COUNT(*) FROM reservation";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<Integer, String> getReservedSeats(int raceId) {
        Map<Integer, String> reservedSeats = new java.util.HashMap<>();
        String sql = "SELECT name, seats FROM reservation WHERE id_race = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, raceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                List<Integer> seats = mapper.readValue(
                        rs.getString("seats"),
                        new TypeReference<List<Integer>>() {}
                );
                for (Integer seat : seats) {
                    reservedSeats.put(seat, name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservedSeats;
    }

    private Reservation mapRow(ResultSet rs) throws Exception {
        List<Integer> seats = mapper.readValue(
                rs.getString("seats"),
                new TypeReference<List<Integer>>() {}
        );
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("id_user"),
                rs.getInt("id_race"),
                rs.getString("name"),
                seats
        );
    }
}