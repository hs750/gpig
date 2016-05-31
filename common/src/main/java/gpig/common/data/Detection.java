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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        result = prime * result + ((person == null) ? 0 : person.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Detection other = (Detection) obj;
        if (person == null) {
            if (other.person != null)
                return false;
        } else if (!person.equals(other.person))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }
}
