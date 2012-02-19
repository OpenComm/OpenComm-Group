package com.example.helloandroid.controller;

import com.example.helloandroid.R;
import com.example.helloandroid.R.layout;
import com.example.helloandroid.view.MainScreenView;
import com.example.helloandroid.view.Screen2View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainScreenController {
    /** Called when the activity is first created. */


    
    private MainScreenView mainScreenView;

	public MainScreenController(MainScreenView mainScreenView) {
		this.mainScreenView = mainScreenView;
	}
	
	public void handleGoButtonClicked() {
		 Intent myIntent = new Intent();
		 myIntent.setClass(mainScreenView,Screen2View.class);
         mainScreenView.startActivity(myIntent);
	}
	
    
}