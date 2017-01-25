package riderservice;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import riderservice.store.DriverLocationStore;
import riderservice.store.DriverLookupTable;
import riderservice.util.CoordDistanceFunction;
import riderservice.util.CoordNodePartitioner;
import riderservice.util.DriverCoordinate;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import static java.lang.Thread.sleep;

/**
 * Created by 17349 on 24/01/17.
 */
public class RiderAPIHandler {

    private final static int NODE_CARDINALITY = 6;

    private final static ReentrantReadWriteLock driverlocationstore_lock = new ReentrantReadWriteLock();

    private static DriverLookupTable lookupTable = null;

    private static DriverLocationStore<DriverCoordinate> store = null;

    private static RiderAPIHandler instance = null;



    RiderAPIHandler() {
        store = new DriverLocationStore<DriverCoordinate>(Collections.emptyList(),
                new CoordDistanceFunction(),
                new CoordNodePartitioner(),
                NODE_CARDINALITY);

        lookupTable = new DriverLookupTable();
    }

    public static RiderAPIHandler getHandlerInstance() {
        if(instance == null) {
            instance = new RiderAPIHandler();
        }

        return instance;
    }


    public void setTreeRootNode(DriverLocationStore newStore) {
        driverlocationstore_lock.writeLock().lock();
        try {
            store.setRootNode(newStore.getRootNode());
        } finally {
            driverlocationstore_lock.writeLock().unlock();
        }
    }

    public void setDriverLocation(Map<String, Object> driverCoord) {
        try {
            double latitude = (Double) driverCoord.get("latitude");
            double longitude = (Double) driverCoord.get("longitude");

            lookupTable.putDriverCoordinate((Integer) driverCoord.get("id"),
                                            latitude, longitude);
        } catch (ValueException ex) {
            System.out.println("Latitude or Longitude format incorrect "+ ex.toString());
        }

    }


    public List<String> getDriverCoordinate(double latitude, double longitude, double radius, int limit) {
        List<String> drivers = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        List<DriverCoordinate> listOfDrivers = store.getNearestNeighbors(new DriverCoordinate(-1, latitude, longitude), radius, limit);
        for(DriverCoordinate driver : listOfDrivers) {
            sb.append("{").append("id:").append(driver.getDriverId())
                    .append(",latitude:").append(driver.getLatitude())
                    .append("longitude:").append(driver.getLongitude())
                    .append("}");
            drivers.add(sb.toString());
            sb.setLength(0);
        }

        return drivers;
    }


    public DriverLookupTable getLookupTable() {
        return lookupTable;
    }

    public int getMaxCardinality() {
        return NODE_CARDINALITY;
    }

}
