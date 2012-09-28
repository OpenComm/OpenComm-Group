package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.EmailController;

public class EmailView {
	  private static String TAG = "EmailView";
      private static boolean D = Values.D;

      private Context context;
      private View layout;
      private PopupWindow popup;
      private EmailController controller;
      
      /**
       * Constructor
       * @param context
       */
      public EmailView(Context context) {
          this.context = context;

          LayoutInflater inflater = (LayoutInflater) context
                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          layout = inflater.inflate(R.layout.email_layout, null);
          popup = new PopupWindow(layout, Values.screenW,
                  Values.screenH, true);
          controller = new EmailController(this, context);
          initEventsAndProperties();
      }
      
      /**
       * Display the popup on screen
       */
      public void launch() {
          if (D) Log.d(TAG, "launch called");
          popup.showAtLocation(layout, 0, 0, 0);
      }
      
      /**
       * Dismisses the popup window
       */
      public void dismiss() {
          if (D) Log.d(TAG, "dismiss called");
          popup.dismiss();
      }
      
      /**
       * Set up click handlers
       */
      public void initEventsAndProperties() {
          if (D) Log.d(TAG, "initEventsAndProperties called");
          initializeSendButtonClickEvent();
          initializeCancelButtonClickEvent();
          initializeEmailButtonFocusChangeEvent();
      }

	private void initializeEmailButtonFocusChangeEvent() {
        if (D) Log.d(TAG, "initializeEmailButtonFocusChangeEvent called");
        View emailBox = getEmailBox();
        if (emailBox != null) {
            emailBox.setOnFocusChangeListener(getEmailFocusChangeHandler());
        }
		
	}

	private OnFocusChangeListener getEmailFocusChangeHandler() {
        if (D) Log.d(TAG, "getEmailFocusChangeHandler called");
        return new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                controller.handleEmailFocusChange(hasFocus);
            }
        };
	}
	
	/**
     * @return Email Box
     */
	public EditText getEmailBox() {
        if (D) Log.d(TAG, "getEmailBox called");
        EditText EmailBox = null;
        if (getContext() != null) {
            EmailBox = (EditText) layout
                    .findViewById(R.id.emailBox);
        }
        return EmailBox;
	}

	private void initializeCancelButtonClickEvent() {
        if (D) Log.d(TAG, "initializeCancelButtonClickEvent called");
        View cancelButton = getCancelButton();
        if (cancelButton != null) {
            cancelButton.setOnClickListener(getCancelButtonHandler());
        }
        cancelButton = getCancelImageButton();
        if (cancelButton != null) {
            cancelButton.setOnClickListener(getCancelButtonHandler());
        }
		
	}

	private OnClickListener getCancelButtonHandler() {
        if (D) Log.d(TAG, "getCancelButtonHandler called");
        return new View.OnClickListener() {

        	public void onClick(View v) {
                controller.handleCancelButtonClick();
            }
        };
	}

	private View getCancelImageButton() {
        if (D) Log.d(TAG, "getCancelImageButton called");
        View cancelImageButton = null;
        if (getContext() != null) {
            cancelImageButton = (View)
                    layout.findViewById(R.id.imageCancel);
        }
        return cancelImageButton;
	}

	private View getCancelButton() {
        if (D) Log.d(TAG, "getCancelButton called");
        View cancelButton = null;
        if (getContext() != null) {
            cancelButton = (View) layout
                    .findViewById(R.id.buttonCancel);
        }
        return cancelButton;
	}

    /**
     * Sets save button click handler
     */
	private void initializeSendButtonClickEvent() {
        if (D) Log.d(TAG, "initializeSaveButtonClickEvent called");
        View saveButton = getSendButton();
        if (saveButton != null) {
            saveButton.setOnClickListener(getSendButtonHandler());
        }
        saveButton = getSendImageButton();
        if (saveButton != null) {
            saveButton.setOnClickListener(getSendButtonHandler());
        }
		
	}

	private OnClickListener getSendButtonHandler() {
        if (D) Log.d(TAG, "getSendButtonHandler called");
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                controller.handleSendButtonClick();
            }
        };
	}

    /**
     * @return Send Button View
     */
    public View getSendButton() {
        if (D) Log.d(TAG, "getSendButton called");
        View sendButton = null;
        if (getContext() != null) {
            sendButton = (View)
                    layout.findViewById(R.id.buttonSend);
        }
        return sendButton;
    }
    
    public View getSendImageButton() {
        if (D) Log.d(TAG, "getSaveImageButton called");
        View sendImageButton = null;
        if (getContext() != null) {
            sendImageButton = (View)
                    layout.findViewById(R.id.imageSend);
        }
        return sendImageButton;
    }
	
    /**
     * getter for Context
     */
    public Context getContext() {
        return context;
    }
}
