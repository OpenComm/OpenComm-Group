package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
public class SplashView extends Activity {
	/** 
	 * Debugging variable: if true, all logs are logged;
	 * set to false before packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;
	
	/**
	 * Logging tag
	 */
	@SuppressWarnings("unused")
	private static final String TAG = SplashView.class.getSimpleName();

	/**
	 * The delay for splash screen in milliseconds
	 */
	private static final int SPLASH_SCREEN_DELAY = 3000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		FontSetter.applySanSerifFont(SplashView.this,findViewById(R.id.splash_layout));
		// delay launch of LoginView
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				launchLoginView();
			}
		}, SPLASH_SCREEN_DELAY);

	}
	/**
	 * Finishes the current activity and launches the Login Activity
	 */
	private void launchLoginView() {
		Intent intent = new Intent(SplashView.this, LoginView.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		// back button disabled
	}
	
}
