package com.example.helloandroid.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.helloandroid.R;
import com.example.helloandroid.controller.MainScreenController;
import com.example.helloandroid.controller.Screen2Controller;


public class Screen2View extends Activity{
	

	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private Screen2Controller screen2Controller = new Screen2Controller(this);
	private View screen2Layout = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Test","Are you calling me?");
		setContentView(R.layout.screen2);
		//initEventsAndProperties();
		screen2Controller = new Screen2Controller(this);
		initEventsAndProperties();
		}

	public Screen2View(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}
	public Screen2View(){
		//inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Log.d("test", "calledd this! didn't initializ something");
	}
	
	private void initEventsAndProperties() {
		// create property adminTipLayout from infalter and store it as a
		// property
		
		Log.d("initialize","attempting to initialize the two buttons");
		initializeImageButton1ClickedEvent();
		initializeImageButton2ClickedEvent();

	}
	
	private void initializeImageButton1ClickedEvent() {
		ImageButton button1 = getImageButton1();
		Log.d("button is ", button1+"");
		if (button1 != null) {
			Log.d("omg", "the clicker is registered");
			button1.setOnClickListener(onImageButton1ClickListener);
		}
	}
	private void initializeImageButton2ClickedEvent() {
		ImageButton button2 = getImageButton2();
		Log.d("button2 is ", button2+"");
		if (button2 != null) {
			button2.setOnClickListener(onImageButton2ClickListener);
		}
	}
	
	
	public ImageButton getImageButton1() {
		ImageButton button1 = (ImageButton)findViewById(R.id.imageButton1);
		return button1;
	}
	
	public ImageButton getImageButton2() {
		ImageButton button2 = (ImageButton)findViewById(R.id.imageButton2);
		return button2;
	}

	private View.OnClickListener onImageButton1ClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			screen2Controller.handleImgeButton1Clicked();
		}
	};
	
	private View.OnClickListener onImageButton2ClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			screen2Controller.handleImgeButton2Clicked();
		}
	};
	
	public EditText getEditText(){
		EditText et = null;
		if (screen2Layout != null){
			et = (EditText) screen2Layout.findViewById(R.id.editText1);
		}
		
		return et;
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}


}

	
	

