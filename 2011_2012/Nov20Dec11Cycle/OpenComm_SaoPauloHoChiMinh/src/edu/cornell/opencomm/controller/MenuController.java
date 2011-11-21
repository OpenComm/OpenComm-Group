package edu.cornell.opencomm.controller;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.MenuView;

public class MenuController {
	private MenuView menuView = null;
	public MenuController(MenuView menuView) {
		this.menuView = menuView;
	}
	
	public void handleMenuButtonClicked() {
		Log.d("MenuController", "Menu button clicked");
		menuView.getWindow().dismiss();
	}
	
	public void handlePopupWindowClicked() {
		menuView.getWindow().dismiss();		
	}
	public void handleAddUserButtonHover() {
		menuView.getAddUserOverlay().setVisibility(View.VISIBLE);
		// replace below with functionality
		menuView.getWindow().dismiss();
		
	}
	public void handleDeleteUserButtonHover() {
		menuView.getDeleteUserOverlay().setVisibility(View.VISIBLE);
		// replace below with functionality
		menuView.getWindow().dismiss();
	}
	public void handleLeaveChatButtonHover() {
		menuView.getLeaveChatOverlay().setVisibility(View.VISIBLE);
		// replace below with functionality
		menuView.getWindow().dismiss();
	}
	
	public void handleSettingsButtonHover() {
		menuView.getSettingsOverlay().setVisibility(View.VISIBLE);
		// replace below with functionality
		menuView.getWindow().dismiss();
	}
	
	public void handleLogoutButtonHover() {
		menuView.getLogoutOverlay().setVisibility(View.VISIBLE);
		// replace below with functionality
		menuView.getWindow().dismiss();
	}

}
