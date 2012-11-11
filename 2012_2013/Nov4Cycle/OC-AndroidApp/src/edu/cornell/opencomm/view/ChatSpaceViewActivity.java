package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import edu.cornell.opencomm.R;

public class ChatSpaceViewActivity extends Activity {


	private ChatSpaceViewGroup csvgroup; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		csvgroup = new ChatSpaceViewGroup(this, 0); 
		setContentView(R.layout.activity_main);
	}



	//TODO- Add functionality - on add pressed in the action bar 
	//For now when the add button is clicked 
	public void addClicked(View v){ 		
		csvgroup = new ChatSpaceViewGroup(this, 1); 
	}

	public void removeClicked(View v){
		csvgroup= new ChatSpaceViewGroup(this, -1); 
	}

	public void contactPressed(View v){

	}

	public void whenViewPressed(View v){

	}

}
