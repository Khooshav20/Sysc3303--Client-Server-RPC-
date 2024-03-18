package src;

/**
 * The Constants class stores constants used in the application.
 */
public class Constants {
    public static final int CLIENT_INTERMEDIATE_PORT = 23; // Port for the client
    public static final int SERVER_INTERMEDIATE_PORT = 69; // Port for the server

    /**
     * Causes the current thread to sleep for 1000 milliseconds.
     */
    public static void sleep() {
        sleep(1000);
    }

    /**
     * Causes the current thread to sleep for the specified amount of time.
     *
     * @param time The duration of time to sleep in milliseconds.
     */
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}