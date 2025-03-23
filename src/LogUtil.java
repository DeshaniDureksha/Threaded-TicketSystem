import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {

    // Logger for the entire ticketing system.
    private static final Logger logger = Logger.getLogger("TicketingSystem");

    static { // Static initializer block, runs once when the class is loaded.
        try {
            // Get the root logger
            Logger rootLogger = Logger.getLogger("");
            // Disable default console logging
            rootLogger.setUseParentHandlers(false);

            // Remove existing handlers from root logger
            for (var handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            // File handler to write logs to a file.
            FileHandler fileHandler = new FileHandler("ticketing-system.log", true);
            fileHandler.setFormatter(new SimpleFormatter()); // Simple text output format
            logger.addHandler(fileHandler);

            // Set logging level
            logger.setLevel(Level.INFO);

        } catch (IOException e) {
            // Handles the error if file logging initialization fails
            System.err.println("Failed to initialize file logging: " + e.getMessage());
        }
    }

    // Logs info level messages
    public static void logInfo(String message) {
        logger.info(message);
    }

    // Logs warning level messages
    public static void logWarning(String message) {
        logger.warning(message);
    }

    // Logs severe level messages (critical errors)
    public static void logSevere(String message) {
        logger.severe(message);
    }

    // Logs vendor specific activity
    public static void logVendorActivity(String vendorName, String activity) {
        logger.info("[Vendor - " + vendorName + "] " + activity);
    }

    // Logs customer specific activity
    public static void logCustomerActivity(String customerName, String activity) {
        logger.info("[Customer - " + customerName + "] " + activity);
    }
}