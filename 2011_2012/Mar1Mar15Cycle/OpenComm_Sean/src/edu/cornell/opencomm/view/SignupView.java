package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.SignupController;

/**
 * View that handles account signup
 * @author jonathan
 *
 */
public class SignupView {

        private static String TAG = "SignupView";
        private static boolean D = Values.D;

        private Context context;
        private View layout;
        private PopupWindow popup;
        private SignupController controller;
        private int focus;

        /**
         * Constructor
         * @param context
         */
        public SignupView(Context context) {
            this.context = context;

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.signup, null);
            popup = new PopupWindow(layout, Values.screenW,
                    Values.screenH, true);
            controller = new SignupController(this, context);
            initEventsAndProperties();
            focus = getFirstNameBox().getId();
        } // end SignupView

        /**
         * Dismisses the popup window
         */
        public void dismiss() {
            if (D) Log.d(TAG, "dismiss called");
            popup.dismiss();
        } // end dismiss

        /**
         * Display the popup on screen
         */
        public void launch() {
            if (D) Log.d(TAG, "launch called");
            popup.showAtLocation(layout, 0, 0, 0);
            //invitationLayout.setOnClickListener(onClickListener);
        } // end launch

        /**
         * Set up click handlers
         */
        public void initEventsAndProperties() {
            if (D) Log.d(TAG, "initEventsAndProperties called");
            initializeSaveButtonClickEvent();
            initializeCancelButtonClickEvent();
            initializeFirstNameButtonFocusChangeEvent();
            initializeLastNameButtonFocusChangeEvent();
            initializeEmailButtonFocusChangeEvent();
        }

        /**
         * Sets save button click handler
         */
        public void initializeSaveButtonClickEvent() {
            if (D) Log.d(TAG, "initializeSaveButtonClickEvent called");
            View saveButton = getSaveButton();
            if (saveButton != null) {
                saveButton.setOnClickListener(getSaveButtonHandler());
            }
        }

        /**
         * Sets cancel button click handler
         */
        public void initializeCancelButtonClickEvent() {
            if (D) Log.d(TAG, "initializeCancelButtonClickEvent called");
            View cancelButton = getCancelButton();
            if (cancelButton != null) {
                cancelButton.setOnClickListener(getCancelButtonHandler());
            }
        }


        /**
         * Sets handler for validation when first name focus changes
         */
        public void initializeFirstNameButtonFocusChangeEvent() {
            if (D) Log.d(TAG, "initializeFirstNameButtonFocusChangeEvent called");
            View firstNameBox = getFirstNameBox();
            if (firstNameBox != null) {
                firstNameBox.setOnFocusChangeListener(getFirstNameFocusChangeHandler());
            }
        }

        /**
         * Sets handler for validation when last name focus changes
         */
        public void initializeLastNameButtonFocusChangeEvent() {
            if (D) Log.d(TAG, "initializeLastNameButtonFocusChangeEvent called");
            View lastNameBox = getLastNameBox();
            if (lastNameBox != null) {
                lastNameBox.setOnFocusChangeListener(getLastNameFocusChangeHandler());
            }
        }

        /**
         * Sets handler for validation when email focus changes
         */
        public void initializeEmailButtonFocusChangeEvent() {
            if (D) Log.d(TAG, "initializeEmailButtonFocusChangeEvent called");
            View emailBox = getEmailBox();
            if (emailBox != null) {
                emailBox.setOnFocusChangeListener(getEmailFocusChangeHandler());
            }
        }

        /**
         * Sets handler photo button click event
         */
        public View.OnClickListener initializePhotoButtonClickEvent() {
            if (D) Log.d(TAG, "initializePhotoButtonClickEvent called");
            return new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    controller.handleSaveButtonClick();
                }
            };
        }

        /**
         * @return Event handler for first name focus changes
         */
        private OnFocusChangeListener getFirstNameFocusChangeHandler() {
            if (D) Log.d(TAG, "getFirstNameFocusChangeHandler called");
            return new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    controller.handleFirstNameFocusChange(hasFocus);
                }
            };
        }

        /**
         * @return Event handler for last name focus changes
         */
        private OnFocusChangeListener getLastNameFocusChangeHandler() {
            if (D) Log.d(TAG, "getLastNameFocusChangeHandler called");
            return new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    controller.handleLastNameFocusChange(hasFocus);
                }
            };
        }

        /**
         * @return Event handler for email focus changes
         */
        private OnFocusChangeListener getEmailFocusChangeHandler() {
            if (D) Log.d(TAG, "getEmailFocusChangeHandler called");
            return new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    controller.handleEmailFocusChange(hasFocus);
                }
            };
        }

        /**
         * @return Event handler for save button clicks
         */
        private View.OnClickListener getSaveButtonHandler() {
            if (D) Log.d(TAG, "getSaveButtonHandler called");
            return new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    controller.handleSaveButtonClick();
                }
            };
        }

        /**
         * @return Event handler for cancel button clicks
         */
        private View.OnClickListener getCancelButtonHandler() {
            if (D) Log.d(TAG, "getCancelButtonHandler called");
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.handleCancelButtonClick();
                }
            };
        }

        /**
         * @return Save Button View
         */
        public View getSaveButton() {
            if (D) Log.d(TAG, "getSaveButton called");
            View saveButton = null;
            if (getContext() != null) {
                saveButton = (View)
                        layout.findViewById(R.id.saveButton);
            }
            return saveButton;
        }

        /**
         * @return Cancel Button View
         */
        public View getCancelButton() {
            if (D) Log.d(TAG, "getCancelButton called");
            View cancelButton = null;
            if (getContext() != null) {
                cancelButton = (View) layout
                        .findViewById(R.id.cancelButton);
            }
            return cancelButton;
        }

        /**
         * @return First Name Box
         */
        public EditText getFirstNameBox() {
            if (D) Log.d(TAG, "getFirstNameBox called");
            EditText firstNameBox = null;
            if (getContext() != null) {
                firstNameBox = (EditText) layout
                        .findViewById(R.id.firstNameBox);
            }
            return firstNameBox;
        }

        /**
         * @return Last Name Box
         */
        public EditText getLastNameBox() {
            if (D) Log.d(TAG, "getLastNameBox called");
            EditText lastNameBox = null;
            if (getContext() != null) {
                lastNameBox = (EditText) layout
                        .findViewById(R.id.lastNameBox);
            }
            return lastNameBox;
        }

        /**
         * @return Email Box
         */
        public EditText getEmailBox() {
            if (D) Log.d(TAG, "getEmailBox called");
            EditText lastNameBox = null;
            if (getContext() != null) {
                lastNameBox = (EditText) layout
                        .findViewById(R.id.emailBox);
            }
            return lastNameBox;
        }
        
        /**
         * @return Password Box
         */
        public EditText getPasswordBox() {
            if (D) Log.d(TAG, "getPasswordBox called");
            EditText passwordBox = null;
            if (getContext() != null) {
                passwordBox = (EditText) layout
                        .findViewById(R.id.emailBox);
            }
            return passwordBox;
        }

        /**
         * @return Title Box
         */
        public EditText getTitleBox() {
            if (D) Log.d(TAG, "getTitleBox called");
            EditText titleBox = null;
            if (getContext() != null) {
                titleBox = (EditText) layout
                        .findViewById(R.id.titleBox);
            }
            return titleBox;
        }

        /**
         * @return Photo Button
         */
        public ImageView getPhotoButton() {
            if (D) Log.d(TAG, "getPhotoButton called");
            ImageView photoButton = null;
            if (getContext() != null) {
                photoButton = (ImageView) layout
                        .findViewById(R.id.photoButton);
            }
            return photoButton;
        }

        /**
         * Accessor for Context
         */
        public Context getContext() {
            return context;
        }
}
