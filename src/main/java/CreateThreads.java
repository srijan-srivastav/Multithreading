import java.util.Scanner;

/**
 * In Java threads can be created using 2 ways
 */
public class CreateThreads {
    public static void main(String[] args) {
        ThreadByExtension te = new ThreadByExtension();
        te.start();
        Scanner scn= new Scanner(System.in);
        scn.nextLine();
        te.shutdown();

        ThreadByImplementation ti = new ThreadByImplementation();
        Thread t = new Thread(ti);
        t.start();
        scn.nextLine();
        ti.shutdown();

    }
}

/**
 *  We can a piece of code inside the run method on a separate thread by extending the Thread class
 *  Just create an instance and call the method start in Thread class on it. Only then it will start
 *  on a separate thread.
 *
 *  Calling run will execute the code on the main thread.
 *
 *  Limitation of this technique is that we can only extend one class in Java
 */

class ThreadByExtension extends Thread {
    /** This has to be declared such that the thread is aware when the state of it changes*/
    private volatile boolean running = true;
    @Override
    public void run() {
        while(running) {
            System.out.println("Running!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        this.running = false;
    }
}

/**
 * This is instance needs to be passed on to a Thread class instance
 * On which .start() would be called, which would intern call the code inside run() on a separate thread
 *
 * Java allows implementation of multiple interfaces
 */
class ThreadByImplementation implements Runnable {
    private volatile boolean running = true;
    @Override
    public void run() {
        while(running)  {
            System.out.println("Running!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        this.running = false;
    }
}
