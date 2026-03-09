import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("finding cars with manufacturer {}", manufacturerN);
        List<Car> cars = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from Masini where manufacturer = ?")) {

            preStmt.setString(1, manufacturerN);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    Car car = new Car(
                            rs.getInt("id"),
                            rs.getString("manufacturer"),
                            rs.getString("model"),
                            rs.getInt("year")
                    );
                    cars.add(car);
                }
            }

            logger.traceExit("found {} cars", cars.size());
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry("finding cars between years {} and {}", min, max);
        List<Car> cars = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from Masini where year >= ? and year <= ?")) {

            preStmt.setInt(1, min);
            preStmt.setInt(2, max);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    Car car = new Car(
                            rs.getInt("id"),
                            rs.getString("manufacturer"),
                            rs.getString("model"),
                            rs.getInt("year")
                    );
                    cars.add(car);
                }
            }

            logger.traceExit("found {} cars", cars.size());
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        return cars;
    }

    @Override
    public void add(Car elem) throws SQLException {
        //to do
        logger.traceEntry("saving task {} ", elem);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("insert into Masini (manufacturer, model, year) values (?, ?, ?)")) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            int result = preStmt.executeUpdate();
            logger.traceExit("saved {} instances ", result);
        }
        catch (SQLException ex) {
            logger.error(ex);

            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Car elem) {
        logger.traceEntry("updating car with id {} to {}", id, elem);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "update Masini set manufacturer = ?, model = ?, year = ? where id = ?")) {

            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            preStmt.setInt(4, id);

            int result = preStmt.executeUpdate();
            logger.traceExit("updated {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
    }

    @Override
    public Iterable<Car> findAll() throws SQLException {
         //to do
	    logger.traceEntry();

        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Masini")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);

            System.err.println("Error DB " + e);
        }

        logger.traceExit(cars);
        return cars;
    }
}
