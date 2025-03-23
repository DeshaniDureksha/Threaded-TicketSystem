import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class Configuration {

    private int totalTickets;           // How many tickets we have in total
    private int ticketReleaseRate;       // How fast vendors release tickets (ms)
    private int customerRetrievalRate;  // How fast customers grab tickets (ms)
    private int maxTicketCapacity;      // Max tickets in the pool at once

    // Getters and Setters for each setting (allow to set and get settings from outside)
    public int getTotalTickets() {
        return totalTickets;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }


    public void saveToFile(String fileName) throws IOException {
        // Gson is used to convert the current settings into a JSON string for saving
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(this, writer); // This actually does the conversion and writes to the file
        }
    }


    public static Configuration loadFromFile(String fileName) throws IOException {
        Gson gson = new Gson(); // Use gson to load the config from a json file
        try (Reader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Configuration.class); // This reads the file and creates an object with its settings
        }
    }
}