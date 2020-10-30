import java.io.*;

public class FileHandlerCount implements Runnable {

    private final File file;
    private final String path;
    private long count;
    private long timeOfHandle;

    public FileHandlerCount(File file) {
        this.file = file;
        this.path = file.getAbsolutePath();
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public long getCount() {
        return count;
    }

    public long getTimeOfHandle() {
        return timeOfHandle;
    }

    @Override
    public void run() {
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            long timeOfBegin = System.currentTimeMillis();
            while (reader.readLine() != null) {
                count = reader.getLineNumber();
            }
            long timeOfEnd = System.currentTimeMillis();
            timeOfHandle = timeOfEnd - timeOfBegin;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
