package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
		
	}

	private void initializeAddUserButtonHoverEvent() {
		Button addUserButton = getAddUserButton();
		if (addUserButton != null) {
			addUserButton.setOnTouchListener(onAddUserButtonTouchListener);
		}
	}

	private void initializeDeleteUserButtonHoverEvent() {
		Button deleteUserButton = getDeleteUserButton();
		if (deleteUserButton != null) {
			deleteUserButton.setOnTouchListener(onDeleteUserButtonTouchListener);
		}
	}
	
	private void initializeLeaveChatButtonHoverEvent() {
		Button leaveChatButton = getLeaveChatButton();
		if (leaveChatButton != null) {
			leaveChatButton.setOnTouchListener(onLeaveChatButtonTouchListener);
		}
	}
	
	private void initializeSettingsButtonHoverEvent() {
		Button settingsButton = getSettingsButton();
		if (settingsButton != null) {
			settingsButton.setOnTouchListener(onSettingsButtonTouchListener);
		}
	}

	public Button getAddUserButton() {
		Button addUserButton = null;
		if (menuLayout != null) {
			addUserButton = (Button) menuLayout
					.findViewById(R.id.adduser);
		}

		return addUserButton;
	}

	public Button getDeleteUserButton() {
		Button deleteUserButton = null;
		if (menuLayout != null) {
			deleteUserButton = (Button) menuLayout
					.findViewById(R.id.deleteuser);
		}

		return deleteUserButton;
	}
	
	public Button getSettingsButton() {
		Button settingsButton = null;
		if(menuLayout !=null){
			settingsButton = (Button) menuLayout
					.findViewById(R.id.button3);
		}
		return settingsButton;
	}
	
	public Button getLeaveChatButton() {
		Button leaveChatButton = null;
		if(menuLayout !=null) {
			leaveChatButton= (Button) menuLayout
					.findViewById(R.id.leavechat);
		}
		return leaveChatButton;
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
			window = new PopupWindow(menuLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(menuLayout, 0, 1, 1);
			menuLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch menu view as inflater/menulayout is null");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			menuController.handlePopupWindowClicked();
		}
	};

	private View.OnTouchListener onAddUserButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			menuController.handleAddUserButtonHover();
			return true;
		}
	};
	
	private View.OnTouchListener onDeleteUserButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			menuController.handleDeleteUserButtonHover();
			return true;
		}
	};
	
	private View.OnTouchListener onLeaveChatButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			menuController.handleLeaveChatButtonHover();
			return true;
		}
	};
	
	private View.OnTouchListener onSettingsButtonTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			menuController.handleSettingsButtonHover();
			return true;
		}
	};

}
