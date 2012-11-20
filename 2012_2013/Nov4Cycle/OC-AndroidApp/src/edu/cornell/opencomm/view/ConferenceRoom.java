package edu.cornell.opencomm.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import edu.cornell.opencomm.R;

public class ConferenceRoom extends Fragment {
	View roomLayout;
	public int layoutId = R.layout.confernec_main_room;
	private static String TAG = ConferenceRoom.class.getName();
	/** (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			//ANKIT: i dont know what this means yet
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		if(savedInstanceState != null){
		 layoutId = savedInstanceState.getInt("LAYOUT");
		}
		else{
			Log.d(TAG, "Bundle is null :(");
		}
		return inflater.inflate(layoutId, container, false);
	}
}
