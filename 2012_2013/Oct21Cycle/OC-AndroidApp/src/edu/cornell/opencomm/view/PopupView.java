package edu.cornell.opencomm.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class PopupView {
	AlertDialog.Builder popupBuilder;
	AlertDialog popup;
	public PopupView(Context context) {
		popupBuilder = new AlertDialog.Builder(context);
	}
		
	public void createPopup(String title, String message){
		popupBuilder.setTitle(title);
		popupBuilder.setMessage(message);
	}
	public void createPositiveButton(String buttonMessage,OnClickListener listener){
		popupBuilder.setPositiveButton(buttonMessage, listener);
		
	}
	public void createNeutralButton(String buttonMessage,OnClickListener listener){
		popupBuilder.setNeutralButton(buttonMessage, listener);
		
	}
	public void createNegativeButton(String buttonMessage,OnClickListener listener){
		popupBuilder.setNegativeButton(buttonMessage, listener);
		
	}
	public void showPopup(){
	 popup = popupBuilder.create();
	 popup.show();

	}
	public void dissmissPopup(){
		popup.dismiss();
	}
}
