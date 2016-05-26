package gpig.c2.config;

import java.net.URL;
import java.util.List;

import gpig.common.config.CommonConfig;
import gpig.common.config.CommunicationChannelConfig;
import gpig.common.config.DetectionsConfig;
import gpig.common.config.LocationsConfig;

public class C2Config extends CommonConfig {
    public CommunicationChannelConfig c2dcChannel;
    public LocationsConfig dcLocations;
    public DetectionsConfig victimDetections;
    public List<URL> interoperabilityInputs;
}
