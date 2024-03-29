package gpig.drones.delivery;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gpig.common.battery.Battery;
import gpig.common.data.Constants;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.messages.heartbeater.LocationProvider;
import gpig.common.messages.heartbeater.StateProvider;
import gpig.common.movement.MovementBehaviour;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.movement.failsafe.BatteryLevelLowFailsafe;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.FallibleMessageSender;
import gpig.common.networking.MessageReceiver;
import gpig.common.util.Log;
import gpig.drones.delivery.config.DeliveryDroneConfig;

public class DeliveryDrone {
    private final FallibleMessageSender msgToDC;
    UUID thisDrone;
    StateProvider state;
    MovementBehaviour movementBehaviour;
    Battery battery;
    Location dcLocation;
    int heartbeatRate;

    public DeliveryDrone(DeliveryDroneConfig config) {
        Log.info("Starting delivery drone");

        heartbeatRate = config.heartbeatRate;
        thisDrone = UUID.randomUUID();

        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dedcChannel, msgFromDC);
        msgToDC = new FallibleMessageSender(dtdcChannel, thisDrone);
        msgFromDC.addHandler(msgToDC);
        
        state = new StateProvider();
        dcLocation = new Location(0, 0);

        battery = new Battery(Constants.AERIAL_VEHICLE_BATTERY_DURATION);
        Location homeLocation = new Location(0.0, 0.0);
        movementBehaviour = new DeliveryPathMovementBehaviour(homeLocation, Constants.DELIVERY_DRONE_SPEED,
                new BatteryLevelLowFailsafe(battery, homeLocation), msgToDC, thisDrone);

        msgFromDC.addHandler(new DeliveryPathHandler(this));
        msgFromDC.addHandler(new DeliveryFatalFailureHandler(this));
        msgFromDC.addHandler(new DeliveryBatteryFailureHandler(this));

        new DeliveryHeartbeater(thisDrone, msgToDC, (LocationProvider) movementBehaviour, state, heartbeatRate);
    }

    public void run() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        ses.scheduleAtFixedRate(() -> {
            if (isDeployed()) {
                movementBehaviour.step();
                synchronized (this) {
                    if (movementBehaviour.currentLocation().equals(dcLocation) && movementBehaviour.isStationary()) {
                        Log.info("Returned to DC");
                        setReturned();
                    }
                }
            }

        }, 0, heartbeatRate, TimeUnit.MILLISECONDS);
    }

    public DroneState getState() {
        return this.state.getState();
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

    public void setDeployed() {
        state.setOutbound();
    }

    public void setReturned() {
        state.setUndeployed();
    }

    public boolean isDeployed() {
        return state.getState() != DroneState.UNDEPLOYED;
    }

    public void setCrashed() {
        state.setCrashed();
    }
}
