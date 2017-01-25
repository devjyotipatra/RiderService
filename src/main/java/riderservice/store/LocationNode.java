package riderservice.store;

/**
 * Created by 17349 on 23/01/17.
 */


import riderservice.util.DistanceFunction;
import riderservice.util.NodePartitioner;
import riderservice.util.NearestNeighborFinder;

import java.util.*;


/**
 * A single node of a LocationStore tree. Nodes can either be leaf nodes that contain geo coordinates or branch nodes
 * that have a "less than threshold: Near" and "more than threshold: Far" children nodes.
 *
 */
class LocationNode<P> {

    private final int cardinality;
    private final DistanceFunction<P> distanceFunction;
    private final NodePartitioner<P> partitioner;

    private ArrayList<P> coordinates;

    private final P partitionPoint;

    private double threshold;

    private LocationNode<P> near;
    private LocationNode<P> far;

    /**
     * Constructs a new node that contains the given collection of points. If the given collection of points is larger
     * than the specified maximum capacity, the new node will partition the collection of points into child
     * nodes using the given distance function.
     *
     */
    public LocationNode(final Collection<P> coordinates, final DistanceFunction<P> distanceFunction,
                        NodePartitioner<P> partitioner, final int cardinality) {

        if (cardinality <= 0) {
            throw new IllegalArgumentException("Cardinality must be positive");
        }


        this.cardinality = cardinality;
        this.distanceFunction = distanceFunction;
        this.partitioner = partitioner;
        this.coordinates = new ArrayList<P>(coordinates);

        // All nodes must have some point using which the coordinates set will be partitioned between children of the node
        this.partitionPoint = this.coordinates.get(new Random().nextInt(coordinates.size()));
        System.out.println("partitionPoint=> " + partitionPoint);
    }



    public void partition() {
        if (this.coordinates == null) {
            if (this.near.size() == 0 || this.far.size() == 0) {
                this.coordinates = new ArrayList<P>();
                this.addAllPointsToCollection(this.coordinates);

                this.near = null;
                this.far = null;

                this.partition();
            } else {
                this.near.partition();
                this.far.partition();
            }
        } else {
            if (this.size() > this.cardinality) {
                this.threshold = partitioner.getThreshold(this.coordinates, this.partitionPoint,
                        this.distanceFunction);

                System.out.println("this.threshold=> " + this.threshold);

                final int firstIndexPastThreshold =
                        LocationNode.partitionPoints(this.coordinates, this.partitionPoint, this.threshold, this.distanceFunction);

                this.near = new LocationNode<>(this.coordinates.subList(0, firstIndexPastThreshold), this.distanceFunction,
                        this.partitioner, this.cardinality);

                this.far = new LocationNode<>(this.coordinates.subList(firstIndexPastThreshold, this.coordinates.size()),
                        this.distanceFunction, this.partitioner, this.cardinality);

                //this.coordinates = null;
            }
        }
    }


    private static <P> int partitionPoints(final List<P> coordinates, final P partitionPoint, final double threshold,
                                           final DistanceFunction<P> distanceFunction)  {
        int i = 0;
        int j = coordinates.size() - 1;

        // This is, essentially, a single swapping quicksort iteration
        for (; i <= j; i++) {
            if (distanceFunction.getDistance(partitionPoint, coordinates.get(i)) > threshold) {
                for (; j >= i; j--) {
                    if (distanceFunction.getDistance(partitionPoint, coordinates.get(j)) <= threshold) {
                        Collections.swap(coordinates, i, j--);
                        break;
                    }
                }
            }
        }

        final int firstIndexPastThreshold = distanceFunction.getDistance(partitionPoint, coordinates.get(i - 1)) > threshold ? i - 1 : i;

        return firstIndexPastThreshold;

        /*if (distanceFunction.getDistance(partitionPoint, coordinates.get(0)) <= threshold &&
                distanceFunction.getDistance(partitionPoint, coordinates.get(coordinates.size() - 1)) > threshold) {

            return firstIndexPastThreshold;
        } else {

        }*/
    }


    public int size() {
        if (this.coordinates == null) {
            return this.near.size() + this.far.size();
        } else {
            return this.coordinates.size();
        }
    }


    private void addAllPointsToCollection(final Collection<P> collection) {
        if (this.coordinates == null) {
            this.near.addAllPointsToCollection(collection);
            this.far.addAllPointsToCollection(collection);
        } else {
            collection.addAll(this.coordinates);
        }
    }


    private LocationNode<P> getChildNodeForPoint(final P point) {
        return this.distanceFunction.getDistance(this.partitionPoint, point) <= this.threshold ? this.near : this.far;
    }


    public void collectNearestNeighbors(final NearestNeighborFinder<P> collector) {
        if (this.coordinates == null) {
            final LocationNode<P> firstNodeSearched = this.getChildNodeForPoint(collector.getQueryPoint());
            firstNodeSearched.collectNearestNeighbors(collector);

            final double distanceFromVantagePointToQueryPoint =
                    this.distanceFunction.getDistance(this.partitionPoint, collector.getQueryPoint());

            final double distanceFromQueryPointToFarthestPoint =
                    this.distanceFunction.getDistance(collector.getQueryPoint(), collector.getFarthestPoint());

            if (firstNodeSearched == this.near) {
                // We've already searched the node that contains points within this node's threshold. We also want to
                // search the farther node if the distance from the query point to the most distant point in the
                // neighbor collector is greater than the distance from the query point to this node's threshold, since
                // there could be a point outside of this node that's closer than the most distant neighbor we've found
                // so far.

                final double distanceFromQueryPointToThreshold = this.threshold - distanceFromVantagePointToQueryPoint;

                if (distanceFromQueryPointToFarthestPoint > distanceFromQueryPointToThreshold) {
                    this.far.collectNearestNeighbors(collector);
                }
            } else {
                // We've already searched the node that contains points beyond this node's threshold. We want to search
                // the within-threshold node if it's "easier" to get from the query point to this node's region than it
                // is to get from the query point to the most distant match, since there could be a point within this
                // node's threshold that's closer than the most distant match.
                final double distanceFromQueryPointToThreshold = distanceFromVantagePointToQueryPoint - this.threshold;

                if(distanceFromQueryPointToThreshold <= distanceFromQueryPointToFarthestPoint) {
                    this.near.collectNearestNeighbors(collector);
                }
            }
        } else {
            for (final P point : this.coordinates) {
                collector.offerPoint(point);
            }
        }
    }

    public ArrayList<P> getCoordinates() {
        return this.coordinates;
    }

    public LocationNode<P> getNear() {
        return this.near;
    }

    public LocationNode<P> getFar() {
        return this.far;
    }
}
