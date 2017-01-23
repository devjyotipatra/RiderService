package riderservice.util;

/**
 * Created by 17349 on 23/01/17.
 */


public class Coordinates {
    private double lat;
    private double lon;

    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Coordinates(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }


    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lon;
    }


    public String toString() {
        return lat + " | " + lon;
    }

}
