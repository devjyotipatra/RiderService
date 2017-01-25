package riderservice.store;

import riderservice.util.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 17349 on 23/01/17.
 */
public class DriverLocationStore<P> {

    private final DistanceFunction<P> distanceFunction;
    private NodePartitioner<P> partitioner;
    private final int nodeCardinality;

    private LocationNode<P> rootNode;

    public static final int DEFAULT_NODE_CARDINALITY = 32;


    public DriverLocationStore(final Collection<P> coordinates, final DistanceFunction<P> distanceFunction,
                               final NodePartitioner<P> partitioner, final int nodeCardinality) {
        this.distanceFunction = distanceFunction;
        this.partitioner = partitioner;
        this.nodeCardinality = nodeCardinality;

        if (coordinates != null && !coordinates.isEmpty()) {
            this.rootNode = new LocationNode<P>(
                    coordinates,
                    this.distanceFunction,
                    this.partitioner,
                    this.nodeCardinality);

            this.rootNode.partition();
        }
    }


    public List<P> getNearestNeighbors(final P queryPoint, final double radius, final int maxResults) {
        final List<P> nearestNeighbors;

        if (this.rootNode == null) {
            nearestNeighbors = null;
        } else {
            final NearestNeighborFinder<P> collector =
                    new NearestNeighborFinder<>(queryPoint, this.distanceFunction, radius, maxResults);

            this.rootNode.collectNearestNeighbors(collector);

            nearestNeighbors = collector.toSortedList();
        }

        return nearestNeighbors;
    }


    public void setRootNode(LocationNode<P> rootNode) {
        this.rootNode = rootNode;
    }


    public LocationNode<P> getRootNode() {
        return rootNode;
    }


    public void printTree(LocationNode node) {
        if(node!=null) {
            ArrayList<DriverCoordinate> coord = node.getCoordinates();
            System.out.println(coord);
            printTree(node.getNear());
            printTree(node.getFar());
        }
    }


    public static void main(String[] args) {
        ArrayList<DriverCoordinate> list =  new ArrayList<DriverCoordinate>();
        list.add(new DriverCoordinate(1, 12.914658, 77.593192));
        list.add(new DriverCoordinate(2, 12.918004, 77.599286));
        list.add(new DriverCoordinate(3, 12.914407, 77.599629));
        list.add(new DriverCoordinate(4, 12.909303, 77.594822));
        list.add(new DriverCoordinate(5, 12.908885, 77.607525));
        list.add(new DriverCoordinate(6, 12.894997, 77.598771));
        list.add(new DriverCoordinate(7, 12.894160, 77.609070));
        list.add(new DriverCoordinate(8, 12.890088, 77.599734));
        list.add(new DriverCoordinate(9, 12.883960, 77.589371));

        DriverLocationStore ls = new DriverLocationStore(list, new CoordDistanceFunction(),
                new CoordNodePartitioner(), 6);

        ls.printTree(ls.rootNode);

        System.out.println(ls.getNearestNeighbors(new DriverCoordinate(-1, 12.894160, 77.609070), 0.5, 4));
    }

}
