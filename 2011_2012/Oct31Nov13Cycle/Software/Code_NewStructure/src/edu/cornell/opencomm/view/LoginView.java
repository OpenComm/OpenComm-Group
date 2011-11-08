package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.layout;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConfirmationController;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.model.Space;

public class LoginView {
	private static String LOG_TAG = "OC_LoginView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private LoginController loginController = new LoginController(this);
	private View loginLayout = null;

	public LoginView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	private void initEventsAndProperties() {
		// create property loginLayout from infalter and store it as a
		// property
		if (inflater != null) {
			View loginViewFromInflater = inflater.inflate(
					R.layout.login_layout, null);
			if (loginViewFromInflater != null) {
				this.loginLayout = loginViewFromInflater;
			}
		}
		initializeLoginButtonClickedEvent();

	}

	private void initializeLoginButtonClickedEvent() {
		Button loginButton = getLoginButton();
		if (loginButton != null) {
			loginButton.setOnTouchListener(onLoginButtonClickedListener);
		}
	}

	public Button getLoginButton() {
		Button loginButton = null;
		if (loginLayout != null) {
			loginButton = (Button) loginLayout
					.findViewById(R.id.buttonAcceptConfirmation);
		}

		return loginButton;
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
		if (inflater != null && loginLayout != null) {
			window = new PopupWindow(loginLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(loginLayout, 0, 1, 1);
			loginLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch login view as inflater layout is null");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//loginController.handlePopupWindowClicked();
		}
	};

	private View.OnTouchListener onLoginButtonClickedListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// loginController.handleLoginButtonClick();
			return true;
		}
	};
}
