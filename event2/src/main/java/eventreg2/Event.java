package eventreg2;

import java.math.BigDecimal;
import java.time.LocalDate; 
import java.util.List;
import java.util.ArrayList;

public class Event {
    
    private int eventId; 
    private String title;
    private LocalDate date; 
    private String location;
    private int capacity;
    private BigDecimal fee;
    
    private List<User> registeredAttendees;

    //CONSTRUCTOR 1: For creating NEW events (No ID, uses LocalDate) 
    public Event(String title, LocalDate date, String location, int capacity, BigDecimal fee) {
        this.title = title;
        this.date = date; // No more String conversion needed here
        this.location = location;
        this.capacity = capacity;
        this.fee = fee;
        this.registeredAttendees = new ArrayList<>();
    }

    //  CONSTRUCTOR 2: For DAO when reading from the database (Includes ID, uses LocalDate) 
    public Event(int eventId, String title, LocalDate date, String location, int capacity, BigDecimal fee) {
        this.eventId = eventId;
        this.title = title;
        this.date = date; // No more String conversion needed here
        this.location = location;
        this.capacity = capacity;
        this.fee = fee;
        this.registeredAttendees = new ArrayList<>();
    }

    // CONSTRUCTOR 3: (If you need a String-based one, though generally avoid this) 
    public Event(String title, String dateString, String location, int capacity, BigDecimal fee) {
        this.title = title;
        // Parse the incoming String date into a LocalDate
        this.date = LocalDate.parse(dateString); 
        this.location = location;
        this.capacity = capacity;
        this.fee = fee;
        this.registeredAttendees = new ArrayList<>();
    }
    
    // Getters
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public LocalDate getDate() { return date; } // Getter returns to LocalDate
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public BigDecimal getFee() { return fee; }
    public List<User> getRegisteredAttendees() { return registeredAttendees; }

    // Setters (CRITICAL for GSON/UPDATE)
    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setTitle(String title) { this.title = title; }
    public void setLocation(String location) { this.location = location; }
    
    // Setter for the date (CRITICAL for GSON to set the parsed LocalDate)
    public void setDate(LocalDate date) { this.date = date; } 
    // Setter for GSON to set the parsed BigDecimal
    public void setFee(BigDecimal fee) { this.fee = fee; } 
    // Setter for GSON to set the parsed Capacity
    public void setCapacity(int capacity) { this.capacity = capacity; } 

    @Override
    public String toString() {
        return "Event [ID=" + eventId + ", Title=" + title + ", Date=" + date + ", Location=" + location + ", Capacity=" + capacity + ", Fee=" + fee + "]";
    }

    
}