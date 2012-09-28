package edu.cornell.opencomm.controller;

import android.util.Log;
import android.view.View;
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
