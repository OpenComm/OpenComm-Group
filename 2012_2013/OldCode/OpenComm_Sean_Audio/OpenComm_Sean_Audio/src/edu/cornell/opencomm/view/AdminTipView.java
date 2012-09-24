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

public class AdminTipView {
    private static String LOG_TAG = "OC_AdminTipView"; // for error checking
    private Context context;
    private LayoutInflater inflater;
    private PopupWindow window = null;
    private AdminTipController adminTipController = new AdminTipController(this);
    private View adminTipLayout = null;

    public AdminTipView(LayoutInflater inflater) {
        this.inflater = inflater;
        initEventsAndProperties();
    }

    private void initEventsAndProperties() {
        // create property adminTipLayout from infalter and store it as a
        // property
        if (inflater != null) {
            View tipViewFromInflater = inflater.inflate(R.layout.admintip_layout, null);
            if (tipViewFromInflater != null) {
                this.adminTipLayout = tipViewFromInflater;
            }
        }
        initializeTipButtonClickedEvent();

    }

    private void initializeTipButtonClickedEvent() {
        Button acceptButton = getAcceptButton();
        ImageButton acceptImageButton = getAcceptImageButton();
        if (acceptButton != null) {
            Log.d(LOG_TAG, "initializing tip button clicked");
            acceptButton.setOnClickListener(onAcceptButtonClickedListener);
        }
        if (acceptImageButton != null) {
            Log.d(LOG_TAG, "initializing tip button clicked");
            acceptImageButton.setOnClickListener(onAcceptButtonClickedListener);
        }
    }

    public ImageView getAcceptBarOverlay() {
        ImageView acceptOverlay = null;
        if (adminTipLayout != null) {
            acceptOverlay = (ImageView) adminTipLayout.findViewById(R.id.adminTipOverlay);
        }
        return acceptOverlay;
    }
    public ImageButton getAcceptImageButton() {
        ImageButton acceptButton = null;
        if (adminTipLayout != null) {
            acceptButton = (ImageButton) adminTipLayout.findViewById(R.id.imageAcceptAdminTip);
        }

        return acceptButton;
    }
    public Button getAcceptButton() {
        Button acceptButton = null;
        if (adminTipLayout != null) {
            acceptButton = (Button) adminTipLayout
                    .findViewById(R.id.buttonAcceptAdminTip);
        }

        return acceptButton;
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
        if (inflater != null && adminTipLayout != null) {
            window = new PopupWindow(adminTipLayout, Values.screenW,
                    Values.screenH, true);
            window.showAtLocation(adminTipLayout, 0, 1, 1);
            adminTipLayout.setOnClickListener(onClickListener);
        } else {
            Log.v(LOG_TAG,
                    "Cannot launch admin tip view as inflater layout is null");
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            adminTipController.handlePopupWindowClicked();
        }
    };

    private View.OnClickListener onAcceptButtonClickedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "onClickAcceptBar");
            adminTipController.handleAcceptButtonClicked();
        }
    };
}
