/**
 * Data Access Object for Event entities.
 * Implements ICRUD to follow the Interface Segregation Principle.
 */

package eventreg2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO implements ICRUD<Event> {
    
    private Connection connection;

    public EventDAO() {
        // The DAO uses the connection that established by DatabaseConnection
        this.connection = DatabaseConnection.getConnection();
        
    }

    // CREATE
    @Override
    public boolean create(Event event) throws SQLException {
        if (connection == null) return false;

        String sql = "INSERT INTO events (title, event_date, location, capacity, fee) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // BINDING PARAMETERS: Use sequential integers like-1,2,3...
            pstmt.setString(1, event.getTitle());
            pstmt.setDate(2, Date.valueOf(event.getDate())); // Convert LocalDate to SQL Date
            pstmt.setString(3, event.getLocation());
            pstmt.setInt(4, event.getCapacity());
            pstmt.setBigDecimal(5, event.getFee());
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Send the generated ID back to the object
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        event.setEventId(rs.getInt(1)); 
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // READ ALL
    @Override
    public List<Event> readAll() throws SQLException {
        List<Event> events = new ArrayList<>();
        if (connection == null) return events;

        String sql = "SELECT * FROM events";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // Construct a new Event object from the result set
                Event event = new Event(
                    rs.getInt("event_id"),
                    rs.getString("title"),
                    rs.getDate("event_date").toLocalDate(), 
                    rs.getString("location"),
                    rs.getInt("capacity"),
                    rs.getBigDecimal("fee")
                );
                events.add(event);
            }
        }
        return events;
    }

    // READ ONE
    @Override
    public Event read(int id) throws SQLException {
        if (connection == null) return null;

        String sql = "SELECT * FROM events WHERE event_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getDate("event_date").toLocalDate(),
                        rs.getString("location"),
                        rs.getInt("capacity"),
                        rs.getBigDecimal("fee")
                    );
                }
            }
        }
        return null;
    }

    // UPDATE
    @Override
    public boolean update(Event event) throws SQLException {
        if (connection == null) {
            // If connection is null, throw an error instead of silently returning false
            throw new SQLException("Database connection is not available in EventDAO.");
        }
        
        String sql = "UPDATE events SET title = ?, event_date = ?, location = ?, capacity = ?, fee = ? WHERE event_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            // Title (String)
            pstmt.setString(1, event.getTitle());
            
            // Date (LocalDate - CRITICAL NULL CHECK)
            if (event.getDate() != null) {
                pstmt.setDate(2, java.sql.Date.valueOf(event.getDate())); 
            } else {
                // Must use setNull if date is null
                pstmt.setNull(2, java.sql.Types.DATE); 
            }
            
            // Location (String)
            pstmt.setString(3, event.getLocation());
            
            // Capacity (int)
            pstmt.setInt(4, event.getCapacity());
            
            // Fee (BigDecimal - CRITICAL NULL CHECK)
            if (event.getFee() != null) {
                pstmt.setBigDecimal(5, event.getFee());
            } else {
                // Must use setNull if fee is null
                pstmt.setNull(5, java.sql.Types.DECIMAL); 
            }

            // ID (WHERE clause - safe as it comes from the API path)
            pstmt.setInt(6, event.getEventId()); 

            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            // Log the detailed error
            System.err.println("❌ DAO SQL Error during Event Update: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to service layer
        }
    }

    // DELETE
    // In EventDAO.java, inside the delete(int id) method:
    @Override
    public boolean delete(int id) throws SQLException {
        if (connection == null) return false;
        
        // Example using a raw SQL statement to delete dependent registrations:
        String deleteRegistrationsSql = "DELETE FROM registrations WHERE event_id = ?";
        try (PreparedStatement pstmtReg = connection.prepareStatement(deleteRegistrationsSql)) {
            pstmtReg.setInt(1, id);
            pstmtReg.executeUpdate();
        }

        // delete the main event row
        String sql = "DELETE FROM events WHERE event_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }   
}