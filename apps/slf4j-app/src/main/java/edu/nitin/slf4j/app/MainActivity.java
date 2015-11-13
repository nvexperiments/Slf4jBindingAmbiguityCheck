package edu.nitin.slf4j.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by nitin.verma on 11/13/15.
 */
public class MainActivity extends Activity {

    private TextView textView;
    private LinearLayout root;
    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.text);
        root = (LinearLayout) findViewById(R.id.rootContainer);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Flavor.run(MainActivity.this);
                return null;
            }
        }.execute();
    }

    public Method getFindPossibleStaticLoggerBinderPathSetMethod() throws NoSuchMethodException {
        final Method method = LoggerFactory.class.getDeclaredMethod("findPossibleStaticLoggerBinderPathSet", (Class<?>[]) null);
        method.setAccessible(true);
        return method;
    }

    public void overRideTextView(final String text, final String color, final boolean selectable) {
        if (textView != null) {
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                overRideTextView0(text, color, selectable);
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        overRideTextView0(text, color, selectable);
                    }
                });
            }
        }
    }

    private void overRideTextView0(final String text, final String color, final boolean selectable) {
        synchronized (lock) {
            textView.setText(new SpannableString(text));
            textView.setTextColor(Color.parseColor(color));
            textView.setTextIsSelectable(selectable);
            root.invalidate();
        }
    }

}
