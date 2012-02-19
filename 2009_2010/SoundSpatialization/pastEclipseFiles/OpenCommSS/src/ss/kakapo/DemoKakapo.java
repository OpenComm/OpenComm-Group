package ss.kakapo;

import java.io.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.widget.*;

/** Demonstrates the ability to switch between private space mode and full conference mode with 8 sounds */
public class DemoKakapo extends Activity {
	/** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demomulti);
		// set toggle buttons
		final ToggleButton rOne = (ToggleButton) findViewById(R.id.one);
		final ToggleButton rThree = (ToggleButton) findViewById(R.id.three);
		final ToggleButton rFive = (ToggleButton) findViewById(R.id.five);
		final ToggleButton rSixteen = (ToggleButton) findViewById(R.id.sixteen);
		final ToggleButton rEighteen = (ToggleButton) findViewById(R.id.eighteen);
		final ToggleButton rTwenty = (ToggleButton) findViewById(R.id.twenty);
		Log.i("DemoKakapo", "ToggleButtons set");
		
		// create SS manipulation for each sound
		final AudioSS aOne = new AudioSS(80, 60); // region 1
		final AudioSS aThree = new AudioSS(400, 60); // region 3
		final AudioSS aFive = new AudioSS(720, 60); // region 5
		final AudioSS aSixteen = new AudioSS(80, 420); // region 16
		final AudioSS aEighteen = new AudioSS(400, 420); // region 18
		final AudioSS aTwenty = new AudioSS(720, 420); // region 20
		
		Log.i("DemoKakapo", "AudioSS set");
		
		// gain access to each sound source
		InputStream sOne = getResources().openRawResource(R.raw.lowc);
		try {
			byte[] dOne = new byte[sOne.available()];
			sOne.read(dOne, 0, dOne.length);
			sOne.close();
		}
		catch (IOException e) {
			Log.e("DemoKakapo", "lowc.raw not found");
		}
		InputStream sThree = getResources().openRawResource(R.raw.lowe);
		try {
			byte[] dThree = new byte[sThree.available()]; 
			sThree.read(dThree, 0, dThree.length);
			sThree.close();
		}
		catch (IOException e) {
			Log.e("DemoKakapo", "lowe.raw not found");
		}
		InputStream sFive = getResources().openRawResource(R.raw.lowg);
		try {
			byte[] dFive = new byte[sFive.available()];
			sFive.read(dFive, 0, dFive.length);
			sFive.close();
		}
		catch (IOException e) {
			Log.e("DemoKakapo", "lowg.raw not found");
		}
		InputStream sSixteen = getResources().openRawResource(R.raw.highc);
		try {
			byte[] dSixteen = new byte[sSixteen.available()];
			sSixteen.read(dSixteen, 0, dSixteen.length);
			sSixteen.close();
		}
		catch (IOException e) {
			Log.e("DemoKakapo", "highc.raw not found");
		}
		InputStream sEighteen = getResources().openRawResource(R.raw.highe);
		try {
			byte[] dEighteen = new byte[sEighteen.available()];
			sEighteen.read(dEighteen, 0, dEighteen.length);
			sEighteen.close();
		}
		catch (IOException e) {
			Log.e("DemoKakapo", "highe.raw not found");
		}
		InputStream sTwenty = getResources().openRawResource(R.raw.highg);
		try {
			byte[] dTwenty = new byte[sTwenty.available()];
			sTwenty.read(dTwenty, 0, dTwenty.length);
			sTwenty.close();
		}
		catch (IOException e) {
			Log.e("DemoKakapo", "highg.raw not found");
		}

		// play and write each sound source
		aOne.play();
		aThree.play();
		aFive.play();
		aSixteen.play();
		aEighteen.play();
		aTwenty.play();

		// set click listener
		rOne.setOnClickListener(new OnClickListener() {
			// if the region is marked as part of the private space
			public void onClick(View v) {
				// in private space
				if (rOne.isChecked()) {
					aOne.setStereoVolume(aOne.getVolume()[0], aOne.getVolume()[1]);
				}
				else {
					aOne.setStereoVolume(aOne.getVolume()[0]/2, aOne.getVolume()[1]/2);
				}
			} // end onClick method
		});
		
		rThree.setOnClickListener(new OnClickListener() {
			// if the region is marked as part of the private space
			public void onClick(View v) {
				// in private space
				if (rThree.isChecked()) {
					aThree.setStereoVolume(aThree.getVolume()[0], aThree.getVolume()[1]);
				}
				else {
					aThree.setStereoVolume(aThree.getVolume()[0]/2, aThree.getVolume()[1]/2);
				}
			} // end onClick method
		});
		
		rFive.setOnClickListener(new OnClickListener() {
			// if the region is marked as part of the private space
			public void onClick(View v) {
				// in private space
				if (rFive.isChecked()) {
					aFive.setStereoVolume(aFive.getVolume()[0], aFive.getVolume()[1]);
				}
				else {
					aFive.setStereoVolume(aFive.getVolume()[0]/2, aFive.getVolume()[1]/2);
				}
			} // end onClick method
		});
		
		rSixteen.setOnClickListener(new OnClickListener() {
			// if the region is marked as part of the private space
			public void onClick(View v) {
				// in private space
				if (rSixteen.isChecked()) {
					aSixteen.setStereoVolume(aSixteen.getVolume()[0], aSixteen.getVolume()[1]);
				}
				else {
					aSixteen.setStereoVolume(aSixteen.getVolume()[0]/2, aSixteen.getVolume()[1]/2);
				}
			} // end onClick method
		});

		rEighteen.setOnClickListener(new OnClickListener() {
			// if the region is marked as part of the private space
			public void onClick(View v) {
				// in private space
				if (rEighteen.isChecked()) {
					aEighteen.setStereoVolume(aEighteen.getVolume()[0], aEighteen.getVolume()[1]);
				}
				else {
					aEighteen.setStereoVolume(aEighteen.getVolume()[0]/2, aEighteen.getVolume()[1]/2);
				}
			} // end onClick method
		});

		rTwenty.setOnClickListener(new OnClickListener() {
			// if the region is marked as part of the private space
			public void onClick(View v) {
				// in private space
				if (rTwenty.isChecked()) {
					aTwenty.setStereoVolume(aTwenty.getVolume()[0], aTwenty.getVolume()[1]);
				}
				else {
					aTwenty.setStereoVolume(aTwenty.getVolume()[0]/2, aTwenty.getVolume()[1]/2);
				}
			} // end onClick method
		});
    } // end onCreate method
} // end class DemoKakapo