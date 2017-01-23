package riderservice;

/**
 * Created by 17349 on 23/01/17.
 */


import java.util.*;


/**
 * A single node of a LocationStore tree. Nodes can either be leaf nodes that contain geo coordinates or branch nodes
 * that have a "less than threshold: Near" and "more than threshold: Far" children nodes.
 *
 */
class LocationNode<P, E extends P> {

    private final int cardinality;
    private final DistanceFunction<P> distanceFunction;
    //private final ThresholdSelectionStrategy<P, E> thresholdSelectionStrategy;

    private LinkedList<E> coordinates;

    private final E partitionPoint;

    private double threshold;

    private LocationNode<P, E> near;
    private LocationNode<P, E> far;

    /**
     * Constructs a new node that contains the given collection of points. If the given collection of points is larger
     * than the specified maximum capacity, the new node will partition the collection of points into child
     * nodes using the given distance function.
     *
     * @param coordinates      the collection of points to store in or below this node
     * @param distanceFunction the distance function to use when partitioning points
     * @param cardinality      the desired maximum capacity of this node; this node may contain more points than the given
     *                         capacity if the given collection of points cannot be partitioned (for example, because all of the points are an
     *                         equal distance away from the vantage point)
     */
    public LocationNode(final Collection<E> coordinates, final DistanceFunction<P> distanceFunction, final int cardinality) {

        if (cardinality <= 0) {
            throw new IllegalArgumentException("Cardinality must be positive");
        }


        this.cardinality = cardinality;
        this.distanceFunction = distanceFunction;
        this.coordinates = new LinkedList<E>(coordinates);

        // All nodes must have some point using which the coordinates set will be partitioned between children of the node
        this.partitionPoint = this.coordinates.get(new Random().nextInt(coordinates.size()));
    }

    protected void partition() {
        if (this.coordinates == null) {
            if (this.near.size() == 0 || this.far.size() == 0) {
                this.coordinates = new LinkedList<E>();
                this.addAllPointsToCollection(this.coordinates);

                this.near = null;
                this.far = null;

                this.partition();
            } else {
                this.near.partition();
                this.far.partition();
            }
        } else {
            if (this.coordinates.size() > this.cardinality) {
                // Partially sort the list such that all points closer than or equal to the threshold distance from the
                // partition point come before the partition point in the list and all points farther away come after
                // this point.
                this.threshold = this.thresholdSelectionStrategy.selectThreshold(this.coordinates, this.partitionPoint,
                        this.distanceFunction);

                try {
                    final int firstIndexPastThreshold =
                            VPTreeNode.partitionPoints(this.points, this.vantagePoint, this.threshold, this.distanceFunction);

                    this.closer = new VPTreeNode<>(this.points.subList(0, firstIndexPastThreshold), this.distanceFunction, this.thresholdSelectionStrategy, this.capacity);
                    this.farther = new VPTreeNode<>(this.points.subList(firstIndexPastThreshold, this.points.size()), this.distanceFunction, this.thresholdSelectionStrategy, this.capacity);

                    this.points = null;
                } catch (final PartitionException e) {
                    // We couldn't partition the list, so just store all of the points in this node
                    this.closer = null;
                    this.farther = null;
                }
            }
        }
    }

}
