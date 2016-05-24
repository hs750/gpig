package gpig.common.data;

import java.util.UUID;

/**
 * Represents a single person and their type. This data can be used in multiple
 * contexts - e.g. a detection, an assignment, etc.
 */
public class Person {
    public UUID id;
    public final PersonType type;
    public final Location location;

    public Person(PersonType personType, Location location) {
        this.id = java.util.UUID.randomUUID();
        this.type = personType;
        this.location = location;
    }

    public enum PersonType {
        CIVILIAN,
        RESCUE_TEAM,
        OTHER,
    }

    private Person() {
        id = null;
        type = null;
        location = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
