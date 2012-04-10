package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
            initEventsAndProperties();
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
            initializeSaveButtonClickEvent();
            initializeCancelButtonClickEvent();
        }

        public void initializeSaveButtonClickEvent() {
            View saveButton = getSaveButton();
            if (saveButton != null) {
                saveButton.setOnClickListener(getSaveButtonHandler());
            }
        }


        public void initializeCancelButtonClickEvent() {
            View cancelButton = getCancelButton();
            if (cancelButton != null) {
                cancelButton.setOnClickListener(getCancelButtonHandler());
            }
        }

        public void initializePhotoButtonClickEvent() {

        }

        private View.OnClickListener getSaveButtonHandler() {
            return new View.OnClickListener() {

                
                public void onClick(View v) {
                    controller.handleSaveButtonClick();
                }
            };
        }

        private View.OnClickListener getCancelButtonHandler() {
            return new View.OnClickListener() {
                
                public void onClick(View v) {
                    controller.handleCancelButtonClick();
                }
            };
        }

        /**
         * @return Save Button View
         */
        public View getSaveButton() {
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
        public EditText getlastNameBox() {
            EditText lastNameBox = null;
            if (getContext() != null) {
                lastNameBox = (EditText) layout
                        .findViewById(R.id.lastNameBox);
            }
            return lastNameBox;
        }

        /**
         * @return Title Name Box
         */
        public EditText getTitleBox() {
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
