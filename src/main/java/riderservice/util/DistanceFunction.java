package riderservice.util;

import java.util.Comparator;

/**
 * Created by 17349 on 23/01/17.
 */
public interface DistanceFunction<P> {

    public double getDistance(P point1, P point2);


    public static class DistanceComparator<P> implements Comparator<P> {
        private final P origin;

        private final DistanceFunction<? super P> distanceFunction;

        public DistanceComparator(final P origin, final DistanceFunction<? super P> distanceFunction) {
            this.origin = origin;
            this.distanceFunction = distanceFunction;
        }

        public int compare(final P point1, final P point2) {
            return Double.compare(
                    this.distanceFunction.getDistance(this.origin, point1),
                    this.distanceFunction.getDistance(this.origin, point2));
        }
    }
}
