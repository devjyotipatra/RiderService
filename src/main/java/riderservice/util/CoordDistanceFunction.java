package riderservice.util;

/**
 * Created by 17349 on 23/01/17.
 */
public class CoordDistanceFunction implements DistanceFunction<DriverCoordinate> {

    /**
     * Returns the Manhatten distance between the given pair of points.
     *
     * @param coord1 Coordinates of point 1
     * @param coord2 Coordinates of point 2
     *
     * @return the Manhatten distance between the given two points
     *
     */

    public double getDistance(DriverCoordinate coord1, DriverCoordinate coord2) {
        double x2 = (coord1.getLatitude() - coord2.getLatitude()) * (coord1.getLatitude() - coord2.getLatitude());
        double y2 = (coord1.getLongitude() - coord2.getLongitude()) * (coord1.getLongitude() - coord2.getLongitude());

        return Math.sqrt(x2 + y2);
    }
}
