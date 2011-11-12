package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConfirmationController;

public class ConfirmationView {
	private static String LOG_TAG = "OC_ConfirmationView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private ConfirmationController confirmationController = new ConfirmationController(
			this);
	private View confirmationLayout = null;

	public ConfirmationView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	private void initEventsAndProperties() {
		// create property confirmationLayout from infalter and store it as a
		// property
		if (inflater != null) {
			View confirmationLayoutFromInflater = inflater.inflate(
					R.layout.confirmation_layout, null);
			if (confirmationLayoutFromInflater != null) {
				this.confirmationLayout = confirmationLayoutFromInflater;
			}
		}
		initializeAcceptButtonHoverEvent();
		initializeCancelButtonHoverEvent();
	}

	private void initializeCancelButtonHoverEvent() {
		Button cancelButton = getAcceptButton();
		if (cancelButton != null) {
			cancelButton.setOnTouchListener(onCancelButtonTouchListener);
		}
	}

	private void initializeAcceptButtonHoverEvent() {
		Button acceptButton = getAcceptButton();
		if (acceptButton != null) {
			acceptButton.setOnTouchListener(onAcceptButtonTouchListener);
		}
	}

	public Button getAcceptButton() {
		Button acceptButton = null;
		if (confirmationLayout != null) {
			acceptButton = (Button) confirmationLayout
					.findViewById(R.id.buttonAcceptConfirmation);
		}

		return acceptButton;
	}

	public Button getCancelButton() {
		Button cancelButton = null;
		if (confirmationLayout != null) {
			cancelButton = (Button) confirmationLayout
					.findViewById(R.id.buttonCancelConfirmation);
		}

		return cancelButton;
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
		if (inflater != null && confirmationLayout != null) {
			window = new PopupWindow(confirmationLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(confirmationLayout, 0, 1, 1);
			confirmationLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch confirmation view as inflater/confirmation layout is nul");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			confirmationController.handlePopupWindowClicked();
		}
	};

	private View.OnTouchListener onAcceptButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			confirmationController.handleAcceptButtonHover();
			return true;
		}
	};
	
	private View.OnTouchListener onCancelButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			confirmationController.handleCancelButtonHover();
			return true;
		}
	};
}
