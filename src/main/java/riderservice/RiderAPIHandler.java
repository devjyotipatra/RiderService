package riderservice;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import riderservice.store.DriverLocationStore;
import riderservice.store.DriverLookupTable;
import riderservice.util.CoordDistanceFunction;
import riderservice.util.CoordNodePartitioner;
import riderservice.util.DriverCoordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
                                         ;
    }


    public DriverLookupTable getLookupTable() {
        return lookupTable;
    }

    public int getMaxCardinality() {
        return NODE_CARDINALITY;
    }

}
