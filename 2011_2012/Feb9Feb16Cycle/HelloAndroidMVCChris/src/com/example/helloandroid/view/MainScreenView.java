package com.example.helloandroid.view;

import com.example.helloandroid.R;
import com.example.helloandroid.controller.MainScreenController;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.	Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;


public class MainScreenView extends Activity{
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private MainScreenController mainScreenController = new MainScreenController(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MainScreenView","tried to create!" );
		setContentView(R.layout.main);
		//inflater = this.getLayoutInflater();
		initEventsAndProperties();
		mainScreenController= new MainScreenController(this);}
	
	public MainScreenView() {
		
	}
	
	private void initEventsAndProperties() {
		// create property adminTipLayout from infalter and store it as a
		// property
		initializeMainScreenButtonClickedEvent();

	}
	
	private void initializeMainScreenButtonClickedEvent() {
		Button goButton = getGoButton();
		Log.d("initialize", "goButton trying.");
		if (goButton != null) {
			Log.d("initialize", "goButton");
			goButton.setOnClickListener(onGoClickListener);
		}
	}
	
	
	public Button getGoButton() {
		Button goButton = null;
			goButton = (Button)findViewById(R.id.button1);
		return goButton;
	}

	private View.OnClickListener onGoClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d("GOBUTTON","Go button clicked..");
			mainScreenController.handleGoButtonClicked();
		}
	};

	public Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}
	
}
