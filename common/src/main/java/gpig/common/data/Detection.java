package gpig.common.data;

import java.io.File;
import java.util.Date;

public class Detection {
    public final Location location;
    public final Person person;
    public final File image;
    public final Date timestamp;

    public Detection(Location location, Person person, File image, Date timestamp) {
        this.location = location;
        this.person = person;
        this.timestamp = timestamp;
        this.image = image;
    }

    private Detection() {
        location = null;
        person = null;
        timestamp = null;
        image = null;
    }
}
