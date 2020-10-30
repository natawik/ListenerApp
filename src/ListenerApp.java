import java.io.IOException;
import java.util.Scanner;
import java.util.logging.LogManager;

public class ListenerApp {
    public static void main(String[] args) throws IOException, InterruptedException {

        LogManager.getLogManager().readConfiguration(Listener.class.getResourceAsStream("logging.properties"));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter path to the directory you want to track:");
        String pathToDir = scanner.nextLine();
        Listener listener = new Listener();
        listener.launch(pathToDir);
    }
}
