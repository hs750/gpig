package gpig.dc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;
import gpig.common.networking.MessageSender;
import gpig.common.units.Kilometres;

public class DetectionDroneDispatcher extends Thread implements DetectionDroneHeartbeatHandler {
    private final Kilometres edgeDistance = new Kilometres(8.6);
    private Location currentLocation;
    private boolean deployable = false;

    private HashMap<UUID, DetectionDroneHeartbeat> allDrones;
    private LinkedHashSet<UUID> inactiveDrones;
    private MessageSender messager;
    
    public DetectionDroneDispatcher(MessageSender messager) {
        this.messager = messager;
        allDrones = new HashMap<>();
        inactiveDrones = new LinkedHashSet<>();
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
    }

    public void deployDrones() throws NullPointerException{
        if(currentLocation == null){
            throw new NullPointerException("Unknown current location");
        }
        if (!deployable) {
            deployable = true;
            start();
        }
    }

    private ArrayList<Path> calculateSearchPattern() {
        // Produces a basic hexagon pattern

        ArrayList<Path> deployments = new ArrayList<>();

        for (int segment = 0; segment < 6; segment++) {
            Location l1 = currentLocation.locationAt(segment * 60, edgeDistance);
            Location l2 = currentLocation.locationAt((segment * 60 + 60) % 360, edgeDistance);

            ArrayList<Waypoint> waypoints = new ArrayList<>();
            waypoints.add(new Waypoint(l1)); // out bound
            waypoints.add(new Waypoint(l2)); // second point
            waypoints.add(new Waypoint(currentLocation)); // home

            Path p = new Path(waypoints);
            deployments.add(p);

        }

        return deployments;

    }

    public void recoverDrones() {
        deployable = false;
        Waypoint w = new Waypoint(currentLocation);
        ArrayList<Waypoint> wps = new ArrayList<>();
        wps.add(w);
        Path p = new Path(wps);
        
        for(UUID drone : allDrones.keySet()){
            if(!inactiveDrones.contains(drone)){
                SetPath sp = new SetPath(p, drone);
                messager.send(sp);
            }
        }

    }

    public boolean allDronesRecovered() {
        boolean recovered = true;
        for (UUID drone : allDrones.keySet()) {
            if (!inactiveDrones.contains(drone)) {
                recovered = false;
            }
        }
        return recovered;
    }

    @Override
    public void run() {
        super.run();
        ArrayList<Path> searchPaths = calculateSearchPattern();
        int searchsDeployed = 0;
        while (deployable) {
            while(!inactiveDrones.iterator().hasNext()){
                try {
                    Thread.sleep(1000); //Wait for a drone to become available
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            UUID assignee = getNextInactiveDrone();
            
            SetPath sp = new SetPath(searchPaths.get(0), assignee);
            messager.send(sp);
            searchsDeployed++;
            
            if (searchsDeployed == searchPaths.size()) {
                deployable = false;
            }
            
            activateDrone(assignee);
        }
    }
    
    private synchronized void activateDrone(UUID drone){
        inactiveDrones.remove(drone);
    }
    
    private synchronized UUID getNextInactiveDrone(){
        return (UUID) inactiveDrones.iterator().next();
    }
    
    private synchronized void deactivateDrone(UUID drone){
        inactiveDrones.add(drone);
    }

    @Override
    public void handle(DetectionDroneHeartbeat message) {
        allDrones.put(message.origin, message);
        if (message.deployed) {
            activateDrone(message.origin);
        } else {
            deactivateDrone(message.origin);
        }

    }

}
