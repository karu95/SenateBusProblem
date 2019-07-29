package org.cse.cp.busproblem;

import java.util.concurrent.Semaphore;

public class Bus implements Runnable {

    private String id;
    private Semaphore bus;
    private Semaphore mutex;
    private Semaphore multiplex;

    public Bus(Semaphore mutex, Semaphore multiplex, Semaphore bus, int id) {

        this.mutex = mutex;
        this.multiplex = multiplex;
        this.bus = bus;
        this.id = String.valueOf(id);
    }

    public void run() {

        System.out.println("--- Bus " + id +" arrived. ---");
        try {
            boardBus();
        } catch (InterruptedException e) {
            System.out.println("Error occurred while boarding the bus.");
        }
    }

    private void boardBus() throws InterruptedException {

        mutex.acquire();
        if (BusStop.getRiders() == 0) {
            System.out.println("--- No passengers boarded to bus " + id + ". ---");
            depart();
        } else {
            if (BusStop.getRiders() > 50) {
                for (int i = 1; i <= 50; i++) {
                    bus.release();
                    multiplex.release();
                }
                System.out.println("--- " + 50 + " boarded to bus " + id + ". ---");
                BusStop.setRiders(BusStop.getRiders() - 50);
            } else {
                for (int i = 1; i <= BusStop.getRiders(); i++) {
                    bus.release();
                    multiplex.release();
                }
                System.out.println("--- " + BusStop.getRiders() + " boarded to bus " + id + ". ---");
                BusStop.setRiders(0);
            }
            depart();
        }
        mutex.release();
    }

    public void depart() {

        System.out.println("--- Bus " +id +" departed. ---");
    }

}
