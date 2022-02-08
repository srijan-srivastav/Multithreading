import java.util.Random;
import java.util.concurrent.*;

/**
 * Message queue implementation
 * 1. Threadpool of producers
 * 2. Threadpool of consumers
 * 3. Thread safe Blocking queue (multiple threads can modify the data structure) where visibility and access control are taken care of
 * 4. Countdown latch to control when the threads should get to work
 *
 * Producers add data in the queue, Consumers poll data from the queue
 * Producers wait if queue has reached capacity
 * Consumers wait if queue is empty
 */
public class ProducerConsumer {
    private static final ExecutorService producers = Executors.newFixedThreadPool(10);
    private static final ExecutorService consumers = Executors.newFixedThreadPool(10);
    private static final CountDownLatch initialize = new CountDownLatch(0);
    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(100);
    private static final Random random = new Random();

    private static void produce() throws InterruptedException {
        while(true) {
            Thread.sleep(1000);
            System.out.println("Queue size: " + queue.size());
            queue.put(random.nextInt(100));
        }
    }

    private static void consume() throws InterruptedException {
        while(true) {
            Thread.sleep(1000);
            Integer val = queue.take();
            System.out.println("polling..." + val);
        }
    }

    public static void main(String[] args) {
        ProducerWorker pw = new ProducerWorker();
        ConsumerWorker cw = new ConsumerWorker();
        for(int i = 0 ; i < 10; i++) {
            producers.submit(pw);
            consumers.submit(cw);
        }
        initialize.countDown();
    }

    static class ProducerWorker implements Runnable {
        @Override
        public void run() {
            try {
                initialize.await();
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ConsumerWorker implements Runnable {
        @Override
        public void run() {
            try {
                initialize.await();
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
