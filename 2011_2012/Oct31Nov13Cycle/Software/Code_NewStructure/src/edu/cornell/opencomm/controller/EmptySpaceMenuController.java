package edu.cornell.opencomm.controller;

import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.SpaceView;

public class EmptySpaceMenuController {
  
	final static CharSequence[] items = {"Add Users", "Delete Users", 
		"Leave chat","Cancel"};
	
	static ContactListController contactListController;
	
	private SpaceView spaceView = null;
	private static Context context;
	public EmptySpaceMenuController(Context context, SpaceView spaceView) {
		this.spaceView = spaceView;
		this.context = context;
		contactListController = new ContactListController(context, spaceView);
	}

	public static void showFreeSpaceMenu(){
		if (context == null)
			Log.v("Free Space Menu", "NULLL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        //Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
		    	if (items[item].equals("Add Users")){
		    		contactListController.showBuddyList();
		    	}
		    	if (items[item].equals("Delete Users")){
		    		//Do something if user clicks on Delete Users
		    	}
		    	if (items[item].equals("Leave Chat")){
		    		//Do something if user clicks on Leave Chat
		    	}if (items[item].equals("Cancel")){
		    		//Do something if user clicks on Cancel
		    	}
		    }
		});

		
		builder.setTitle( "Empty Space Menu" )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}

	 
}
