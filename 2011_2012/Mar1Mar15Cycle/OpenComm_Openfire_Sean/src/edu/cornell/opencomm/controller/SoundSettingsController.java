package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.ConfirmationView;
import edu.cornell.opencomm.view.SoundSettingsView;
import edu.cornell.opencomm.view.VerticalSlideBar;

public class SoundSettingsController {
	private SoundSettingsView soundSettingsView = null;
	private HashMap<String, Space> volumeControlsHashMap = null;
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

	/*
	 * This method will associate a volume control with every space by setting its Tag property. hence
	 * when the volume level of any control changes, we fetch the corresponding Space from the stored
	 * map and populate the volume property of it
	 */
	public void setSideChatVolumeControlsOnView(ArrayList<VerticalSlideBar> sideChatVolumeControls) {
		Iterator<Entry<String, Space>> iterator = Space.allSpaces.entrySet().iterator();
		volumeControlsHashMap = new HashMap<String, Space>();
		int arrayIndex = 0;
		while(iterator.hasNext()) {
			Space space = iterator.next().getValue();
			if(!space.isMainSpace()) {
				VerticalSlideBar volumeControl = sideChatVolumeControls.get(arrayIndex++);
				volumeControl.setVisibility(View.VISIBLE);
				volumeControl.setTag(space.getRoomID());
				volumeControl.setProgress(space.getVolume());
				volumeControlsHashMap.put(space.getRoomID(), space);
				volumeControl.setOnSeekBarChangeListener(volumeControlProgressChanged);
			}
			else {	
				//add the main chat to the array too
				VerticalSlideBar volumeControl = soundSettingsView.getMainChatVolumeControl();
				volumeControl.setVisibility(View.VISIBLE);
				volumeControl.setTag(space.getRoomID());
				volumeControl.setProgress(space.getVolume());
				volumeControlsHashMap.put(space.getRoomID(), space);
				volumeControl.setOnSeekBarChangeListener(volumeControlProgressChanged);
			}			
		}
		while(arrayIndex < sideChatVolumeControls.size()) {
			sideChatVolumeControls.get(arrayIndex++).setVisibility(View.INVISIBLE);
		}

		
	}
	
	private OnSeekBarChangeListener volumeControlProgressChanged = new OnSeekBarChangeListener() {

		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Space space = volumeControlsHashMap.get(seekBar.getTag());
			//VerticalSlideBar volumeControl = (VerticalSlideBar)seekBar;
			space.setVolume(progress);
			
		}

		
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
	};
}
