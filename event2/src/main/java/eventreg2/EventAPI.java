
/** * Main method to launch the SparkJava server and define API routes.
   * * @param args Command line arguments (not used).
*/

/**
 * The entry point for the Event Registration System Web Service.
 * This class configures the SparkJava server, establishes RESTful API endpoints,
 * and handles the serialization of data using Google GSON.
 * * <p>Standard CRUD operations for Events and User authentication (Login/Signup) 
 * are managed through this controller.</p>
 */


package eventreg2;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;


public class EventAPI {
    
    /** * GSON instance configured with a custom LocalDateAdapter to handle 
     * JSON date serialization/deserialization.
     */
    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();
    
    /** Service layer instance for handling event business logic. */
    private static final EventService eventService = new EventService();

    /** Data Access Object for handling user-related database operations. */
    private static final UserDAO userDAO = new UserDAO();

    
    public static void main(String[] args) { 
        
        // 1. Server Configuration
        staticFiles.location("/public"); 
        port(5000); 

        // 2. CORS (Cross-Origin Resource Sharing) Configuration
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            return "OK";
        });
        
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        
        // 3. API ENDPOINTS

        /**
         * GET /api/events
         * Fetches all events from the database.
         */
        get("/api/events", (request, response) -> {
            response.type("application/json");
            return GSON.toJson(eventService.getAllEvents());
        });

        /**
         * POST /api/events
         * Creates a new event based on JSON request body.
         */
        post("/api/events", (request, response) -> {
            Event newEvent = GSON.fromJson(request.body(), Event.class);
            eventService.createEvent(newEvent);
            return "{\"status\": \"success\"}"; 
        });

        /**
         * POST /api/signup
         * Registers a new user in the database.
         */
        post("/api/signup", (request, response) -> {
            try {
                User newUser = GSON.fromJson(request.body(), User.class);
                userDAO.saveUser(newUser); 
                return "{\"status\": \"success\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"error\": \"" + e.getMessage() + "\"}";
            }
        });

        /**
         * POST /api/login
         * Authenticates a user against hardcoded admin credentials or the database.
         */
        post("/api/login", (request, response) -> {
            try {
                User attempt = GSON.fromJson(request.body(), User.class);
                
                // Check for hardcoded admin first
                if ("admin".equals(attempt.getUsername()) && "1234".equals(attempt.getPassword())) {
                    return "{\"status\": \"success\"}";
                }
                
                // Otherwise, check the database
                User existingUser = userDAO.findByUsername(attempt.getUsername());
                if (existingUser != null && existingUser.getPassword().equals(attempt.getPassword())) {
                    return "{\"status\": \"success\"}";
                }
                
                response.status(401);
                return "{\"error\": \"Invalid username or password\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"error\": \"Server Error: " + e.getMessage() + "\"}";
            }
        });

        /**
         * PUT /api/events/:id
         * Updates an existing event by its ID.
         */
        put("/api/events/:id", (request, response) -> {
            try {
                int urlId = Integer.parseInt(request.params(":id"));
                Event updatedEvent = GSON.fromJson(request.body(), Event.class); 
                updatedEvent.setEventId(urlId); 

                if (eventService.updateEvent(updatedEvent)) {
                    response.status(200);
                    return GSON.toJson(updatedEvent); 
                } else {
                    response.status(404);
                    return "{\"error\": \"Event ID " + urlId + " not found in database\"}";
                }
            } catch (Exception e) {
                response.status(500);
                return "{\"error\": \"" + e.getMessage() + "\"}";
            }
        });

        /**
         * DELETE /api/events/:id
         * Deletes an event by its unique ID.
         */
        delete("/api/events/:id", (request, response) -> {
            int eventId = Integer.parseInt(request.params(":id"));
            if (eventService.deleteEvent(eventId)) {
                return "{\"message\": \"Deleted\"}";
            }
            return "{\"error\": \"Failed\"}";
        });

        // 4. Utility: Auto-open the browser on startup
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:5000/index.html");
        } catch (Exception e) {
            System.out.println("Visit: http://localhost:5000/index.html");
        }
    }
}