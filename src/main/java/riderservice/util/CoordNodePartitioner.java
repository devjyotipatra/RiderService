package riderservice.util;

import java.util.List;

/**
 * Created by 17349 on 23/01/17.
 */
public abstract class CoordNodePartitioner<Coordinate> implements NodePartitioner {

    public Coordinate getPartitionPoint(final List<Coordinate> coordinates, final Coordinate partitionPoint,
                                        final DistanceFunction<Coordinate> distanceFunction) {

        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinates list must not be empty.");
        }

        

    }
}
