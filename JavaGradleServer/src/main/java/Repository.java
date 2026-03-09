import java.sql.SQLException;

public interface Repository<ID, T> {
    void add(T elem) throws SQLException;
    void update(ID id, T elem);
    Iterable<T> findAll() throws SQLException;
}
