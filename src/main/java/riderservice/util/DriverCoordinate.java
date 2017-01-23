package riderservice.util;

/**
 * Created by 17349 on 23/01/17.
 */


public class DriverCoordinate {
    private int driverId;
    private double lat;
    private double lon;

    public DriverCoordinate(int driverId, double lat, double lon) {
        this.driverId = driverId;
        this.lat = lat;
        this.lon = lon;
    }

    public DriverCoordinate(int driverId, float lat, float lon) {
        this.driverId = driverId;
        this.lat = lat;
        this.lon = lon;
    }


    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lon;
    }

    public int getDriverId() {
        return driverId;
    }


    public String toString() {
        return lat + " | " + lon;
    }

}
