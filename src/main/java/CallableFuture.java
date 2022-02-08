import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CallableFuture {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> results = new ArrayList<>();
        for(int i = 0 ; i < 15; i++) {
            Future<Integer> res = executorService.submit(new CallableWork());
            results.add(res.get());
        }
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
        System.out.println(results);
    }
}

/**
 * Implement work as callable if you want the result from thread execution inside a future
 */
class CallableWork implements Callable<Integer> {

    Random random = new Random();
    @Override
    public Integer call() throws Exception {
        int duration = random.nextInt(2000);
        Thread.sleep(duration);
        return duration;
    }
}
