package gpig.drones.delivery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import gpig.common.data.Assignment;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.messages.DeliveryNotification;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.drones.delivery.config.DeliveryDroneConfig;

public class DeliveryDrone {
    private final MessageSender msgToDC;
    private DroneState state;
    private Location location;
    private Assignment assignment;
    private Path path;

    public DeliveryDrone(DeliveryDroneConfig config) {
        Log.info("Starting delivery drone");

        this.state = DroneState.UNDEPLOYED;

        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dedcChannel, msgFromDC);
        msgToDC = new MessageSender(dtdcChannel);

        msgFromDC.addHandler(new DeliveryDroneAssignmentHandler(this));
    }

    public void run() {
        // Put logic to tick, move and deliver.
        // On delivery, call "onAssignmentDelivery" method.
    }

    public DroneState getState() {
        return this.state;
    }

    public void assign(Assignment assignment) {
        if (this.assignment == null || this.assignment.status == Assignment.AssignmentStatus.DELIVERED) {
            this.assignment = assignment;
            calculatePath();
        }
    }

    private void calculatePath() {
        Location deliveryDestination = assignment.detection.person.location;
        Location returnDestination = this.location;

        List<Path.Waypoint> waypoints = Collections.synchronizedList(new ArrayList<>());
        waypoints.add(new Path.Waypoint(deliveryDestination));
        waypoints.add(new Path.Waypoint(returnDestination));

        Path path = new Path(waypoints);
        this.path = path;
    }

    private void onAssignmentDelivery() {
        this.assignment.status = Assignment.AssignmentStatus.DELIVERED;
        DeliveryNotification msg = new DeliveryNotification(new Date(), assignment);
        msgToDC.send(msg);
    }

    public static void main(String... args) throws IOException {
        if (args.length != 1) {
            throw new IOException("Did not specify a config path");
        }

        String configPath = args[0];
        DeliveryDroneConfig conf = DeliveryDroneConfig.getConfig(configPath, DeliveryDroneConfig.class);

        DeliveryDrone drone = new DeliveryDrone(conf);
        drone.run();
    }
}
