package org.cse.cp.busproblem;

import java.util.concurrent.Semaphore;

public class Rider implements Runnable {

    private Semaphore multiplex;
    private Semaphore mutex;
    private Semaphore bus;

    public Rider(int riders, Semaphore multiplex, Semaphore mutex, Semaphore bus) {

        this.multiplex = multiplex;
        this.mutex = mutex;
        this.bus = bus;
    }

    public void run() {

        try {
            arrive();
        } catch (InterruptedException e) {
            System.out.println("Error occurred during pasenger arrival.");
        }
    }

    private void arrive() throws InterruptedException {

        mutex.acquire();
        BusStop.incrementRider();
        System.out.println("--- " + BusStop.getRiders() + " riders waiting for a bus. ---");
        mutex.release();

        multiplex.acquire();
        bus.acquire();
    }
}
