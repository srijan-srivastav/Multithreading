import java.util.concurrent.*;

public class Interrupts {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting.");

        ExecutorService executor = Executors.newCachedThreadPool();

        Future<?> fu = executor.submit(new Callable<Void>() {

            @Override
            public Void call() throws Exception {

                for (int i = 0; i < 1000000000; i++) {
                    System.out.println("something...");
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.printf("Interrupted at %d !!!", i);
                        break;
                    }
                }
                return null;
            }
        });

        /*
        in this example, there are different ways you can interrupt a thread
        execution.
         */
        Thread.sleep(10);

        //JavaDoc: http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html#cancel-boolean-
        fu.cancel(true);

        //JavaDoc: http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html#shutdownNow--
        executor.shutdownNow();

        executor.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Finished.");
    }

}