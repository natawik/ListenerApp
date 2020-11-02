import java.io.*;

public class FileHandlerCount implements Runnable {

    private final File file;
    private final String path;
    private long count;

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

    @Override
    public void run() {
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                count = reader.getLineNumber();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
