package eventreg2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Data Access Object (DAO) for handling the many-to-many relationship 
 * between Users (Attendees) and Events.
 * This object is responsible for creating registration records.
 */
public class RegistrationDAO {

    private Connection connection;

    public RegistrationDAO() {
        // Aggregation: Uses the centralized connection established by DatabaseConnection
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Registers an attendee (by user ID) to an event (by event ID).
     * Requires the 'registrations' database table to be set up.
     * * @param userId The ID of the user (attendee) to register.
     * @param eventId The ID of the event the user is registering for.
     * @return true if the registration was successful, false otherwise (e.g., if user is already registered).
     * @throws SQLException If a database access error or constraint violation occurs (e.g., invalid IDs).
     */
    public boolean register(int userId, int eventId) throws SQLException {
        if (connection == null) return false;

        // SQL to insert the link into the registration table
        // 'NOW()' sets the registration_date to the current timestamp.
        String sql = "INSERT INTO registrations (user_id, event_id, registration_date) VALUES (?, ?, NOW())";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, eventId);
            
            // executeUpdate returns the number of rows affected (should be 1 for success)
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLIntegrityConstraintViolationException e) {
            // This handles two cases:
            // 1. Duplicate entry (user_id, event_id) primary key violation (User already registered)
            // 2. Foreign key violation (User or Event ID does not exist)
            System.err.println("Registration failed due to constraint: " + e.getMessage());
            // Throw the exception again to be handled by the EventAPI route
            throw e;
        }
    }
}