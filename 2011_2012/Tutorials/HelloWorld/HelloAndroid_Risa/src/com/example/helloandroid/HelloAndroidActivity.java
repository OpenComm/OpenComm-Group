package com.example.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/** HelloAndroidActivity
 * <p>Tutorial provided by Android Developers website to get used to
 * programming for Android</p>
 * @author Risa Naka (rn96)
 *
 */
public class HelloAndroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** A View is a drawable object used as an element in the UI Layout
        The subclass that handles text is TextView
        TextView tv = new TextView(this);
        tv.setText("Hello, Android");
        setContentView(tv);*/
        
        /** Debugger example: a null pointer exception
        Object o = null;
        o.toString();
         */
        setContentView(R.layout.main);
    }
}