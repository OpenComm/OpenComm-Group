package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.layout;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConfirmationController;
import edu.cornell.opencomm.controller.InvitationController;
import edu.cornell.opencomm.model.Space;

public class InvitationView {
	private static String LOG_TAG = "OC_InvitationView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private InvitationController invitationController = new InvitationController(
			this);
	private View invitationLayout = null;

	public InvitationView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	private void initEventsAndProperties() {
		// create property invitationLayout from infalter and store it as a
		// property
		if (inflater != null) {
			View invitationLayoutFromInflater = inflater.inflate(
					R.layout.invitation_layout, null);
			if (invitationLayoutFromInflater != null) {
				this.invitationLayout = invitationLayoutFromInflater;
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
		if (invitationLayout != null) {
			acceptButton = (Button) invitationLayout
					.findViewById(R.id.buttonAcceptConfirmation);
		}

		return acceptButton;
	}

	public Button getCancelButton() {
		Button cancelButton = null;
		if (invitationLayout != null) {
			cancelButton = (Button) invitationLayout
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
		if (inflater != null && invitationLayout != null) {
			window = new PopupWindow(invitationLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(invitationLayout, 0, 1, 1);
			invitationLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch invitation view as inflater/confirmation layout is nul");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			invitationController.handlePopupWindowClicked();
		}
	};

	private View.OnTouchListener onAcceptButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			invitationController.handleAcceptButtonHover();
			return true;
		}
	};
	
	private View.OnTouchListener onCancelButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			invitationController.handleCancelButtonHover();
			return true;
		}
	};
}
