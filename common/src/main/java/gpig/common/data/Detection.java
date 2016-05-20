package gpig.common.data;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;

public class Detection {
    public final Person person;
    public final File image;
    public final LocalDateTime timestamp;

    public Detection(Person person, File image, LocalDateTime timestamp) {
        this.person = person;
        this.timestamp = timestamp;
        this.image = image;
    }

    private Detection() {
        person = null;
        timestamp = null;
        image = null;
    }
}
