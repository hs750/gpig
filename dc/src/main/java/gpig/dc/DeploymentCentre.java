package gpig.dc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gpig.common.data.Constants;
import gpig.common.data.DeploymentArea;
import gpig.common.data.Location;
import gpig.common.messages.DeploymentCentreHeartbeat;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;
import gpig.common.movement.ImmediateReturn;
import gpig.common.movement.MovementBehaviour;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.movement.failsafe.BatteryLevelLowFailsafe;
import gpig.common.movement.failsafe.NoFailsafe;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.dc.config.DCConfig;
import gpig.dc.dispatching.DCPathHandler;
import gpig.dc.dispatching.DeliveryDroneDispatcher;
import gpig.dc.dispatching.DetectionDroneDispatcher;
import gpig.dc.dispatching.DroneRecaller;

import static gpig.common.units.Units.kilometresPerHour;

public class DeploymentCentre {
    public final UUID id;
    public final MessageSender msgToC2;

    public final MovementBehaviour movementBehaviour;

    private boolean isDeployed = false;

    public DeploymentCentre(DCConfig config) {
        id = UUID.randomUUID();
        Log.info("Starting mobile deployment centre: %s", id);

        // C2-DC communication
        MessageReceiver msgFromC2 = new MessageReceiver();
        CommunicationChannel dcc2Channel = new CommunicationChannel(config.dcc2Channel, msgFromC2);
        msgToC2 = new MessageSender(dcc2Channel);

        // DC-DetectionDrone communication
        MessageReceiver msgFromDts = new MessageReceiver();
        CommunicationChannel dcdtChannel = new CommunicationChannel(config.dcdtChannel, msgFromDts);
        MessageSender msgToDts = new MessageSender(dcdtChannel);

        // DC-DeliveryDrone communication
        MessageReceiver msgFromDes = new MessageReceiver();
        CommunicationChannel dcdeChannel = new CommunicationChannel(config.dcdeChannel, msgFromDes);
        MessageSender msgToDes = new MessageSender(dcdeChannel);
        
        DetectionDroneDispatcher dtdd = new DetectionDroneDispatcher(msgToDts, new ImmediateReturn(), new DeploymentArea(new Location(0, 0), Constants.DEPLOYMENT_SEARCH_RADIUS), msgToC2); //TODO create this object when a true locaiton is known.
        msgFromDts.addHandler(dtdd);
        
        DeliveryDroneDispatcher dedd = new DeliveryDroneDispatcher(msgToDes, new ImmediateReturn(), new DeploymentArea(new Location(0, 0), Constants.DEPLOYMENT_DELIVERY_RADIUS), msgToC2); //TODO create this object when a true locaiton is known.
        msgFromDes.addHandler((DeliveryDroneHeartbeatHandler) dedd);

        //Forward messages from drones to C2
        new DroneMessageForwarder(msgToC2, msgFromDts, msgFromDes);
        
        // Recover Drones
        new DroneRecaller(id, msgFromC2, dtdd, dedd);

        // Forward fail commands to drones, DC does nothing with them itself
        new FailCommandForwarder(msgFromC2, msgToDes, msgToDts);

        DCPathHandler pathHandler = new DCPathHandler(this);
        msgFromC2.addHandler(pathHandler);

        Location initialLocation = config.dcLocations.locations.get(0);

        // FIXME: Use real DC speed
        movementBehaviour = new WaypointBasedMovement(initialLocation, kilometresPerHour(150.0), new NoFailsafe());
    }

    public void run() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        ses.scheduleAtFixedRate(() -> {
            if (isDeployed && movementBehaviour.isStationary()) {
                deploy();
            }

            movementBehaviour.step();

            DeploymentCentreHeartbeat msg = new DeploymentCentreHeartbeat(this.id, this.location());
            msgToC2.send(msg);
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void setDeployed() {
        isDeployed = true;
    }

    private void deploy() {

    }

    private Location location() {
        return movementBehaviour.currentLocation();
    }

    public static void main(String... args) throws IOException {
        if (args.length != 1) {
            throw new IOException("Did not specify a config path");
        }

        String configPath = args[0];
        DCConfig conf = DCConfig.getConfig(configPath, DCConfig.class);

        DeploymentCentre dc = new DeploymentCentre(conf);
        dc.run();
    }
}
