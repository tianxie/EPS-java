package tf_29_actors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

abstract class ActiveWFObject extends Thread {
    protected String name;
    protected boolean stopMe;
    private BlockingQueue<Object[]> queue;

    public ActiveWFObject() {
        this.name = getClass().getSimpleName();
        this.stopMe = false;
        this.queue = new LinkedBlockingQueue<>();
        this.start();
    }

    @Override
    public void run() {
        while (!stopMe) {
            try {
                Object[] message = queue.take();
                dispatch(message);
                if ("die".equals(message[0])) {
                    stopMe = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    protected abstract void dispatch(Object[] message);

    public static void send(ActiveWFObject receiver, Object[] message) {
        try {
            receiver.queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
