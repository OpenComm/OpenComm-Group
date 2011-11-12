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
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.SpaceView;

/** When a user press and hold an empty space of the mainscreen, this controller
 *  is invoked. It displays a dialog menu.
 *  
 *  @author - rahularora
 */   
public class EmptySpaceMenuController {
  
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
			Log.v("Empty Space Menu", "NULLL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(Values.emptyspaceMenu, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        //Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
		    	if (Values.emptyspaceMenu[item].equals("Add Users")){
		    		contactListController.showBuddyList();
		    	}
		    	else if (Values.emptyspaceMenu[item].equals("Delete Users")){
		    		//Do something if user clicks on Delete Users
		    	}
		    	else if (Values.emptyspaceMenu[item].equals("Leave Chat")){
		    		//Do something if user clicks on Leave Chat
		    	}
		    	else if (Values.emptyspaceMenu[item].equals("Cancel")){
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
