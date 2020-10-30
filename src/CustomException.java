import java.nio.file.NoSuchFileException;

public class CustomException extends NoSuchFileException {

    public CustomException(String message) {
        super(message);
    }

}
