package edu.cornell.opencomm.view;
import edu.cornell.opencomm.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
 
public class WelcomeView extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.welcome);
	      Thread welcomeThread = new Thread() {
	         @Override
	         public void run() {
	            try {
	               int waited = 0;
	               while (waited < 5000) {
	                  sleep(100);
	                  waited += 100;
	               }
	            } catch (InterruptedException e) {
	               // do nothing
	            } finally {
	               finish();
	               Intent intent = new Intent();
	               intent = new Intent(getApplicationContext(), LoginView.class);
	               startActivity( intent ); 
	            }
	         }
	      };
	      welcomeThread.start();
	   }
}