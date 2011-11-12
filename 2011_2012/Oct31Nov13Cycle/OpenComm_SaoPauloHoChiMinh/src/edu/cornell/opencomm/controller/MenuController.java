package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.MenuView;

public class MenuController {
	private MenuView menuView = null;
	public MenuController(MenuView menuView) {
		this.menuView = menuView;
	}
	public void handlePopupWindowClicked() {
		menuView.getWindow().dismiss();		
	}
	public void handleAddUserButtonHover() {
		menuView.getAddUserButton().setBackgroundColor(R.color.light_grey);
		
	}
	public void handleDeleteUserButtonHover() {
		menuView.getDeleteUserButton().setBackgroundColor(R.color.light_grey);
	}
	public void handleLeaveChatButtonHover() {
		menuView.getLeaveChatButton().setBackgroundColor(R.color.light_grey);
	}
	public void handleSettingsButtonHover() {
		menuView.getSettingsButton().setBackgroundColor(R.color.light_grey);
	}

}
