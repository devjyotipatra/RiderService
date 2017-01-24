package riderservice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by 17349 on 24/01/17.
 */
public class Scheduler {

    private static Scheduler instance;

    private static int periodInSeconds = 10;

    private final ScheduledExecutorService executor;
    private final List<Runnable> runnables;

    private ScheduledFuture<?> future;


    private Scheduler(ScheduledExecutorService s) {
        executor = s;
        runnables = new ArrayList<Runnable>();
    }

    public static Scheduler getInstance() {
        if(instance == null) {
            instance = new Scheduler(Executors.newScheduledThreadPool(2));
        }

        return instance;
    }

    public void add(Runnable runnable) {
        runnables.add(runnable);
    }



    public boolean isStarted() {
        return future != null && !future.isCancelled() && !future.isDone();
    }

    public synchronized void start() {
        if (!isStarted()) {
            long untilNextPeriod = 10;
            future = executor.scheduleAtFixedRate(new Runnable() {
                //@Override
                public void run() {
                    System.out.println("Starting Scheduler..");
                    Scheduler.this.run();
                }
            }, untilNextPeriod, periodInSeconds, TimeUnit.SECONDS);

        }
    }



    public void run() {
        for (Runnable runnable : runnables) {
            executor.submit(runnable);
        }
    }

    public synchronized void stop() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }
}

