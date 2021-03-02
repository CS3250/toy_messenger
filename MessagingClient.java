import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MessagingClient {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: MessagingClient <server IP address>");
            return;
        }
        try (var socket = new Socket(args[0], 59898)) {
            System.out.println("Enter message to be sent:");
            var scanner = new Scanner(System.in);
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
                System.out.println(in.nextLine());
            }
        }
    }
}
