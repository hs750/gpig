package gpig.dc.config;

import gpig.common.config.CommonConfig;
import gpig.common.config.CommunicationChannelConfig;
import gpig.common.config.LocationsConfig;

public class DCConfig extends CommonConfig {
    public CommunicationChannelConfig dcc2Channel;
    public CommunicationChannelConfig dcdtChannel;
    public CommunicationChannelConfig dcdeChannel;
    public LocationsConfig dcLocations;
}
