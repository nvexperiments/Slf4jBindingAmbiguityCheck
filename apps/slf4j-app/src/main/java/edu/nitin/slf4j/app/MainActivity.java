package edu.nitin.slf4j.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Flavor.run(MainActivity.this);
                return null;
            }
        }.execute();
    }

    public void updateTextView(final String text) {
        if (textView != null) {
            if ( Looper.getMainLooper().getThread() == Thread.currentThread() ) {
                synchronized (lock) {
                    textView.setText(text);
                    root.invalidate();
                }
            }
            else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (lock) {
                            textView.setText(text);
                            root.invalidate();
                        }
                    }
                });
            }
        }
    }

}
