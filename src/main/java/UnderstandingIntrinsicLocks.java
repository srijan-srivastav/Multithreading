import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnderstandingIntrinsicLocks {

    private static final Random random = new Random();
    private static final Object o1 = new Object();
    private static final Object o2 = new Object();

    private static final List<Integer> list1 = new ArrayList<>();
    private static final List<Integer> list2 = new ArrayList<>();

    /**
     * synchronized, methods use different data (list1 list2) so by synchronized
     * methods if one thread runs the stageOne other thread cannot run stageTwo
     * at the same time because that same locks are used. Solution is using two
     * lock Object for two shared data.
     */
    public static void stageOne() {
        synchronized (o1) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list1.add(random.nextInt(100));
        }
    }

    /**
     * The two methods are independent of each other. So, if we use the intrinsic lock of this class.
     * It won't be able run in parallel. As intrinsic lock applies on the object.
     *
     * We use the locks on different objects to fix this
     */
    public static synchronized void stageTwo() {
        synchronized (o2) {
            try {
                //do your work here
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list2.add(random.nextInt(100));
        }
    }

    public static void process() {
        for (int i = 0; i < 1000; i++) {
            stageOne();
            stageTwo();
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting ...");
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                process();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                process();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignored) {
        }

        long end = System.currentTimeMillis();

        System.out.println("Time taken: " + (end - start));
        System.out.println("List1: " + list1.size() + "; List2: " + list2.size());
    }
}