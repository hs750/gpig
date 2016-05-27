package gpig.drones.detection.config;

import java.io.File;

import gpig.common.config.CommonConfig;
import gpig.common.config.CommunicationChannelConfig;

public class DetectionDroneConfig extends CommonConfig {
    public CommunicationChannelConfig dtdcChannel;
    public File detectionConfig;
}
