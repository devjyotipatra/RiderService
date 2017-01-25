package riderservice.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by 17349 on 23/01/17.
 */
public interface NodePartitioner<P> {

    public double getThreshold(final ArrayList<P> coordinates, final P partitionPoint,
                               final DistanceFunction<P> distanceFunction);
}
