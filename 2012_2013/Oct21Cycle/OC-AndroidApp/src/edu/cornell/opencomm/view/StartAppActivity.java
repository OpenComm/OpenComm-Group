package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
/**
 * View for splash screen.
 * It launches the DashboardView after 3 seconds.
 *
 * Issues [TODO]
 * - For any other issues search for string "TODO"
 *
 * @author Kris Kooi[backend], Ankit Singh[frontend]
 * */
public class StartAppActivity extends Activity {
	/** 
	 * Debugging variable: if true, all logs are logged;
	 * set to false before packaging
	 */
	private static final boolean D = true;
	
	/**
	 * Logging tag
	 */
	private static final String TAG = StartAppActivity.class.getSimpleName();

	/**
	 * The delay for splash screen in milliseconds
	 */
	private static final int SPLASH_SCREEN_DELAY = 3000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_view_layout);
		FontSetter.applySanSerifFont(StartAppActivity.this,
				findViewById(R.id.splash_view_layout));
		// delay launch of LoginView
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (D) Log.d(TAG, "Splash timeout:Attempting Connection");
				launchLoginView();
			}
		}, SPLASH_SCREEN_DELAY);

	}
	/**
	 * Finishes the current activity and launches the Login Activity
	 */
	private void launchLoginView() {
		Intent intent = new Intent(StartAppActivity.this, LoginView.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		// back button disabled
	}
	
}
