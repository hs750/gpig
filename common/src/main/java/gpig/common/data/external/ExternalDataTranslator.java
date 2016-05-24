package gpig.common.data.external;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import gpig.all.schema.BoundingBox;
import gpig.all.schema.Coord;
import gpig.all.schema.GISPosition;
import gpig.all.schema.GPIGData;
import gpig.all.schema.Image;
import gpig.all.schema.Point;
import gpig.all.schema.Polar;
import gpig.all.schema.Position;
import gpig.all.schema.Timestamp;
import gpig.all.schema.datatypes.Delivery;
import gpig.all.schema.datatypes.StrandedPerson;
import gpig.common.data.Detection;
import gpig.common.data.Location;
import gpig.common.data.Person;
import gpig.common.data.Person.PersonType;
import gpig.common.messages.DeliveryNotification;
import gpig.common.units.Kilometres;
import gpig.common.util.Log;

public class ExternalDataTranslator {

    public static List<Detection> extractDetections(GPIGData data) {
        ArrayList<Detection> detections = new ArrayList<>();

        for (GISPosition item : data.positions) {
            if (item.payload instanceof StrandedPerson) {
                Position p = item.position;
                Location l = null;

                if (p instanceof Point) {
                    l = new Location(((Point) p).coord.latitude, ((Point) p).coord.longitude);
                } else if (p instanceof Polar) {
                    l = new Location(((Polar) p).point.latitude, ((Polar) p).point.longitude);
                } else if (p instanceof BoundingBox) {
                    Location l1 = new Location(((BoundingBox) p).topleft.latitude, ((BoundingBox) p).topleft.longitude);
                    Location l2 = new Location(((BoundingBox) p).topright.latitude,
                            ((BoundingBox) p).topright.longitude);

                    double b = l1.bearingOf(l2);
                    Kilometres d = l1.distanceFrom(l2);

                    // location in the middle of the bounding box
                    l = l1.locationAt(b, new Kilometres(d.value() / 2));
                } else {
                    Log.error("Cannot have detections in Ploygon");
                }

                if (l != null) {
                    Detection d = new Detection(new Person(PersonType.OTHER, l),
                            new File(((StrandedPerson) item.payload).image.url), toLocalDateTime(item.timestamp.date));
                    detections.add(d);
                }
            }
        }

        return detections;
    }

    public static GISPosition export(Detection detection) {
        GISPosition gis = new GISPosition();
        gis.timestamp = new Timestamp();
        gis.timestamp.date = toDate(detection.timestamp);

        Point p = new Point();
        p.coord = new Coord();
        p.coord.latitude = new Float(detection.person.location.latitude());
        p.coord.longitude = new Float(detection.person.location.longitude());
        gis.position = p;

        StrandedPerson sp = new StrandedPerson();
        sp.image = new Image();
        sp.image.url = detection.image.toString();
        gis.payload = sp;

        return gis;
    }

    public static GISPosition export(DeliveryNotification delivery) {
        GISPosition gis = new GISPosition();
        gis.timestamp = new Timestamp();
        gis.timestamp.date = toDate(delivery.timestamp);

        Point p = new Point();
        p.coord = new Coord();
        p.coord.latitude = new Float(delivery.assignment.detection.person.location.latitude());
        p.coord.longitude = new Float(delivery.assignment.detection.person.location.longitude());
        gis.position = p;

        Delivery del = new Delivery();
        gis.payload = del;

        return gis;

    }

    public static GPIGData export(Collection<Detection> detections, Collection<DeliveryNotification> deliveries) {
        if (detections == null) {
            detections = Collections.emptyList();
        }
        if (deliveries == null) {
            deliveries = Collections.emptyList();
        }

        GPIGData data = new GPIGData();
        data.positions = new HashSet<>();

        detections.forEach(detection -> data.positions.add(export(detection)));
        deliveries.forEach(delivery -> data.positions.add(export(delivery)));

        return data;
    }

    public static String serialise(GPIGData data) {
        try {
            StringWriter sw = new StringWriter();
            JAXBContext jaxbContext = null;

            jaxbContext = JAXBContext.newInstance(GPIGData.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.marshal(data, sw);

            return sw.toString();
        } catch (JAXBException e) {
            Log.error("Unable to serialise GPIGData instance");
            e.printStackTrace();
        }

        return "";
    }

    public static GPIGData deserialise(String data) {
        GPIGData gpd = null;
        try {
            StringReader sr = new StringReader(data);
            JAXBContext jaxbContext = null;

            jaxbContext = JAXBContext.newInstance(GPIGData.class);
            Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();

            gpd = (GPIGData) jaxbMarshaller.unmarshal(sr);

        } catch (JAXBException e) {
            Log.error("Unable to deserialise GPIGData: %s", data);
            e.printStackTrace();
        }

        return gpd;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
