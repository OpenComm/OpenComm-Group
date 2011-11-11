package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SpaceView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/** When a user press and hold a SideChat Icon, this controller
 *  is invoked. It displays a dialog menu.
 *  
 *  @author - rahularora
 */  

public class SideChatIconMenuController {

	final static CharSequence[] items = {"Go to", "Leave chat","Cancel"};
	
	static ContactListController contactListController;
	
	private PrivateSpaceIconView privateSpaceIconView = null;
	private static Context context;
	
	public SideChatIconMenuController(Context context, PrivateSpaceIconView privateSpaceIconView) {
		this.privateSpaceIconView = privateSpaceIconView;
		this.context = context;
	}

	public static void showSideChatMenu(){
		if (context == null)
			Log.v("Side Chat Icon Menu", "NULL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (items[item].equals("Go to")){
		    		//Do something if user clicks on Go to 
		    	}
		    	else if (items[item].equals("Leave chat")){
		    		//Do something if user clicks on Leave Chat
		    	}
		    	else if (items[item].equals("Cancel")){
		    		//Do something if user clicks on Cancel
		    	}
		    }
		});

		
		builder.setTitle( "Side Menu" )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}

	 

}
