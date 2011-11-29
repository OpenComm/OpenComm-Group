package edu.cornell.opencomm.controller;

import android.view.View;
import edu.cornell.opencomm.view.ConfirmationView;
import edu.cornell.opencomm.view.SoundSettingsView;

public class SoundSettingsController {
	private SoundSettingsView soundSettingsView = null;
	public SoundSettingsController(SoundSettingsView soundSettingsView) {
		this.soundSettingsView = soundSettingsView;
	}
	
	/** Handle when the window is pressed */
	public void handlePopupWindowClicked() {
		// Dismisses the window for now
		soundSettingsView.getWindow().dismiss();		
	}
	
	/** Handle when the cancel button is pressed */
	public void handleCancelButtonHover() {
		//soundSettingsView.getCancelOverlay().setVisibility(View.VISIBLE);
		// Dismisses the window for now
		soundSettingsView.getWindow().dismiss();
	}
}
