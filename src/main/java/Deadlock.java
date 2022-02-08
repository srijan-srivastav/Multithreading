import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {
    public static void main(String[] args) throws InterruptedException {
        Transactions t = new Transactions();
        Thread t1 = new Thread(() -> {
            try {
                t.worker1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                t.worker2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        t.finalBalances();
    }
}

class BankAccount {
    BigDecimal amount = new BigDecimal(10000);
    public static void transfer(BankAccount b1, BankAccount b2, BigDecimal amount) {
        b1.amount = b1.amount.subtract(amount);
        b2.amount = b2.amount.add(amount);
    }
}

/**
 * If you are using locks then make sure they are always locked and unlocked in the same order
 * Otherwise, it might lead to deadlocks.
 *
 * In the example below if you change the order of taking the locks, it will lead to deadlock
 * it happens because Thread1 takes lock1 and simultaneously Thread2 takes lock2 and now both
 * are waiting on each other.
 *
 * Always remember to release lock in a finally block as because of some exception we might
 * never release te lock
 *
 * Refer to the method acquireLock below to understand how to deal with it. (add unlocks in finally for production level code)
 */
class Transactions {
    BankAccount b1 = new BankAccount();
    BankAccount b2 = new BankAccount();
    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    private void acquireLock() throws InterruptedException {
        boolean acquiredLock1 = false;
        boolean acquiredLock2 = false;
        while(true) {
            acquiredLock1 = lock1.tryLock(10, TimeUnit.MILLISECONDS);
            acquiredLock2 = lock2.tryLock(10, TimeUnit.MILLISECONDS);
            if(acquiredLock1 && acquiredLock2) {
                return;
            }
            if(acquiredLock1) {
                lock1.unlock();
            }
            if(acquiredLock2) {
                lock2.unlock();
            }

        }
    }


    public void worker1() throws InterruptedException {
        Random random = new Random();
        for(int i = 0 ; i < 10000; i++) {
//            lock1.lock();
//            lock2.lock();
            acquireLock();
            BankAccount.transfer(b1, b2, BigDecimal.valueOf(random.nextDouble()));
            lock1.unlock();
            lock2.unlock();
        }
    }

    public void worker2() throws InterruptedException {
        Random random = new Random();
        for(int i = 0 ; i < 10000; i++) {
//            lock1.lock(); // if you change the order it will lead to deadlock
//            lock2.lock();
            acquireLock();
            BankAccount.transfer(b2, b1, BigDecimal.valueOf(random.nextDouble()));
            lock1.unlock();
            lock2.unlock();
        }
    }

    public void finalBalances() {
        System.out.println("Acc1: " + b1.amount);
        System.out.println("Acc2: " + b2.amount);
        System.out.println("Final Amount: " + b1.amount.add(b2.amount));
    }
}
