package gpig.dc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gpig.common.data.Constants;
import gpig.common.data.DCState;
import gpig.common.data.DeploymentArea;
import gpig.common.data.Location;
import gpig.common.messages.DeploymentCentreHeartbeat;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;
import gpig.common.messages.handlers.DeliveryNotificationHandler;
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

    private DetectionDroneDispatcher dtdd;
    private DeliveryDroneDispatcher dedd;

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

        Location initialLocation = config.dcLocations.locations.get(0);
        
        dtdd = new DetectionDroneDispatcher(msgToDts, new ImmediateReturn(),
                new DeploymentArea(initialLocation, Constants.DEPLOYMENT_SEARCH_RADIUS), msgToC2);
        msgFromDts.addHandler(dtdd);

        dedd = new DeliveryDroneDispatcher(msgToDes, new ImmediateReturn(),
                new DeploymentArea(initialLocation, Constants.DEPLOYMENT_DELIVERY_RADIUS), msgToC2);
        msgFromDes.addHandler((DeliveryDroneHeartbeatHandler) dedd);
        msgFromC2.addHandler((DeliveryAssignmentHandler) dedd);
        msgFromDes.addHandler((DeliveryNotificationHandler) dedd);

        // Forward messages from drones to C2
        new DroneMessageForwarder(msgToC2, msgFromDts, msgFromDes);

        // Recover Drones
        new DroneRecaller(id, msgFromC2, dtdd, dedd);

        // Forward fail commands to drones, DC does nothing with them itself
        new FailCommandForwarder(msgFromC2, msgToDes, msgToDts);

        DCPathHandler pathHandler = new DCPathHandler(this);
        msgFromC2.addHandler(pathHandler);

        
        movementBehaviour = new WaypointBasedMovement(initialLocation, Constants.MDC_SPEED, new NoFailsafe());
    }

    public void run() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        ses.scheduleAtFixedRate(() -> {
            if (isDeployed && movementBehaviour.isStationary()) {
                deployDrones();
            }

            movementBehaviour.step();
            dtdd.setCurrentLocation(new DeploymentArea(this.location(), Constants.DEPLOYMENT_SEARCH_RADIUS));
            dedd.setCurrentLocation(new DeploymentArea(this.location(), Constants.DEPLOYMENT_DELIVERY_RADIUS));
            
            DeploymentCentreHeartbeat msg = new DeploymentCentreHeartbeat(this.id, this.location(), dtdd.isDeployable() && dedd.isDeployable() ? DCState.ACTIVE : DCState.INACTIVE);
            msgToC2.send(msg);
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void setDeployed() {
        isDeployed = true;
    }

    private void deployDrones() {
        dtdd.deployDrones();
        dedd.deployDrones();
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
