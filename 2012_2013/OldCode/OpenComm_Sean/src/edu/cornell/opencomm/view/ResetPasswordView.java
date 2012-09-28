package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ResetPasswordController;

/* An instance of a reset password view, allowing users to reset or sign up with whichever username (their email) they desire. 
 Just a heads-up, this view is called as a popup window

 To call it :
 1. inflate by creating an instance of Layoutinflater in whichever view
 2. pass this instance in the ResetPasswordView controller
 3. use launch() to popup view.
 */
public class ResetPasswordView {
	private static String LOG_TAG = "View.ResetPassword"; // for error checking
	private Context context;
	private LayoutInflater inflater = null;
	// private PopupWindow window = null;
	private ResetPasswordController resetPasswordController = null;
	private View resetPasswordLayout;
	private PopupWindow window;

	/*
	 * Debugging private static final String TAG = "View.ResetPassword"; private
	 * static final boolean D = true;
	 */

	// font
	private Typeface font;

	// Layout Views not sure if needed..
	// private static EditText resetUsername;
	// private static Button resetText;
	// private static Button signUp;
	// private static ImageButton resetButton;
	// private static ImageView loginOverlay;

	public ImageView resetButtonOverylay;
	public ImageView signUpButtonOverlay;

	public ResetPasswordView(LayoutInflater inflater, Context context) {
		setContext(context);
		this.inflater = inflater;
		this.resetPasswordController = new ResetPasswordController(this);
		initEventsAndProperties();

	}

	public void launch() {
		if (inflater != null && resetPasswordLayout != null) {
			window = new PopupWindow(resetPasswordLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(resetPasswordLayout, 0, 1, 1);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch resetpassword view as inflater/confirmation layout is nul");
		}
	} // end launch
	// Don't think i need oncreate, but here justincase
	// public void onCreate(Bundle savedInstanceState) {} // end onCreate method

	private void initEventsAndProperties() {
		if (inflater != null) {
			View invitationLayoutFromInflater = inflater.inflate(
					R.layout.reset_password, null);
			if (invitationLayoutFromInflater != null) {
				this.resetPasswordLayout = invitationLayoutFromInflater;
			}
		}
		initializButtonClickedEvent();
	} // end initEventsAndProperties

	private void initializButtonClickedEvent() {
		ImageButton resetButton = getResetButton();
		resetButtonOverylay = getResetOverlay();
		signUpButtonOverlay = getSignUpOverlay();
		Button resetTextButton = getResetTextButton();
		TextView signUpTextButton = getSignUpTextButton();
		final EditText resetUsernameText = getResetUsername();
		if (resetButton != null) {
			resetButton.setOnClickListener(onResetButtonClickedListener);
		}
		if (resetTextButton != null) {
			resetTextButton.setOnClickListener(onResetButtonClickedListener);
		}
		if (signUpTextButton != null) {
			signUpTextButton.setOnClickListener(onSignUpButtonClickedListener);
		}
		if (resetUsernameText != null) {
			resetUsernameText
					.setOnFocusChangeListener(new OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							Log.d(LOG_TAG,
									"Setting listeners for onfocusChange");
							if (!hasFocus) {
								Log.d(LOG_TAG,
										"Not focused, trying to set text");
								// resetUsernameText.setText("WTF");
								resetPasswordController
										.handleTextChange(resetUsernameText
												.getEditableText());
							}
						}
					});
		}
	}

	private TextView getSignUpTextButton() {
		TextView signUpButton = null;
		if (resetPasswordLayout != null) {
			signUpButton = (TextView) resetPasswordLayout
					.findViewById(R.id.signUpButton);
		}

		return signUpButton;
	}

	private Button getResetTextButton() {
		Button resetTextButton = null;
		if (resetPasswordLayout != null) {
			resetTextButton = (Button) resetPasswordLayout
					.findViewById(R.id.resetTextButton);
		}

		return resetTextButton;
	}

	public EditText getResetUsername() {
		if (resetPasswordLayout == null) {
			return null;
		}
		EditText resetUsername = (EditText) resetPasswordLayout
				.findViewById(R.id.editTextResetUsername);

		// this.resetUsername = resetUsername;
		return resetUsername;

	}

	public ImageButton getResetButton() {
		ImageButton resetButton = null;
		if (resetPasswordLayout != null) {
			resetButton = (ImageButton) resetPasswordLayout
					.findViewById(R.id.resetButton);
		}

		return resetButton;
	}

	public ImageView getResetOverlay() {
		ImageView resetOverlay = null;
		if (resetPasswordLayout != null) {
			resetOverlay = (ImageView) resetPasswordLayout
					.findViewById(R.id.resetPWOverlay);
		}

		return resetOverlay;

	}

	public ImageView getSignUpOverlay() {
		ImageView signUpOverlay = null;
		if (resetPasswordLayout != null) {
			signUpOverlay = (ImageView) resetPasswordLayout
					.findViewById(R.id.signUpAccOverlay);
			Log.v(LOG_TAG, ("signupOverlay: " + signUpOverlay == null) + "");
		}

		return signUpOverlay;

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

	private View.OnClickListener onResetButtonClickedListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			resetPasswordController.handleResetButtonClick();
		}
	};

	private View.OnClickListener onSignUpButtonClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			resetPasswordController.handleSignUpButtonClick();
		}
	};

	public PopupWindow getWindow() {
		return window;
	}

}
