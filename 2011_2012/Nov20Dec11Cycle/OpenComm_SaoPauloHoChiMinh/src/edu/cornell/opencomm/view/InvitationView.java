package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.InvitationController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Invitation;
import edu.cornell.opencomm.model.User;

public class InvitationView {
	private static String LOG_TAG = "OC_InvitationView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private InvitationController invitationController = null;
	private View invitationLayout = null;
	private Invitation invitation = null;

	public InvitationView(LayoutInflater inflater) {
		this.inflater = inflater;
		this.invitationController =  new InvitationController(
				this);
		initEventsAndProperties();
	}
	
	public InvitationView(LayoutInflater inflater, Invitation invitation){
		this.inflater = inflater;
		this.invitation = invitation;
		this.invitationController = new InvitationController(this);
		initEventsAndProperties();
	}
	
	public InvitationView(LayoutInflater inflater, Invitation invitation, InvitationController inviteController){
		this.inflater = inflater;
		this.invitation = invitation;
		this.invitationController = inviteController;
		initEventsAndProperties();
	}

	public InvitationController getInvitationController(){
		return invitationController;
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
	
	/** Set the information on the invite 
	 * 1) If a moderator request - user image and profile info set to that of the invitee's
	 * Message should say "requester requests to invite invitee to this chat"
	 * 2) Otherwise, set to info of the requester
	 * Message should say "Requester would like to invite you to a chat"
	 */
	public void setInvitationInfo(User requester, User invitee, boolean isModeratorRequest){
		Log.v("InvitationView", "setInvitationInfo()");
		// User image
		ImageView userImage = null;
		int userImageDrawable = 0;
		// Conference Title
		String confMessage, confRequesterName, inviteeName; 
		TextView confTitle = null;
		//User Name Title
		TextView bigNameTitle = null;
		String bigName;
		// User Profile Into
		TextView userProfInfo;
		String smallName, phone, email;
		if(invitationLayout != null) {
			userImage = (ImageView) invitationLayout.findViewById(R.id.iconImage);
			confTitle = (TextView) invitationLayout.findViewById(R.id.textViewConfTitle);
			bigNameTitle = (TextView) invitationLayout.findViewById(R.id.textViewHeader);
			userProfInfo = (TextView) invitationLayout.findViewById(R.id.textViewInfo);
			
			confRequesterName = requester.getNickname();
			inviteeName = invitee.getNickname();
			
			if(isModeratorRequest){
				userImageDrawable = invitee.getImage();
				confMessage = confRequesterName + " invited " + inviteeName + " to a chat.";
				bigName = invitee.getNickname();
				smallName = invitee.getNickname();
				phone = "None";
				email = "None";
			}
			else{
				userImageDrawable = requester.getImage();
				confMessage = confRequesterName + " invited you to a chat.";
				bigName = requester.getNickname();
				smallName = requester.getNickname();
				phone = "None";
				email = "None";
			}
			userImage.setImageDrawable(MainApplication.screen.getActivity().getResources().getDrawable(userImageDrawable));
			confTitle.setText(confMessage);
			bigNameTitle.setText(bigName);
			userProfInfo.setText(smallName+"\n" + email + "\n" + phone);
		}

	}
	
	/** Use this version of setInvitationInfo if you wish to input the String values
	 * instead of the user objects. This version only for if you received an invitation
	 * and are not a moderator.
	 */
	public void setInvitationInfo(int imageDrawableInt, String inviterNickname, String inviterPhone, String inviterEmail){
		if(invitationLayout != null) {
			ImageView userImage = (ImageView) invitationLayout.findViewById(R.id.iconImage);
			TextView confTitle = (TextView) invitationLayout.findViewById(R.id.textViewConfTitle);
			TextView bigNameTitle = (TextView) invitationLayout.findViewById(R.id.textViewHeader);
			TextView userProfInfo = (TextView) invitationLayout.findViewById(R.id.textViewInfo);

			//int userImageDrawable = Values.default_user_image;//imageDrawableInt;
			int userImageDrawable = R.drawable.question;
			if(imageDrawableInt==-1)
				userImageDrawable = Values.default_user_image;
			String confMessage = inviterNickname + " invited you to a chat.";
			String bigName = inviterNickname;
			String smallName = inviterNickname;
			String phone = inviterPhone;
			String email = inviterEmail; 
			
			userImage.setImageDrawable(MainApplication.screen.getActivity().getResources().getDrawable(userImageDrawable));
			confTitle.setText(confMessage);
			bigNameTitle.setText(bigName);
			userProfInfo.setText(smallName+"\n" + email + "\n" + phone);
		}
	}
	
	/** Set the user icon image of the invite */
/*	public void setUserImage(User user){
		ImageView userImage = null;
		if(invitationLayout != null) {
			userImage = (ImageView) invitationLayout.findViewById(R.id.iconImage);
			int image = user.getImage(); 
			userImage.setImageDrawable(MainApplication.screen.getActivity().getResources().getDrawable(image));
		}
	} */
	
	/** Set the conference title message in the invite 
	 * @param requester - the requester user
	 * @param invitee - the invitee user
	 * @param moderatorRequest - true if a invite request to a moderator, 
	 * false if is a simple invite request */
	/*public void setConferenceTitle(User requester, User invitee, boolean moderatorRequest){
		String message;
		String requesterName = requester.getNickname();
		if(invitee==MainApplication.user_primary){
			message = requesterName + " would like to invite you to a chat.";
		}
		else {
			String inviteeName = invitee.getNickname();
			message = requesterName + " would like to invite " + inviteeName + " to a chat.";
		}
		TextView current = null;
		if(invitationLayout != null){
			current = (TextView) invitationLayout.findViewById(R.id.textViewConfTitle);
			current.setText(message);
		}
	} */

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

	public Invitation getInvitation(){
		return invitation;
	}
	
	/*
	 * this method launches the confirmation layout on a popupwindiw, can be
	 * changed later to launch like a normal view
	 */
	public void launch() {
		//Log.v(LOG_TAG, "Inflater is null = " + (inflater==null) + ", and invationLayout is null = " + (invitationLayout==null));
		Log.v("InvitationView", "inv 1");
		if (inflater != null && invitationLayout != null) {
			Log.v("InvitationView", "inv 2");
			window = new PopupWindow(invitationLayout, Values.screenW,
					Values.screenH, true);
			Log.v("InvitationView", "inv 3");
			window.showAtLocation(invitationLayout, 0, 1, 1);
			Log.v("InvitationView", "inv 4");
			invitationLayout.setOnClickListener(onClickListener);
			Log.v("InvitationView", "inv 5");
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
