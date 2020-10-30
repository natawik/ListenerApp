import java.io.IOException;
import java.util.Scanner;

public class ListenerApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter path to the directory you want to track:");
        String pathToDir = scanner.nextLine();
        Listener listener = new Listener();
        listener.launch(pathToDir);
    }
}
