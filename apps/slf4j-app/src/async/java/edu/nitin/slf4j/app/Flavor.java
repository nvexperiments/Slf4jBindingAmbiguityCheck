package edu.nitin.slf4j.app;

import android.util.Log;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by nitin.verma on 11/13/15.
 */
public class Flavor {
    private static final String TAG = "SLF4J-ASYNC";

    public static void run(final MainActivity mainActivity) {
        try {
            run0(mainActivity);
        } catch (final Throwable throwable) {
            Log.e(TAG, "Failed", throwable);
        }
    }

    private static void run0(final MainActivity mainActivity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {
        final Method method = mainActivity.getFindPossibleStaticLoggerBinderPathSetMethod();
        testAsync(mainActivity, method);
    }

    public static void testAsync(final MainActivity mainActivity, final Method method) throws InterruptedException {
        final StringBuilder msg = new StringBuilder();
        try {
            long start = System.nanoTime();
            FindPathSetThread thread = new FindPathSetThread(method);
            thread.start();
            long end = System.nanoTime();

            long duration = end - start;
            msg.append(duration).append(" nanoseconds\n");
            msg.append(thread.getMsg()).append("\n");
        } finally {
            mainActivity.overRideTextView(msg.toString(), "#000000", true);
            Log.i(TAG, msg.toString());
        }

    }

    private static class FindPathSetThread extends Thread {
        private final Method method;
        private final Object lock = new Object();
        private String msg;

        public FindPathSetThread(final Method method) {
            this.method = method;
        }

        public void run() {
            try {
                run0();
            } catch (final Throwable throwable) {
                Log.e(TAG, "Failed", throwable);
            }
        }

        public void run0() throws InvocationTargetException, IllegalAccessException {
            long start = System.nanoTime();
            method.invoke(LoggerFactory.class, (Object[]) null);
            long end = System.nanoTime();

            setMsg("Found set in " + (end - start) + " nanoseconds");
        }

        private void setMsg(final String msg) {
            synchronized (lock) {
                if (this.msg == null) {
                    this.msg = msg;
                    lock.notifyAll();
                } else {
                    Log.w(TAG, "Msg can not be set again and again");
                }
            }
        }

        public String getMsg() throws InterruptedException {
            synchronized (lock) {
                if (msg == null) {
                    lock.wait(TimeUnit.MINUTES.toMillis(1));
                }
                if (msg == null) {
                    Log.w(TAG, "Msg didn't get set for all the time I waited");
                }
                return msg;
            }
        }
    }
}
