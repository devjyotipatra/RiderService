package riderservice.util;

/**
 * Created by 17349 on 23/01/17.
 */
public interface NodePartitioner<P> {

    public P getPartitionPoint(final List<P> points, final P partitionPoint, final DistanceFunction<P> distanceFunction);
}
