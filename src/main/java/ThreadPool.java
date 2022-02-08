import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Instead of creating and running Threads one by one, we can create an executor service.
 * It keeps a pool of worker threads, that will execute the tasks submitted to it.
 */
public class ThreadPool {
    /** Creates Threadpool of 10 threads */
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    public static void main(String[] args) throws InterruptedException {
        Task t = new Task();
        /** Submit task to the executor */
        for(int i = 0 ; i < 10000; i++) {
            executor.submit(t);
        }
        /** This runs on main thread and will be printed instantly */
        System.out.println("Tasks submitted");
        long start = System.currentTimeMillis();
        /** Stop accepting anymore tasks */
        executor.shutdown();
        /** Wait until all tasks are finished */
        executor.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();
        System.out.println("Time taken:" + (end-start));
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread executing...");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
