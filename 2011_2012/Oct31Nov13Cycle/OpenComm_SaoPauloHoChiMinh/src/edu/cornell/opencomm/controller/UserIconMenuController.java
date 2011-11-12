package edu.cornell.opencomm.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.SpaceView;

/** When a user press and hold an icon of the mainscreen, this controller
 *  is invoked. It displays a dialog menu.
 *  
 *  @author - rahularora
 */  
public class UserIconMenuController {

	
	private SpaceView spaceView = null;
	private static Context context;

	/** Constructor: UserIconMenuController */
	public UserIconMenuController(Context context, SpaceView spaceView) {
		this.spaceView = spaceView;
		UserIconMenuController.context = context;
	}

	/** Show a menu after long-pressing on a user icon (UserView)
	 * Menu options are: Delete User, Cancel */
	public static void showIconMenu(){
		if (context == null)
			Log.v("User Icon Menu", "NULLL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(Values.userviewMenu, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (Values.userviewMenu[item].equals("Delete User")){
		    		//Do something if user clicks on Delete User
		    	}
		    	else if (Values.userviewMenu[item].equals("Cancel")){
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
