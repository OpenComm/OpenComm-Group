
	package edu.cornell.opencomm.view;

	import android.app.Activity;
	import android.content.Context;
	import android.graphics.Typeface;
	import android.util.Log;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.widget.Button;
	import android.widget.EditText;
	import android.widget.ImageButton;
	import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
	import edu.cornell.opencomm.R;
	import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ResetPasswordController;

	public class ResetPasswordView {
	    private static String LOG_TAG = "View.ResetPassword"; // for error checking
	    private Context context;
	    private LayoutInflater inflater = null;
	    // private PopupWindow window = null;
	    private ResetPasswordController resetPasswordController = null;
	    private View resetPasswordLayout;
	    private PopupWindow window;

	    /* Debugging
		private static final String TAG = "View.ResetPassword";
		private static final boolean D = true;*/

	    //font
	    private Typeface font;

	    // Layout Views not sure if needed..
	    //private static EditText resetUsername;
	    private static Button  resetText;
	    private static Button signUp;
	    private static ImageButton resetButton;
	    private static ImageView loginOverlay;

	    public ResetPasswordView(LayoutInflater inflater) {
	        this.inflater = inflater;
	        this.resetPasswordController =  new ResetPasswordController(
	                this);
	        initEventsAndProperties();

	    } 
	    
	    
	    public void launch() {
	        if (inflater != null && resetPasswordLayout != null) {
	            window = new PopupWindow(resetPasswordLayout, Values.screenW,
	                    Values.screenH, true);
	            window.showAtLocation(resetPasswordLayout, 0, 1, 1);
	        } else {
	            Log.v(LOG_TAG,
	                    "Cannot launch invitation view as inflater/confirmation layout is nul");
	        }
	    } // end launch
//Don't think i need oncreate, but here justincase
	    //public void onCreate(Bundle savedInstanceState) {} // end onCreate method

	    
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
	       Button resetTextButton = getResetTextButton();
	       TextView signUpTextButton = getSignUpTextButton();
	        if (resetButton != null) {
	            resetButton.setOnClickListener(onResetButtonClickedListener);
	        }
	        if (resetTextButton != null) {
	            resetTextButton.setOnClickListener(onResetButtonClickedListener);
	        }
	        if (signUpTextButton != null) {
	            signUpTextButton.setOnClickListener(onSignUpButtonClickedListener);
	        }
	    }

	    private TextView getSignUpTextButton() {
			TextView signUpButton = null;
			if (resetPasswordLayout != null) {
				signUpButton = (TextView) resetPasswordLayout.
						findViewById(R.id.signUpButton);
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

		public EditText getResetUsername(){
			EditText resetUsername = null;
			if (resetPasswordLayout != null) {
		 	resetUsername = (EditText) resetPasswordLayout
					.findViewById(R.id.editTextResetUsername);
			}
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

	    public ImageView getLoginOverlay() {
	        return loginOverlay;
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

	

