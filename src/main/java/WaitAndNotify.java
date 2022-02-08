/**
 * Let's break down what's going on here:
 *
 * The packet variable denotes the data that is being transferred over the network.
 * We have a boolean variable transfer, which the Sender and Receiver will use for synchronization:
 * If this variable is true, the Receiver should wait for Sender to send the message.
 * If it's false, Sender should wait for Receiver to receive the message.
 * The Sender uses the send() method to send data to the Receiver:
 * If transfer is false, we'll wait by calling wait() on this thread.
 * But when it is true, we toggle the status, set our message, and call notifyAll() to wake up other threads to specify that a significant event has occurred and they can check if they can continue execution.
 * Similarly, the Receiver will use the receive() method:
 * If the transfer was set to false by Sender, only then will it proceed, otherwise we'll call wait() on this thread.
 * When the condition is met, we toggle the status, notify all waiting threads to wake up, and return the data packet that was received.
 *
 * Why Enclose wait() in a while Loop?
 * Since notify() and notifyAll() randomly wake up threads that are waiting on this object's monitor, it's not always important that the condition is met. Sometimes the thread is woken up, but the condition isn't actually satisfied yet.
 *
 * We can also define a check to save us from spurious wakeups — where a thread can wake up from waiting without ever having received a notification.
 *
 * Why Do We Need to Synchronize send() and receive() Methods?
 * We placed these methods inside synchronized methods to provide intrinsic locks. If a thread calling wait() method does not own the inherent lock, an error will be thrown.
 */

public class WaitAndNotify {
    private String packet;

    // True if receiver should wait
    // False if sender should wait
    private boolean transfer = true;

    public synchronized String receive() {
        while (transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread Interrupted");
            }
        }
        transfer = true;

        String returnPacket = packet;
        notifyAll();
        return returnPacket;
    }

    public synchronized void send(String packet) {
        while (!transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread Interrupted");
            }
        }
        transfer = false;

        this.packet = packet;
        notifyAll();
    }
}

