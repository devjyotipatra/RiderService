package riderservice;


import riderservice.store.DriverLocationStore;
import riderservice.store.DriverLookupTable;
import riderservice.util.CoordDistanceFunction;
import riderservice.util.CoordNodePartitioner;
import riderservice.util.DriverCoordinate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 17349 on 24/01/17.
 */


public class BackgroundTaskManager implements ServletContextListener {

    private static final int MAXIMUM_CONCURRENT = 1;

    private static final int JOB_INTERVAL = 10;

    private ScheduledThreadPoolExecutor executor = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executor = new ScheduledThreadPoolExecutor(MAXIMUM_CONCURRENT);

        executor.scheduleAtFixedRate(new Runnable() {
                                         @Override
                                         public void run() {
                                             RiderAPIHandler handler = RiderAPIHandler.getHandlerInstance();
                                             DriverLookupTable lookupTable = handler.getLookupTable();

                                             List<DriverCoordinate> list = lookupTable.getDriverCoordList();

                                             System.out.println(list);

                                             DriverLocationStore<DriverCoordinate> store = new DriverLocationStore(list,
                                                                                            new CoordDistanceFunction(),
                                                                                            new CoordNodePartitioner(),
                                                                                            handler.getMaxCardinality());

                                             handler.setTreeRootNode(store);
                                         }
                                     },
                                JOB_INTERVAL,
                                JOB_INTERVAL,
                                TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executor.shutdown();
    }
}
