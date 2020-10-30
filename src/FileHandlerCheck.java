import java.io.File;

public class FileHandlerCheck {

    private final File file;

    public FileHandlerCheck(File file) {
        this.file = file;
    }

    public boolean delete() {
        return file.delete();
    }
}
