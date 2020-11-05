import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
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

    private void listen(WatchService watchService, String pathToDir) throws IOException, InterruptedException {
        //Создается ключ наблюдения для хранения возвращаемого значения операции poll
        WatchKey key;
        //Цикл while будет блокироваться до тех пор, пока условный оператор не вернется либо с ключом наблюдения, либо с нулем.
        while ((key = watchService.take()) != null) {
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assert key != null;
            watchEvent(key, pathToDir);
            //После обработки всех событий мы должны вызвать API reset , чтобы снова поставить в очередь ключ наблюдения.
            key.reset();
        }
    }

    private void watchEvent(WatchKey key, String pathToDir) throws IOException, InterruptedException {
        String sep = getSeperetor(pathToDir);
        //Когда мы получаем ключ наблюдения, тогда цикл while выполняет код внутри него.
        //Мы используем API WatchKey.pollEvents , чтобы вернуть список событий, которые произошли.
        //Затем мы используем цикл for each , чтобы обработать их один за другим.
        for (WatchEvent<?> event : key.pollEvents()) {
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
                    handleEvent(event, file);
                    break;
            }
        }
    }

    private void handleEvent(WatchEvent<?> event, File file) throws InterruptedException {
        if (Objects.equals(getFileExtension(String.valueOf(event.context())), ".xml") ||
                Objects.equals(getFileExtension(String.valueOf(event.context())), ".json")) {
            FileHandlerCount fileHandler = new FileHandlerCount(file);

            Thread thread = new Thread(fileHandler);
            long timeOfBegin = System.currentTimeMillis();
            thread.start();
            thread.join();
            long timeOfEnd = System.currentTimeMillis();

            long timeOfHandle = timeOfEnd - timeOfBegin;
            log.log(Level.INFO, "begin of handling time: " + LocalDateTime.now()
                    + ", common time of handling, ms: " + timeOfHandle +
                    ", number of lines in file: " + fileHandler.getCount());
        } else {
            FileHandlerCheck fileHandlerCheck = new FileHandlerCheck(file);

            log.log(Level.INFO, "file deleted: " + fileHandlerCheck.delete()
                    + ", name of deleted file: " + event.context());
        }

    }

    public void launch(String pathToDir) throws IOException, InterruptedException {

        Path path = Paths.get(pathToDir);
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
        if (watchService != null) {
            listen(watchService, pathToDir);
        }
    }

}
