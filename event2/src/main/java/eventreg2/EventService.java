package eventreg2; 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    
    private EventDAO eventDAO;

    public EventService() {
        this.eventDAO = new EventDAO();
    }

    
    public boolean updateEvent(Event event) {
        try {
            return eventDAO.update(event); 
        } catch (SQLException e) {
            System.err.println("Service Error during Event Update: " + e.getMessage());
            e.printStackTrace(); 
            return false;
        }
    }
    
    //R: Reads a single event by ID 
    public Event getEventById(int id) {
        try {
            // Calls the read method from the evendao file
            return eventDAO.read(id);
        } catch (SQLException e) {
            System.err.println("Service Error reading event ID " + id + ": " + e.getMessage());
            return null;
        }
    }
    
    // Add other CRUD methods here 
    public List<Event> getAllEvents() {
        try {
            return eventDAO.readAll();
        } catch (SQLException e) {
            System.err.println("Service Error reading all events: " + e.getMessage());
            return new ArrayList<>();
        }
    }


        
        // C: Creates a new event by calling the DAO.
        public boolean createEvent(Event event) {
            try {
                // Calls the create method from the DAO
                return eventDAO.create(event); 
            } catch (SQLException e) {
                System.err.println("Service Error creating event: " + e.getMessage());
                // Handle constraint violations or other SQL errors
                e.printStackTrace();
                return false;
            }
        }

        
        //D: Deletes an event by ID 
         
        public boolean deleteEvent(int id) {
            try {
                return eventDAO.delete(id); 
            } catch (SQLException e) {
                System.err.println("Service Error deleting event ID " + id + ": " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
}