package com.example.helloandroid.controller;

import android.util.Log;
import android.widget.EditText;

import com.example.helloandroid.R;
import com.example.helloandroid.view.MainScreenView;
import com.example.helloandroid.view.Screen2View;

public class Screen2Controller {
    private Screen2View screen2View;
    private EditText textToDisplay;

	public Screen2Controller(Screen2View screen2View) {
		this.screen2View = screen2View;
	}

	public void handleImgeButton2Clicked() {
		Log.d("Buttons", "Button2clicked");

		   EditText text2 = (EditText)screen2View.findViewById(R.id.editText1);
		   text2.setText(R.string.button2text);
		
		
	}

	public void handleImgeButton1Clicked() {
		Log.d("Buttons", "Button1clicked");
		   EditText text1 = (EditText)screen2View.findViewById(R.id.editText1);
		   text1.setText(R.string.button1text);
		
	}
	
	
}
