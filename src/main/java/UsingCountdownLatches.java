import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Refer to documentation from CountdownLatch class
 */
public class UsingCountdownLatches {
    private static final CountDownLatch waitLatch = new CountDownLatch(1);
    private static final CountDownLatch workLatch = new CountDownLatch(10000);
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final AtomicInteger counter = new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {
        Worker work = new Worker();
        for(int i = 0 ; i < 10000; i++) {
            executor.submit(work);
        }
        waitLatch.countDown();
        workLatch.await();
        System.out.println("Work completed");
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            try {
                waitLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.incrementAndGet();
            System.out.println("Doing some work..." + counter.get());
            workLatch.countDown();
        }
    }
}
