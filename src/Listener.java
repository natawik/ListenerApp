import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Listener {

    private static final Logger log = Logger.getLogger(Listener.class.getName());

    private static String getFileExtension(String ext) {
        int index = ext.indexOf('.');
        return index == -1 ? null : ext.substring(index);
    }

    private static String getSeperetor(String path) {
        if (path.contains("/")) return "/";
        return "\\";
    }

    public void launch(String pathToDir) throws InterruptedException, IOException {

        LogManager.getLogManager().readConfiguration(Listener.class.getResourceAsStream("logging.properties"));

        Path path = Paths.get(pathToDir);
        String sep = getSeperetor(pathToDir);

        WatchService watchService = null;
        try {
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (NoSuchFileException e) {
            log.log(Level.WARNING, "Wrong path was entered");
            throw new CustomException("Wrong path was entered");
        } catch (IOException e) {
            e.printStackTrace();
            log.log(Level.WARNING, "Some IO error");
        }

        while (true) {
            WatchKey key = null;
            try {
                assert watchService != null;
                key = watchService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assert key != null;
            for (WatchEvent event : key.pollEvents()) {
                switch (event.kind().name()) {
                    case "OVERFLOW":
                        System.out.println("We lost some events");
                        break;
                    case "ENTRY_CREATE":
                        File file = new File(pathToDir + sep + event.context());

                        BasicFileAttributes attr = Files.readAttributes(
                                Paths.get(pathToDir + sep + event.context()),
                                BasicFileAttributes.class);

                        log.log(Level.INFO, "name of file: " + event.context().toString() +
                                ", creation time: " + attr.creationTime());

                        if (Objects.equals(getFileExtension(String.valueOf(event.context())), ".xml") ||
                                Objects.equals(getFileExtension(String.valueOf(event.context())), ".json")) {
                            FileHandlerCount fileHandler = new FileHandlerCount(file);
                            Thread thread1 = new Thread(fileHandler);
                            thread1.start();

                            log.log(Level.INFO, "begin of handling time: " + LocalDateTime.now()
                                    + ", common time of handling, ms: " + fileHandler.getTimeOfHandle() +
                                    ", number of lines in file: " + fileHandler.getCount());
                            thread1.join(fileHandler.getTimeOfHandle());
                        } else {
                            FileHandlerCheck fileHandlerCheck = new FileHandlerCheck(file);

                            log.log(Level.INFO, "file deleted: " + fileHandlerCheck.delete()
                                    + ", name of deleted file: " + event.context());
                        }
                        break;
                }
            }
            key.reset();
        }
    }

}
