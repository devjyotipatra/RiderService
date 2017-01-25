package riderservice.util;

/**
 * Created by 17349 on 24/01/17.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A utility class that uses a priority queue to efficiently collect results for k nearestneighbors
 */
public class NearestNeighborFinder<P> {
    private final P queryPoint;
    private final double radius;
    private final int capacity;

    private final DistanceFunction<P> distanceFunction;
    private final DistanceFunction.DistanceComparator<P> distanceComparator;
    private final PriorityQueue<P> priorityQueue;

    private double distanceToFarthestPoint;

    public NearestNeighborFinder(final P queryPoint, final DistanceFunction<P> distanceFunction, final double radius, final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }

        this.queryPoint = queryPoint;
        this.distanceFunction = distanceFunction;
        this.radius = radius;
        this.capacity = capacity;

        this.distanceComparator = new DistanceFunction.DistanceComparator<>(queryPoint, distanceFunction);

        this.priorityQueue =
                new PriorityQueue<>(this.capacity, java.util.Collections.reverseOrder(this.distanceComparator));
    }

    public P getQueryPoint() {
        return this.queryPoint;
    }

    public void offerPoint(final P point) {
        final boolean pointAdded;

        if (this.priorityQueue.size() < this.capacity) {
            this.priorityQueue.add(point);
            pointAdded = true;
        } else {
            assert this.priorityQueue.size() > 0;

            final double distanceToNewPoint = this.distanceFunction.getDistance(this.queryPoint, point);

            if (distanceToNewPoint < this.distanceToFarthestPoint && distanceToNewPoint <= this.radius) {
                this.priorityQueue.poll();
                this.priorityQueue.add(point);
                pointAdded = true;
            } else {
                pointAdded = false;
            }
        }

        if (pointAdded) {
            this.distanceToFarthestPoint = this.distanceFunction.getDistance(this.queryPoint, this.priorityQueue.peek());
        }
    }

    public P getFarthestPoint() {
        return this.priorityQueue.peek();
    }

    public List<P> toSortedList() {
        final ArrayList<P> sortedList = new ArrayList<>(this.priorityQueue);
        java.util.Collections.sort(sortedList, this.distanceComparator);

        return sortedList;
    }
}
