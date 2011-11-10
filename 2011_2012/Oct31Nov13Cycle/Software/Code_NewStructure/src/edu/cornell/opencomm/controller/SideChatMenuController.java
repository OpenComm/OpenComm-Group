package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SpaceView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class SideChatMenuController {

	final static CharSequence[] items = {"Go to", "Leave chat","Cancel"};
	
	static ContactListController contactListController;
	
	private PrivateSpaceIconView privateSpaceIconView = null;
	private static Context context;
	
	public SideChatMenuController(Context context, PrivateSpaceIconView privateSpaceIconView) {
		this.privateSpaceIconView = privateSpaceIconView;
		this.context = context;
	}

	public static void showSideChatMenu(){
		if (context == null)
			Log.v("Free Space Menu", "NULL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        
		    }
		});

		
		builder.setTitle( "Side Menu" )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}

	 

}
