package eventreg2;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("Starting Event Registration System...");

        // Initialize DAO Aggregates the Database Connection
        EventDAO eventDAO = new EventDAO();

        // Demonstration of OOP Inheritance & Polymorphism
        System.out.println("\n--- OOP DEMO (Inheritance & Polymorphism) ---");
        Attendee newAttendee = new Attendee("Alice Johnson", "alice@example.com", "R001");
        User standardUser = new User("Bob Smith", "bob@example.com");

        System.out.println(newAttendee.getName() + " Attendee Type: " + newAttendee.getUserType()); // Polymorphism: Registered Event Attendee
        System.out.println(standardUser.getName() + " Attendee Type: " + standardUser.getUserType()); // Polymorphism: Standard User

        try {
            //  CREATE Operation 
            System.out.println("\n--- 1. CREATE an Event ---");
            Event newEvent = new Event(
                "OOP Design Workshop",
                LocalDate.of(2025, 11, 30),
                "Room 401",
                50,
                new BigDecimal("25.00")
            );

            if (eventDAO.create(newEvent)) {
                System.out.println("✅ CREATED: " + newEvent.getTitle() + " with ID: " + newEvent.getEventId());
            }

            // READ ALL Operation
            System.out.println("\n--- 2. READ ALL Events ---");
            List<Event> allEvents = eventDAO.readAll();
            allEvents.forEach(System.out::println);
            
            if (!allEvents.isEmpty()) {
                // Grab the first event created for update/delete
                Event eventToModify = allEvents.get(0); 

                // UPDATE Operation
                System.out.println("\n--- 3. UPDATE Event ID " + eventToModify.getEventId() + " ---");
                eventToModify.setTitle("Advanced JAVA Workshop");
                eventToModify.setLocation("Lecture Theatre B");
                
                if (eventDAO.update(eventToModify)) {
                    System.out.println("✅ UPDATED: Details changed.");
                }

                // READ one Operation (to verify update) 
                System.out.println("\n--- 4. READ ONE Event (Verify Update) ---");
                Event verifiedEvent = eventDAO.read(eventToModify.getEventId());
                if (verifiedEvent != null) {
                    System.out.println("Verified: " + verifiedEvent);
                }

                //  DELETE Operation 
                System.out.println("\n--- 5. DELETE Event ID " + eventToModify.getEventId() + " ---");
                if (eventDAO.delete(eventToModify.getEventId())) {
                    System.out.println("✅ DELETED successfully.");
                } else {
                    System.out.println("❌ DELETE FAILED.");
                }

            }

            // READ ALL (to confirm deletion)
            System.out.println("\n--- 6. READ ALL (Confirm Deletion) ---");
            eventDAO.readAll().forEach(System.out::println);


        } catch (SQLException e) {
            System.err.println("A database error occurred during CRUD operations.");
            e.printStackTrace();
        }
    }
}