package edu.cornell.opencomm.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.view.SpaceView;

/** When a user press and hold an icon of the mainscreen, this controller
 *  is invoked. It displays a dialog menu.
 *  
 *  @author - rahularora
 */  
public class UserIconMenuController {
	final static CharSequence[] items = {"Delete User", "Cancel"};
	
	private SpaceView spaceView = null;
	private static Context context;

	public UserIconMenuController(Context context, SpaceView spaceView) {
		this.spaceView = spaceView;
		this.context = context;
	}

	
	public static void showIconMenu(){
		if (context == null)
			Log.v("User Icon Menu", "NULLL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (items[item].equals("Delete User")){
		    		//Do something if user clicks on Delete User
		    	}
		    	else if (items[item].equals("Cancel")){
		    		//Do something if user clicks on Cancel
		    	}
		    }
		});

		
		builder.setTitle( "Icon Menu" )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}
