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

public class FreeSpaceMenuController {
  
	final static CharSequence[] items = {"Add Users", "Delete Users", "Spatialize","Ping",
		"Leave chat/conference","Cancel"};
	
	private SpaceView spaceView = null;
	private static Context context;
	public FreeSpaceMenuController(Context context, SpaceView spaceView) {
		this.spaceView = spaceView;
		this.context = context;
	}

	
	public static void showFreeSpaceMenu(){
		if (context == null)
			Log.v("ShowBUddyLIst", "NULLL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
		    }
		});

		
		builder.setTitle( "Buddylist" )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}

	 
}
