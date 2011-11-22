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
import edu.cornell.opencomm.controller.TipController;

public class TipView {
	private static String LOG_TAG = "OC_LoginView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private TipController tipController = new TipController(this);
	private View tiplayout = null;

	public TipView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	private void initEventsAndProperties() {
		// create property tiplayout from infalter and store it as a
		// property
		if (inflater != null) {
			View tipViewFromInflater = inflater.inflate(
					R.layout.tip_layout, null);
			if (tipViewFromInflater != null) {
				this.tiplayout = tipViewFromInflater;
			}
		}
		initializeTipButtonClickedEvent();

	}

	private void initializeTipButtonClickedEvent() {
		Button acceptButton = getAcceptButton();
		ImageButton acceptImageButton = getAcceptImageButton();
		if (acceptButton != null) {
			acceptButton.setOnClickListener(onAcceptButtonClickedListener);
		}
		if (acceptImageButton != null) {
			acceptImageButton.setOnClickListener(onAcceptButtonClickedListener);
		}
	}

	public Button getAcceptButton() {
		Button acceptButton = null;
		if (tiplayout != null) {
			acceptButton = (Button) tiplayout
					.findViewById(R.id.buttonAcceptTip);
		}

		return acceptButton;
	}
	
	public ImageButton getAcceptImageButton() {
		ImageButton acceptButton = null;
		if (tiplayout != null) {
			acceptButton = (ImageButton) tiplayout.findViewById(R.id.imageAcceptAdminTip);
		}

		return acceptButton;
	}
	
	public ImageView getAcceptBarOverlay() {
		ImageView acceptOverlay = null;
		if (tiplayout != null) {
			acceptOverlay = (ImageView) tiplayout.findViewById(R.id.tipOverlay);
		}
		return acceptOverlay;
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
		if (inflater != null && tiplayout != null) {
			window = new PopupWindow(tiplayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(tiplayout, 0, 1, 1);
			tiplayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch tip view as inflater layout is null");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			tipController.handlePopupWindowClicked();
		}
	};

	private View.OnClickListener onAcceptButtonClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			tipController.handleAcceptButtonClicked();
		}
	};
}
