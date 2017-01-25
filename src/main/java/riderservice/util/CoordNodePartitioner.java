package riderservice.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by 17349 on 23/01/17.
 */


/*
 *   Quicksort based "fast" Partitioner.
 *   getPartitionPoint method will recursively partition the given det of coordinates into two sets; "near to the partitionPoint"
 *   and "far from the partitionPoint" around the pivot (partitionPoint) in O(N) time using the Partition step of
 *   QuickSort algorithm.
 *
 */
public class CoordNodePartitioner<Coordinate> implements NodePartitioner<Coordinate> {

    /**
     * Returns the median distance of the given points from the given origin. This method will also partially sort the list
     * of points in the process.
     *
     * @param coordinates the list of points from which a median distance will be chosen
     * @param partitionPoint the point from which distances to other points will be calculated
     * @param distanceFunction the function to be used to calculate the distance between the origin and other points
     *
     * @return the median distance from the origin to the given list of points
     *
     * @throws IllegalArgumentException if the given list of points is empty
     */

    public double getThreshold(final ArrayList<Coordinate> coordinates, final Coordinate partitionPoint,
                               final DistanceFunction<Coordinate> distanceFunction) {

        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinates list must not be empty.");
        }


        int left = 0;
        int right = coordinates.size() - 1;

        final int medianIndex = coordinates.size() / 2;
        final Random random = new Random();

        while (left != right) {
            final int pivotIndex = left + (right - left == 0 ? 0 : random.nextInt(right - left));
            final double pivotDistance = distanceFunction.getDistance(partitionPoint, coordinates.get(pivotIndex));

            Collections.swap(coordinates, pivotIndex, right);

            int storeIndex = left;

            for (int i = left; i < right; i++) {
                if (distanceFunction.getDistance(partitionPoint, coordinates.get(i)) < pivotDistance) {
                    Collections.swap(coordinates, storeIndex++, i);
                }
            }

            Collections.swap(coordinates, right, storeIndex);

            if (storeIndex == medianIndex) {
                break;
            } else if (storeIndex < medianIndex) {
                left = storeIndex + 1;
            } else {
                right = storeIndex - 1;
            }
        }

        return distanceFunction.getDistance(partitionPoint, coordinates.get(medianIndex));
    }
}
