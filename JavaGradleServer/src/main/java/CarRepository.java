import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface CarRepository extends Repository<Integer,Car> {
    List<Car> findByManufacturer(String manufacturer);
    List<Car> findBetweenYears(int min, int max);
}
