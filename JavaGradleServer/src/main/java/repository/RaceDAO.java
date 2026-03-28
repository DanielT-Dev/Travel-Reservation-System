package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Database;
import model.Race;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RaceDAO extends AbstractRepository<Race, Integer> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int PAGE_SIZE = 5;

    @Override
    public void add(Race race) {
        String sql = "INSERT INTO race(destination, date, time, seats) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, race.getDestination());
            ps.setString(2, race.getDate());
            ps.setString(3, race.getTime());
            ps.setString(4, mapper.writeValueAsString(race.getAvaiableSeats()));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Race race) {
        String sql = "DELETE FROM race WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, race.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Race race, Integer id) {
        String sql = "UPDATE race SET destination = ?, date = ?, time = ?, seats = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, race.getDestination());
            ps.setString(2, race.getDate());
            ps.setString(3, race.getTime());
            ps.setString(4, mapper.writeValueAsString(race.getAvaiableSeats()));
            ps.setInt(5, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Race findById(Integer id) {
        String sql = "SELECT * FROM race WHERE id = ?";

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
    public Collection<Race> getAll() {
        List<Race> races = new ArrayList<>();
        String sql = "SELECT * FROM race";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                races.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return races;
    }

    @Override
    public Iterable<Race> findAll() {
        return getAll();
    }

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
                races.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return races;
    }

    public int getTotalRaces() {
        String sql = "SELECT COUNT(*) FROM race";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Race mapRow(ResultSet rs) throws Exception {
        List<Boolean> seats = mapper.readValue(
                rs.getString("seats"),
                new TypeReference<List<Boolean>>() {}
        );
        return new Race(
                rs.getInt("id"),
                rs.getString("destination"),
                rs.getString("date"),
                rs.getString("time"),
                seats
        );
    }
}