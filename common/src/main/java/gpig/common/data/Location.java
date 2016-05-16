package gpig.common.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import gpig.common.units.Kilometres;

import java.io.IOException;

import static gpig.common.units.Units.kilometres;

/**
 * A single point on a map, defined by latitude and longitude.
 */
@JsonSerialize(using = Location.LocationSerializer.class)
@JsonDeserialize(using = Location.LocationDeserializer.class)
public class Location {
    final LatLng location;

    public Location(double latitude, double longitude) {
        location = new LatLng(latitude, longitude);
    }

    public double latitude() {
        return location.getLatitude();
    }

    public double longitude() {
        return location.getLongitude();
    }

    /**
     * 
     * @param that
     * @return The distance from this location to that location
     */
    public Kilometres distanceFrom(Location that) {
        double km = LatLngTool.distance(location, that.location, LengthUnit.KILOMETER);
        return kilometres(km);
    }

    /**
     * 
     * @param that
     * @return The bearing of that location from this location
     */
    public double bearingOf(Location that) {
        double b = LatLngTool.initialBearing(location, that.location);
        return b;
    }

    /**
     * 
     * @param bearing
     * @param distance
     * @return The location from this location given a bearing and distance
     */
    public Location locationAt(double bearing, Kilometres distance) {
        LatLng l = LatLngTool.travel(location, bearing, distance.value(), LengthUnit.KILOMETER);
        Location loc = new Location(l.getLatitude(), l.getLongitude());
        return loc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Location that = (Location) o;
        return location.equals(that.location);
    }

    private Location() {
        location = null;
    }

    public static class LocationSerializer extends JsonSerializer<Location> {
        @Override
        public void serialize(Location value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            gen.writeStartObject();
            gen.writeNumberField("lat", value.latitude());
            gen.writeNumberField("lon", value.longitude());
            gen.writeEndObject();
        }
    }

    public static class LocationDeserializer extends JsonDeserializer<Location> {
        @Override
        public Location deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            JsonNode latitudeNode = node.get("lat");
            JsonNode longitudeNode = node.get("lon");

            if (!latitudeNode.isNumber())
                throw new IllegalArgumentException("Latitude was non-numeric");
            if (!longitudeNode.isNumber())
                throw new IllegalArgumentException("Longitude was non-numeric");

            double latitude = node.get("lat").asDouble();
            double longitude = node.get("lon").asDouble();

            return new Location(latitude, longitude);
        }
    }
}
