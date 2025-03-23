public class Vendor implements Runnable {
    private final String name;
    private final int ticketReleaseRate;
    private final TicketPool ticketPool;
    private volatile boolean running = true;

    public Vendor(String name, int ticketReleaseRate, TicketPool ticketPool) {
        this.name = name;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        try {
            while (running) {
                synchronized (ticketPool) {
                    ticketPool.addTicket(name);
                    LogUtil.logVendorActivity(name, "Added a ticket. Total tickets available: " + ticketPool.getAvailableTickets());
                }
                Thread.sleep(ticketReleaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogUtil.logVendorActivity(name, "Thread was interrupted.");
        }
        LogUtil.logVendorActivity(name, "Stopped.");
    }

    public void stop() {
        running = false;
        LogUtil.logVendorActivity(name, "Stopping...");
    }
}
