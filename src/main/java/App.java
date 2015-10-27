public class App {

    private Client client;
    private EventLogger eventLogger;

    public static void main(String[] args) {
        App app = new App();

        app.client = new Client(1l, "John Doe");
        app.eventLogger = new ConsoleEventLogger();

        app.logEvent("Event for user 1");
    }

    private void logEvent(String event) {
        String message = event.replaceAll(String.valueOf(client.getId()), client.getFullName());
        eventLogger.logEvent(message);
    }
}
