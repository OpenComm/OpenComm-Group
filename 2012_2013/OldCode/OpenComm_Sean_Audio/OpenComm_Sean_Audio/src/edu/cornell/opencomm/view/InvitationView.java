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

//TODO jonathan: Sanity Check This File

/**
 * Handles Invitation Popups
 */
public class InvitationView {
    private static String LOG_TAG = "OC_InvitationView"; // for error checking
    private Context context;
    private LayoutInflater inflater;
    private PopupWindow window = null;
    private InvitationController invitationController = null;
    private View invitationLayout = null;
    private Invitation invitation = null;

    private static boolean D = Values.D;
    private static String TAG  = "InvitationView";

    /**
     * Constructor
     */
    public InvitationView(LayoutInflater inflater) {
        if (D) Log.d(TAG, "InvitationView constructor called");
        this.inflater = inflater;
        this.invitationController =  new InvitationController(
                this);
        initEventsAndProperties();
    } // end InvitationView

    /**
     * Constructor
     * Delegates to previous constructor
     */
    public InvitationView(LayoutInflater inflater, Invitation invitation) {
        this(inflater);
        if (D) Log.d(TAG, "InvitationView constructor called");
        this.invitation = invitation;
    } // end InvitationView

    /**
     * Constructor
     * Delegates to previous constructor
     */
    public InvitationView(LayoutInflater inflater, Invitation invitation, InvitationController inviteController) {
        this(inflater, invitation);
        if (D) Log.d(TAG, "InvitationView constructor called");
        this.invitationController = inviteController;
    } // end InvitationView


    /**
     * Returns invitation controller
     */
    public InvitationController getInvitationController() {
        if (D) Log.d(TAG, "getInvitationController called");
        return invitationController;
    } // end getInvitationController

    /**
     * Initializes Event handlers and inflates the layout
     */
    private void initEventsAndProperties() {
        if (D) Log.d(TAG, "initEventsAndProperties called");
        if (inflater != null) {
            View invitationLayoutFromInflater = inflater.inflate(
                    R.layout.invitation_layout, null);
            if (invitationLayoutFromInflater != null) {
                this.invitationLayout = invitationLayoutFromInflater;
            }
        }
        initializeAcceptButtonHoverEvent();
        initializeCancelButtonHoverEvent();
    } // end initEventsAndProperties

    /**
     * Sets OnClickListener for Cancel Button Hover
     */
    private void initializeCancelButtonHoverEvent() {
        if (D) Log.d(TAG, "initializeCancelButtonHoverEvent called");
        Button cancelButton = getCancelButton();
        if (cancelButton != null) {
            cancelButton.setOnClickListener(onCancelButtonClickListener);
        }
        ImageButton imageCancelButton = getImageCancelButton();
        if (imageCancelButton != null) {
            imageCancelButton.setOnClickListener(onCancelButtonClickListener);
        }
    } // end initializeCancelButtonHoverEvent

    /**
     * Sets OnClickListener for Accept Button Hover
     */
    private void initializeAcceptButtonHoverEvent() {
        if (D) Log.d(TAG, "initializeAcceptButtonHoverEvent called");
        Button acceptButton = getAcceptButton();
        if (acceptButton != null) {
            acceptButton.setOnClickListener(onAcceptButtonClickListener);
        }
        ImageButton imageAcceptButton = getImageAcceptButton();
        if (imageAcceptButton != null) {
            imageAcceptButton.setOnClickListener(onAcceptButtonClickListener);
        }
    } // end initializeAcceptButtonHoverEvent

    /**
     * Returns Accept Button from the invitation layout
     */
    public Button getAcceptButton() {
        if (D) Log.d(TAG, "getAcceptButton called");
        Button acceptButton = null;
        if (invitationLayout != null) {
            acceptButton = (Button) invitationLayout
                    .findViewById(R.id.buttonAcceptInvite);
        }

        return acceptButton;
    } // end getAcceptButton

    /**
     * Returns Image Accept Button from the invitation layout
     */
    public ImageButton getImageAcceptButton() {
        if (D) Log.d(TAG, "getAcceptButton called");
        ImageButton imageAcceptButton = null;
        if (invitationLayout != null) {
            imageAcceptButton = (ImageButton) invitationLayout
                    .findViewById(R.id.imageAcceptInvite);
        }

        return imageAcceptButton;
    }  // getImageAcceptButton

    /**
     * Gets the Accept Overlay from the invitation layout
     */
    public ImageView getAcceptOverlay() {
        if (D) Log.d(TAG, "getAcceptOverlay called");
        ImageView acceptOverlay = null;
        if (invitationLayout != null) {
            acceptOverlay = (ImageView) invitationLayout
                    .findViewById(R.id.acceptInviteOverlay);
        }

        return acceptOverlay;
    } // end getAcceptOverlay

    /**
     * Gets the Cancel Button from the invitation layout
     */
    public Button getCancelButton() {
        if (D) Log.d(TAG, "getCancelButton called");
        Button cancelButton = null;
        if (invitationLayout != null) {
            cancelButton = (Button) invitationLayout
                    .findViewById(R.id.buttonCancelInvite);
        }

        return cancelButton;
    } // end getCancelButton

    /**
     * Gets the Cancel Button Image from the invitation layout
     */
    public ImageButton getImageCancelButton() {
        if (D) Log.d(TAG, "getImageCancelButton called");
        ImageButton imageCancelButton = null;
        if (invitationLayout != null) {
            imageCancelButton = (ImageButton) invitationLayout
                    .findViewById(R.id.imageCancelInvite);
        }

        return imageCancelButton;
    } // end getImageCancelButton

    /**
     * Gets the Cancel Overlay from the invitation layout
     */
    public ImageView getCancelOverlay() {
        if (D) Log.d(TAG, "getCancelOverlay called");
        ImageView cancelOverlay = null;
        if (invitationLayout != null) {
            cancelOverlay = (ImageView) invitationLayout
                    .findViewById(R.id.cancelInviteOverlay);
        }

        return cancelOverlay;
    } // end getCancelOverlay

    /**
     * Set the information on the invite
     * 1) If a moderator request - user image and profile info set to that of the invitee's
     * Message should say "requester requests to invite invitee to this chat"
     * 2) Otherwise, set to info of the requester
     * Message should say "Requester would like to invite you to a chat"
     */
    public void setInvitationInfo(User requester, User invitee, boolean isModeratorRequest) {
        if (D) Log.d(TAG, "setInvitationInfo called");
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

    } // end setInvitationInfo

    /**
     * Use this version of setInvitationInfo if you wish to input the String values
     * instead of the user objects. This version only for if you received an invitation
     * and are not a moderator.
     */
    public void setInvitationInfo(int imageDrawableInt, String inviterNickname, String inviterPhone, String inviterEmail) {
        if (D) Log.d(TAG, "setInvitationInfo called");
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
    } // end setInvitationInfo

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
        if (D) Log.d(TAG, "launch called");
        //Log.v(LOG_TAG, "Inflater is null = " + (inflater==null) + ", and invationLayout is null = " + (invitationLayout==null));
        if (inflater != null && invitationLayout != null) {
            window = new PopupWindow(invitationLayout, Values.screenW,
                    Values.screenH, true);
            window.showAtLocation(invitationLayout, 0, 1, 1);
            invitationLayout.setOnClickListener(onClickListener);
        } else {
            Log.v(LOG_TAG,
                    "Cannot launch invitation view as inflater/confirmation layout is nul");
        }
    } // end launch

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
