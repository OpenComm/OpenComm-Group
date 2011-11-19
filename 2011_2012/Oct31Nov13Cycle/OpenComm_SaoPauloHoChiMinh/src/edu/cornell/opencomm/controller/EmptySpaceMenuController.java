package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.ParticipantView;
import edu.cornell.opencomm.view.SpaceView;

/** When a user press and hold an empty space of the mainscreen, this controller
 *  is invoked. It displays a dialog menu.
 *  
 *  @author - rahularora
 */   
public class EmptySpaceMenuController {
  
	protected static final String TAG = "EmptySpaceMenuController";

	static ContactListController contactListController;
	
	private static SpaceView spaceView = null;
	private static Context context;
	public EmptySpaceMenuController(Context context, SpaceView spaceView) {
		EmptySpaceMenuController.spaceView = spaceView;
		EmptySpaceMenuController.context = context;
		contactListController = new ContactListController(context, spaceView);
	}

	public static void showFreeSpaceMenu(){
		if (context == null)
			Log.v("Empty Space Menu", "NULL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(Values.emptyspaceMenu, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				//Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
				if (Values.emptyspaceMenu[item].equals("Add Users")){
					Log.v(TAG, "pressed Add Users");
					ContactListController.showBuddyList();
				}
				else if (Values.emptyspaceMenu[item].equals("Delete Users")){
					Log.v(TAG, "pressed Delete Users");
					ParticipantView.deleteParticipants();
				}
		    	else if (Values.emptyspaceMenu[item].equals("Leave Chat")){
		    		Log.v(TAG, "pressed Leave Chat");
		    		if(MainApplication.user_primary.equals(
		    				spaceView.getSpace().getOwner())){
		    			ParticipantView.whatHappen();
		    		}
		    		else{
		    			spaceView.getSpace().getParticipantController().leaveSpace(
		    					spaceView.getSpace().equals(Space.getMainSpace()));
		    		}
		    	}
		    	else if (Values.emptyspaceMenu[item].equals("Cancel")){
		    		Log.v(TAG, "pressed Cancel");
		    		//Do nothing
		    	}
		    }
		});

		
		builder.setTitle( "Empty Space Menu" )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}

	 
}
