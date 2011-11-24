package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.MenuController;

public class MenuView {
	private static String LOG_TAG = "OC_MenuView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private MenuController menuController = new MenuController(
			this);
	private View menuLayout = null;
	private View parentView = null;

	public MenuView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	private void initEventsAndProperties() {
		// create property menuLayout from inflater and store it as a
		// property
		if (inflater != null) {
			View menuLayoutFromInflater = inflater.inflate(
					R.layout.menu, null);
			if (menuLayoutFromInflater != null) {
				this.menuLayout = menuLayoutFromInflater;
			}
		}
		initializeDeleteUserButtonHoverEvent();
		initializeAddUserButtonHoverEvent();
		initializeLeaveChatButtonHoverEvent();
		initializeSettingsButtonHoverEvent();
		initializeLogoutButtonHoverEvent();
		initializeMenuButtonHoverEvent();
		
	}
	
	public void initializeMenuButtonHoverEvent() {
		Log.d(LOG_TAG, "initializing menu button click");
		this.menuLayout.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction()!=KeyEvent.ACTION_DOWN) {
					return true;
				}
				switch (keyCode) {
					case KeyEvent.KEYCODE_MENU:{
						Log.d(LOG_TAG, "initialize: menuButton clicked");
						menuController.handleMenuButtonClicked();
						break;
					}
				}
				return true;
			}		
		});
	}
	
	private void initializeAddUserButtonHoverEvent() {
		Button addUserButton = getAddUserButton();
		if (addUserButton != null) {
			addUserButton.setOnClickListener(onAddUserButtonClickListener);
		}
	}

	private void initializeDeleteUserButtonHoverEvent() {
		Button deleteUserButton = getDeleteUserButton();
		if (deleteUserButton != null) {
			deleteUserButton.setOnClickListener(onDeleteUserButtonClickListener);
		}
	}
	
	private void initializeLeaveChatButtonHoverEvent() {
		Button leaveChatButton = getLeaveChatButton();
		if (leaveChatButton != null) {
			leaveChatButton.setOnClickListener(onLeaveChatButtonClickListener);
		}
	}
	
	private void initializeSettingsButtonHoverEvent() {
		Button settingsButton = getSettingsButton();
		if (settingsButton != null) {
			settingsButton.setOnClickListener(onSettingsButtonClickListener);
		}
	}
	
	private void initializeLogoutButtonHoverEvent() {
		Button logoutButton = getLogoutButton();
		if (logoutButton != null) {
			logoutButton.setOnClickListener(onLogoutButtonClickListener);
		}
	}

	public Button getAddUserButton() {
		Button addUserButton = null;
		if (menuLayout != null) {
			addUserButton = (Button) menuLayout
					.findViewById(R.id.menuAddUser);
		}

		return addUserButton;
	}
	
	public ImageView getAddUserOverlay() {
		ImageView addUserOverlay = null;
		if (menuLayout != null) {
			addUserOverlay = (ImageView) menuLayout
					.findViewById(R.id.menuAddUserOverlay);
		}

		return addUserOverlay;
	}

	public Button getDeleteUserButton() {
		Button deleteUserButton = null;
		if (menuLayout != null) {
			deleteUserButton = (Button) menuLayout
					.findViewById(R.id.menuDeleteUser);
		}

		return deleteUserButton;
	}
	
	
	public ImageView getDeleteUserOverlay() {
		ImageView deleteUserOverlay = null;
		if (menuLayout != null) {
			deleteUserOverlay = (ImageView) menuLayout
					.findViewById(R.id.menuDeleteUserOverlay);
		}

		return deleteUserOverlay;
	}
	
	public Button getSettingsButton() {
		Button settingsButton = null;
		if(menuLayout !=null){
			settingsButton = (Button) menuLayout
					.findViewById(R.id.menuSetting);
		}
		return settingsButton;
	}
	
	
	public ImageView getSettingsOverlay() {
		ImageView settingsOverlay = null;
		if (menuLayout != null) {
			settingsOverlay = (ImageView) menuLayout
					.findViewById(R.id.menuSettingOverlay);
		}

		return settingsOverlay;
	}
	
	public Button getLeaveChatButton() {
		Button leaveChatButton = null;
		if(menuLayout !=null) {
			leaveChatButton= (Button) menuLayout
					.findViewById(R.id.menuLeave);
		}
		return leaveChatButton;
	}
	
	public ImageView getLeaveChatOverlay() {
		ImageView leaveChatOverlay = null;
		if (menuLayout != null) {
			leaveChatOverlay = (ImageView) menuLayout
					.findViewById(R.id.menuLeaveOverlay);
		}

		return leaveChatOverlay;
	}
	
	public Button getLogoutButton() {
		Button logoutButton = null;
		if(menuLayout !=null) {
			logoutButton= (Button) menuLayout
					.findViewById(R.id.menuLogout);
		}
		return logoutButton;
	}
	
	public ImageView getLogoutOverlay() {
		ImageView logoutOverlay = null;
		if (menuLayout != null) {
			logoutOverlay = (ImageView) menuLayout
					.findViewById(R.id.menuLogoutOverlay);
		}

		return logoutOverlay;
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
	 * this method launches the confirmation layout on a popupwindow, can be
	 * changed later to launch like a normal view
	 */

	
	public void launch() {
		if (inflater != null && menuLayout != null) {
			window = new PopupWindow(menuLayout, Values.screenW, Values.screenH, true);
			window.showAtLocation(this.menuLayout, Gravity.BOTTOM, -10, -10);
			menuLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch menu view as inflater/menulayout is nul");
		}
	}


	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			menuController.handlePopupWindowClicked();
		}
	};

	private View.OnClickListener onAddUserButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			menuController.handleAddUserButtonHover();
		}
	};
	
	private View.OnClickListener onDeleteUserButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			menuController.handleDeleteUserButtonHover();
		}
	};
	
	private View.OnClickListener onLeaveChatButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			menuController.handleLeaveChatButtonHover();
		}
	};
	
	private View.OnClickListener onSettingsButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			menuController.handleSettingsButtonHover();
		}
	};
	
	private View.OnClickListener onLogoutButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			menuController.handleLogoutButtonHover();
		}
	};


}
