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
}
