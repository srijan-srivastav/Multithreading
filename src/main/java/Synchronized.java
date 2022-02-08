import java.util.concurrent.atomic.AtomicInteger;

/**
 * Here we try to deal with the problem of access control in a multithreaded environment.
 *
 * If we increment a counter to 20000 by using two threads, it won't give guaranteed 20000
 * because addition is not an atomic operation. There are 3 steps atleast, Read, Update, Write.
 *
 * Threads can read value of the counter in intermediate stages.
 *
 * Solutions
 * 1. Make it an Atomic Integer (Atomic operation + volatile)
 * 2. Make the increment a Synchronized function (Mutual Exclusion, Only one thread will be allowed)
 */
public class Synchronized {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static int syncCounter = 0;

    private static synchronized void increment() {
        syncCounter++;
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 10000; i++) {
                counter.incrementAndGet();
                increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0 ; i < 10000; i++) {
                counter.incrementAndGet();
                increment();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(counter);
        System.out.println(syncCounter);
    }
}

