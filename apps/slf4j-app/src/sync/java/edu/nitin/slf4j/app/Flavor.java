package edu.nitin.slf4j.app;

import android.util.Log;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nitin.verma on 11/13/15.
 */
public class Flavor {
    private static final String TAG = "SLF4J-SYNC";

    public static void run(final MainActivity mainActivity) {
        try {
            run0(mainActivity);
        } catch (final Throwable throwable) {
            Log.e(TAG, "Failed", throwable);
        }
    }

    private static void run0(final MainActivity mainActivity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method method = mainActivity.getFindPossibleStaticLoggerBinderPathSetMethod();
        test(mainActivity, method);
    }

    public static void test(final MainActivity mainActivity, final Method method) throws InvocationTargetException, IllegalAccessException {
        final StringBuilder msg = new StringBuilder();
        try {
            int count = 10;
            long sum = 0;
            for (int i = 0; i < count; i++) {
                final long time = timeFindBindingSetCall(method);
                msg.append(time).append(" nanoseconds\n");
                sum += time;
            }
            msg.append(sum / count).append(" nanoseconds in average\n");
        } finally {
            mainActivity.overRideTextView(msg.toString(), "#000000", true);
            Log.i(TAG, msg.toString());
        }
    }

    private static long timeFindBindingSetCall(final Method method) throws InvocationTargetException, IllegalAccessException {
        long start = System.nanoTime();
        method.invoke(LoggerFactory.class, (Object[]) null);
        long end = System.nanoTime();
        return end - start;

    }
}
