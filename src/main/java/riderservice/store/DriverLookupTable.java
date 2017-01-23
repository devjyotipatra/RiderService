package riderservice.store;

import riderservice.util.DriverCoordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 17349 on 24/01/17.
 */
public class DriverLookupTable {

    final static Map<Integer, DriverCoordinate> driverMap = new HashMap<Integer, DriverCoordinate>();

    public void putDriverCoordinate(Integer id, double lat, double lon) {
        driverMap.put(id, new DriverCoordinate(id, lat, lon));
    }

    public List<DriverCoordinate> getDriverCoordList() {
        return driverMap.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());
    }
}
