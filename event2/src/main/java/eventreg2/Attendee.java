/**
 * Attendee class representing a registered user for an event.
 * Demonstrates Inheritance by extending the User class.
 */

package eventreg2;


public class Attendee extends User {
    private String registrationID;

    public Attendee(String name, String email, String registrationID) {
        super(name, email); // Passes name/email to the User class
        this.registrationID = registrationID;
    }

    @Override
    public String getUserType() {
        return "Registered Event Attendee";
    }
}
