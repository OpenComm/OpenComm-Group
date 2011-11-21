package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.InvitationController;

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
		Button cancelButton = getCancelButton();
		if (cancelButton != null) {
			cancelButton.setOnClickListener(onCancelButtonClickListener);
		}
		ImageButton imageCancelButton = getImageCancelButton();
		if (imageCancelButton != null) {
			imageCancelButton.setOnClickListener(onCancelButtonClickListener);
		}
	}

	private void initializeAcceptButtonHoverEvent() {
		Button acceptButton = getAcceptButton();
		if (acceptButton != null) {
			acceptButton.setOnClickListener(onAcceptButtonClickListener);
		}
		ImageButton imageAcceptButton = getImageAcceptButton();
		if (imageAcceptButton != null) {
			imageAcceptButton.setOnClickListener(onAcceptButtonClickListener);
		}
	}

	public Button getAcceptButton() {
		Button acceptButton = null;
		if (invitationLayout != null) {
			acceptButton = (Button) invitationLayout
					.findViewById(R.id.buttonAcceptInvite);
		}

		return acceptButton;
	}
	public ImageButton getImageAcceptButton() {
		ImageButton imageAcceptButton = null;
		if (invitationLayout != null) {
			imageAcceptButton = (ImageButton) invitationLayout
					.findViewById(R.id.imageAcceptInvite);
		}

		return imageAcceptButton;
	}
	
	public ImageView getAcceptOverlay() {
		ImageView acceptOverlay = null;
		if (invitationLayout != null) {
			acceptOverlay = (ImageView) invitationLayout
					.findViewById(R.id.acceptInviteOverlay);
		}

		return acceptOverlay;
	}

	public Button getCancelButton() {
		Button cancelButton = null;
		if (invitationLayout != null) {
			cancelButton = (Button) invitationLayout
					.findViewById(R.id.buttonCancelInvite);
		}

		return cancelButton;
	}
	
	public ImageButton getImageCancelButton() {
		ImageButton imageCancelButton = null;
		if (invitationLayout != null) {
			imageCancelButton = (ImageButton) invitationLayout
					.findViewById(R.id.imageCancelInvite);
		}

		return imageCancelButton;
	}
	
	public ImageView getCancelOverlay() {
		ImageView cancelOverlay = null;
		if (invitationLayout != null) {
			cancelOverlay = (ImageView) invitationLayout
					.findViewById(R.id.cancelInviteOverlay);
		}

		return cancelOverlay;
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

	private View.OnClickListener onAcceptButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			invitationController.handleAcceptButtonHover();
		}
	};
	
	private View.OnClickListener onCancelButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			invitationController.handleCancelButtonHover();
		}
	};
}
