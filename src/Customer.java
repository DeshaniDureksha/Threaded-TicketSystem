public class Customer implements Runnable {
    private final String name;
    private final int retrievalRate;
    private final TicketPool ticketPool;
    private volatile boolean running = true;

    public Customer(String name, int retrievalRate, TicketPool ticketPool) {
        this.name = name;
        this.retrievalRate = retrievalRate;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        try {
            while (running) {
                synchronized (ticketPool) {
                    String ticket = ticketPool.removeTicket(name);
                    if (ticket != null) {
                        LogUtil.logCustomerActivity(name, "Purchased " + ticket + ".");
                    } else {
                        LogUtil.logCustomerActivity(name, "Attempted to purchase a ticket, but none were available.");
                    }
                }
                Thread.sleep(retrievalRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogUtil.logCustomerActivity(name, "Thread was interrupted.");
        }
        LogUtil.logCustomerActivity(name, "Stopped.");
    }

    public void stop() {
        running = false;
        LogUtil.logCustomerActivity(name, "Stopping...");
    }
}
