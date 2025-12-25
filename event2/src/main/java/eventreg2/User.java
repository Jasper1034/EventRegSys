package eventreg2;

public class User {
    private String name;
    private String email;
    private String username; // Added for login
    private String password; // Added for login

    // Constructor for the demo in Main.java
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Default constructor for GSON (Login/Signup)
    public User() {}

    public String getName() { return name; }
    
    // This fixes the error for login
    public String getUserType() {
        return "Standard User";
    }

    // Getters and Setters for Login
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}