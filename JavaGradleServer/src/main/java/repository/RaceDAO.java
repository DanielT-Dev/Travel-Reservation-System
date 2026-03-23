package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Database;
import model.Race;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceDAO {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int PAGE_SIZE = 5;

    /* CREATE */
    public void save(Race race) {
        String sql = "INSERT INTO race(destination, date, time, seats) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String seatsJson = mapper.writeValueAsString(race.getAvaiableSeats());
            ps.setString(1, race.getDestination());
            ps.setString(2, race.getDate());
            ps.setString(3, race.getTime());
            ps.setString(4, seatsJson);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* READ ALL */
    public List<Race> findAll() {
        List<Race> races = new ArrayList<>();
        String sql = "SELECT * FROM race";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String seatsJson = rs.getString("seats");
                List<Boolean> seats = mapper.readValue(seatsJson, new TypeReference<List<Boolean>>() {});
                races.add(new Race(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        rs.getString("date"),
                        rs.getString("time"),
                        seats
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return races;
    }

    /* READ BY ID */
    public Race findById(int id) {
        String sql = "SELECT * FROM race WHERE id = ?";
        Race race = null;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String seatsJson = rs.getString("seats");
                List<Boolean> seats = mapper.readValue(seatsJson, new TypeReference<List<Boolean>>() {});
                race = new Race(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        rs.getString("date"),
                        rs.getString("time"),
                        seats
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return race;
    }

    /* UPDATE */
    public void update(Race race) {
        String sql = "UPDATE race SET destination = ?, date = ?, time = ?, seats = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String seatsJson = mapper.writeValueAsString(race.getAvaiableSeats());
            ps.setString(1, race.getDestination());
            ps.setString(2, race.getDate());
            ps.setString(3, race.getTime());
            ps.setString(4, seatsJson);
            ps.setInt(5, race.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* DELETE */
    public void deleteById(int id) {
        String sql = "DELETE FROM race WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* PAGINATION */
    public List<Race> findPage(int pageNumber) {
        List<Race> races = new ArrayList<>();
        int offset = (pageNumber - 1) * PAGE_SIZE;
        String sql = "SELECT * FROM race ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, PAGE_SIZE);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String seatsJson = rs.getString("seats");
                List<Boolean> seats = mapper.readValue(seatsJson, new TypeReference<List<Boolean>>() {});
                races.add(new Race(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        rs.getString("date"),
                        rs.getString("time"),
                        seats
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return races;
    }

    /* TOTAL COUNT */
    public int getTotalRaces() {
        String sql = "SELECT COUNT(*) FROM race";
        int total = 0;

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) total = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}