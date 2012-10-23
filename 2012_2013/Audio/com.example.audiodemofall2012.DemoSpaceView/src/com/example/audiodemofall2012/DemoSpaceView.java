package com.example.audiodemofall2012;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class DemoSpaceView extends Activity {

	private static final String TAG = "DemoSpaceView";
	private FrameLayout space;
	private SoundController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_screen);
		space = (FrameLayout) findViewById(R.id.spaceViewFrame);
		controller = new SoundController();
		
		space.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int touchX, touchY;

				touchX = (int) arg1.getX();
				touchY = (int) arg1.getY();
				Log.v(TAG, "touched screen");

				controller.manipulateSource(touchX, touchY);

				Log.v(TAG, "played sound");
				return false;
			}
		});
	}
}
