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
import edu.cornell.opencomm.controller.ConfirmationController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;

public class ConfirmationView {
    private static String LOG_TAG = "OC_ConfirmationView"; // for error checking
    private Context context;
    private LayoutInflater inflater;
    private PopupWindow window = null;
    private ConfirmationController confirmationController = new ConfirmationController(
            this);
    private View confirmationLayout = null;
    private String[] kickoutInfo = null;
    private Space space;

    public ConfirmationView(LayoutInflater inflater) {
        this.inflater = inflater;
        initEventsAndProperties();
    }

    public ConfirmationView(LayoutInflater inflater, String[] kickoutInfo,
            Space space) {
        this.inflater = inflater;
        this.kickoutInfo = kickoutInfo;
        this.space = space;
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

    /** Sets the text information in the confirmation popup GUI */
    public void setConfirmationInfo(User requester, User kickee,
            boolean isModeratorRequest) {
        // User image
        ImageView userImage = null;
        int userImageDrawable = 0;
        // Conference Title
        String confMessage, confRequesterName, kickeeName;
        TextView confTitle = null;
        // User Name Title
        TextView bigNameTitle = null;
        String bigName;
        // User Profile Into
        TextView userProfInfo;
        String smallName, phone, email;
        if (confirmationLayout != null) {
            userImage = (ImageView) confirmationLayout
                    .findViewById(R.id.iconImage);
            confTitle = (TextView) confirmationLayout
                    .findViewById(R.id.textViewConfTitle);
            bigNameTitle = (TextView) confirmationLayout
                    .findViewById(R.id.textViewHeader);
            userProfInfo = (TextView) confirmationLayout
                    .findViewById(R.id.textViewInfo);

            confRequesterName = requester.getNickname();
            kickeeName = kickee.getNickname();

            if (isModeratorRequest) {
                userImageDrawable = kickee.getImage();
                confMessage = confRequesterName + " would like to remove "
                        + kickeeName + " from the chat.";
                bigName = kickee.getNickname();
                smallName = kickee.getNickname();
                phone = "None";
                email = "None";
            } else {
                userImageDrawable = requester.getImage();
                confMessage = "You have been removed from this chat.";
                bigName = requester.getNickname();
                smallName = requester.getNickname();
                phone = "None";
                email = "None";
            }
            userImage.setImageDrawable(MainApplication.screen.getActivity()
                    .getResources().getDrawable(userImageDrawable));
            confTitle.setText(confMessage);
            bigNameTitle.setText(bigName);
            userProfInfo.setText(smallName + "\n" + email + "\n" + phone);
        }
    }

    public Button getAcceptButton() {
        Button acceptButton = null;
        if (confirmationLayout != null) {
            acceptButton = (Button) confirmationLayout
                    .findViewById(R.id.buttonAcceptConfirm);
        }

        return acceptButton;
    }

    public ImageButton getImageAcceptButton() {
        ImageButton imageAcceptButton = null;
        if (confirmationLayout != null) {
            imageAcceptButton = (ImageButton) confirmationLayout
                    .findViewById(R.id.imageAcceptConfirm);
        }

        return imageAcceptButton;
    }

    public ImageView getAcceptOverlay() {
        ImageView acceptOverlay = null;
        if (confirmationLayout != null) {
            acceptOverlay = (ImageView) confirmationLayout
                    .findViewById(R.id.acceptConfirmOverlay);
        }

        return acceptOverlay;
    }

    public Button getCancelButton() {
        Button cancelButton = null;
        if (confirmationLayout != null) {
            cancelButton = (Button) confirmationLayout
                    .findViewById(R.id.buttonCancelConfirm);
        }

        return cancelButton;
    }

    public ImageButton getImageCancelButton() {
        ImageButton imageCancelButton = null;
        if (confirmationLayout != null) {
            imageCancelButton = (ImageButton) confirmationLayout
                    .findViewById(R.id.imageCancelConfirm);
        }

        return imageCancelButton;
    }

    public ImageView getCancelOverlay() {
        ImageView cancelOverlay = null;
        if (confirmationLayout != null) {
            cancelOverlay = (ImageView) confirmationLayout
                    .findViewById(R.id.cancelConfirmOverlay);
        }

        return cancelOverlay;
    }

    public ConfirmationController getConfirmationController() {
        return confirmationController;
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

    public String[] getKickoutInfo() {
        return kickoutInfo;
    }

    public void setKickoutInfo(String[] info) {
        kickoutInfo = info;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
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

    private View.OnClickListener onAcceptButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            confirmationController.handleAcceptButtonHover();
        }
    };

    private View.OnClickListener onCancelButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            confirmationController.handleCancelButtonHover();
        }
    };
}
