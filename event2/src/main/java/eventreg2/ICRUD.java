


package eventreg2;

import java.sql.SQLException;
import java.util.List;

/**
 * Standard Interface for CRUD (Create, Read, Update, Delete) operations.
 * Demonstrates the Interface Segregation Principle from SOLID.
 * @param <T> The type of object the DAO will handle (e.g., Event or User).
 */
public interface ICRUD<T> {
    
    /**
     * Saves a new record to the database.
     * @param t The object to be saved.
     * @return true if successful, false otherwise.
     * @throws SQLException if a database error occurs.
     */
    boolean create(T t) throws SQLException;

    /**
     * Retrieves all records of type T from the database.
     * @return A list of all objects.
     * @throws SQLException if a database error occurs.
     */
    List<T> readAll() throws SQLException;

    /**
     * Finds a single record by its ID.
     * @param id The unique database ID.
     * @return The found object or null.
     * @throws SQLException if a database error occurs.
     */
    T read(int id) throws SQLException;

    /**
     * Updates an existing record in the database.
     * @param t The object with updated information.
     * @return true if updated successfully.
     * @throws SQLException if a database error occurs.
     */
    boolean update(T t) throws SQLException;

    /**
     * Removes a record from the database by ID.
     * @param id The ID of the record to delete.
     * @return true if deleted successfully.
     * @throws SQLException if a database error occurs.
     */
    boolean delete(int id) throws SQLException;
}