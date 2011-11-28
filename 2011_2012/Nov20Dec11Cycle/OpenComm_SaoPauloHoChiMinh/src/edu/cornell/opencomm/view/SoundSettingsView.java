package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.AdminTipController;
import edu.cornell.opencomm.controller.SoundSettingsController;

public class SoundSettingsView {
	private static String LOG_TAG = "OC_SoundSettingsView"; // for error
															// checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private SoundSettingsController soundSettingsController = new SoundSettingsController(
			this);
	private View soundSettingsLayout = null;

	public SoundSettingsView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	private void initEventsAndProperties() {
		// create property soundSettingsLayout from infalter and store it as a
		// property
		if (inflater != null) {
			View soundSettingsViewFromInflater = inflater.inflate(
					R.layout.sound_settings, null);
			if (soundSettingsViewFromInflater != null) {
				this.soundSettingsLayout = soundSettingsViewFromInflater;
			}
		}
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

	public PopupWindow getWindow() {
		return window;
	}

	public void setWindow(PopupWindow window) {
		this.window = window;
	}

	/*
	 * this method launches the confirmation layout on a popupwindiw, can be
	 * changed later to launch like a normal view
	 */
	public void launch() {
		if (inflater != null && soundSettingsLayout != null) {
			window = new PopupWindow(soundSettingsLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(soundSettingsLayout, 0, 1, 1);
			soundSettingsLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch sound settings view as inflater layout is null");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			soundSettingsController.handlePopupWindowClicked();
		}
	};

}
