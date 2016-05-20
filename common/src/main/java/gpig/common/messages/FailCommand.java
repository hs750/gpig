package gpig.common.messages;

import java.util.UUID;

/**
 * The purpose of this command is to enable failures in the demonstration. This
 * is not for 'real-world' use. Modes of failure are communication failure,
 * fatal engine / generic failure, and the battery becoming unexpectedly
 * depleted
 *
 */
public class FailCommand {
    public final UUID drone;
    public final FailType type;
    public final double batteryPercentage;

    /**
     * 
     * @param type COMMS or FATAL
     */
    public FailCommand(UUID drone, FailType type) {
        if(type == FailType.BATTERY){
            throw new IllegalArgumentException();
        }
        this.drone = drone;
        this.type = type;
        batteryPercentage = 0.0;
    }

    /**
     * 
     * @param batteryPercentage the level the battery level should be changed to
     */
    public FailCommand(UUID drone, double batteryPercentage) {
        this.drone = drone;
        this.type = FailType.BATTERY;
        this.batteryPercentage = batteryPercentage;
    }

    private FailCommand() {
        drone = null;
        type = null;
        batteryPercentage = 0.0;
    }

    public enum FailType {
        COMMS, FATAL, BATTERY
    }

}
