package eventreg2;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDate;
import java.math.BigDecimal;

public class ProjectTest {

    @Test
    public void testUserPolymorphism() {
        // Testing inheritance from User.java and Attendee.java
        User user = new User("John Doe", "john@example.com");
        Attendee attendee = new Attendee("Jane Doe", "jane@example.com", "REG123");

        assertEquals("Standard User", user.getUserType());
        assertEquals("Registered Event Attendee", attendee.getUserType());
    }

    @Test
    public void testEventData() {
        // Testing Event.java model logic
        LocalDate date = LocalDate.of(2025, 12, 25);
        Event event = new Event("Christmas Gala", date, "Grand Hall", 100, new BigDecimal("50.00"));

        assertEquals("Christmas Gala", event.getTitle());
        assertEquals(100, event.getCapacity());
        // Use compareTo for BigDecimal to avoid scale issues
        assertTrue(new BigDecimal("50.00").compareTo(event.getFee()) == 0);
    }

    @Test
    public void testDateParsing() {
        // Testing if the date logic you used in the API works
        String jsonDate = "2025-10-15";
        LocalDate expected = LocalDate.of(2025, 10, 15);
        LocalDate parsed = LocalDate.parse(jsonDate);
        
        assertEquals(expected, parsed);
    }
    
    @Test
    public void testAttendeeListInitialization() {
        // Testing Aggregation: checking if the list is initialized in the constructor
        Event event = new Event("Workshop", LocalDate.now(), "Lab 1", 20, BigDecimal.ZERO);
        
        assertNotNull("Attendee list should not be null", event.getRegisteredAttendees());
        assertEquals(0, event.getRegisteredAttendees().size());
    }
}