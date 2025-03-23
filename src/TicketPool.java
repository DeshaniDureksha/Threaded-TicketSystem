import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<String> tickets = new LinkedList<>(); // Thread-safe queue
    private final int maxCapacity;
    private int ticketCounter = 1; // Unique ticket ID generator

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTicket(String vendorName) {
        if (tickets.size() < maxCapacity) {
            String ticket = "Ticket-" + ticketCounter++;
            tickets.add(ticket);
            System.out.println(vendorName + " added " + ticket + ". Total Tickets: " + tickets.size());
        } else {
            System.out.println(vendorName + " waiting: Pool is full.");
        }
    }

    public synchronized String removeTicket(String customerName) {
        if (!tickets.isEmpty()) {
            String ticket = tickets.poll();
            System.out.println(customerName + " purchased " + ticket + ". Remaining Tickets: " + tickets.size());
            return ticket;
        } else {
            System.out.println(customerName + " tried to purchase, but no tickets are available.");
            return null;
        }
    }

    public synchronized int getAvailableTickets() {
        return tickets.size();
    }
}
