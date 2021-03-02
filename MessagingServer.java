import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * A server program which accepts requests from clients to capitalize strings.
 * When a client connects, a new thread is started to handle it. Receiving
 * client data, capitalizing it, and sending the response back is all done on
 * the thread, allowing much greater throughput because more clients can be
 * handled concurrently.
 */
public class MessagingServer {

    /**
     * Runs the server. When a client connects, the server spawns a new thread to do
     * the servicing and immediately returns to listening. The application limits
     * the number of threads via a thread pool (otherwise millions of clients could
     * cause the server to run out of resources by allocating too many threads).
     */
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898)) {
            System.out.println("MessagingServer is running...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Server(listener.accept()));
            }
        }
    }

    private static class Server implements Runnable {
        private Socket socket;

        Server(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Connected to: " + socket);
            try {
                var in = new Scanner(socket.getInputStream());
                var out = new PrintWriter(socket.getOutputStream(), true);
                while (in.hasNextLine()) {
                    out.println("You said: " + in.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }
    }
}

