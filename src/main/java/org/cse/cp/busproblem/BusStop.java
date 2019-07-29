package org.cse.cp.busproblem;

import java.util.concurrent.Semaphore;

public class BusStop {

    private static Semaphore multiplex = new Semaphore(50);
    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore bus = new Semaphore(0);

    private static int riders = 0;

    public static void main(String[] args) {

        final int noOfBuses = Integer.valueOf(args[0]);
        final int noOfRiders = Integer.valueOf(args[1]);

        Thread busThread = new Thread(new Runnable() {
            public void run() {
                int busCount = 1;
                while (busCount <= noOfBuses) {
                    try {
                        Thread.sleep(arrival_time(20*60*10));
                        new Thread(new Bus(mutex, multiplex, bus, busCount)).start();
                        busCount++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread riderThread = new Thread(new Runnable() {
            public void run() {
                int ridersCount = 1;
                while(ridersCount <= noOfRiders) {
                    try {
                        Thread.sleep(arrival_time(30*10));
                        new Thread(new Rider(riders, multiplex, mutex, bus)).start();
                        ridersCount++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        riderThread.start();
        busThread.start();
    }

    public static long arrival_time(long mean_arrival_time) {

        double u = Math.random();
        return Math.round ((-mean_arrival_time)*Math.log(1-u));
    }

    public static int getRiders() {

        return riders;
    }

    public static void incrementRider() {

        riders += 1;
    }

    public static void setRiders(int riders) {

        BusStop.riders = riders;
    }
}
