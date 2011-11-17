package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

/** When a user press and hold an icon of the mainscreen, this controller
 *  is invoked. It displays a dialog menu.
 *  
 *  @author - rahularora
 */  
public class UserIconMenuController {

	private static UserView userView = null;
	private static SpaceView spaceView = null;
	private static Context context;

	/** Constructor: UserIconMenuController */
	public UserIconMenuController(Context context, SpaceView spaceView) {
		this.spaceView = spaceView;
		UserIconMenuController.context = context;
	}

	/** Show a menu after long-pressing on a user icon (UserView)
	 * Menu options are: Delete User, Cancel */
	public static void showIconMenu(UserView uv){
		userView = uv;
		if (context == null)
			Log.v("User Icon Menu", "NULLL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(Values.userviewMenu, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (Values.userviewMenu[item].equals("Delete User")){
		    		//Do something if user clicks on Delete User
		    		try{
		    		spaceView.getSpace().getKickoutController().kickoutUser(userView.getPerson(), "Because you should leave");
		    		} catch(XMPPException e){
		    			Log.d("UserIconMenuController", "Couldn't kick out user " + userView.getPerson() + " from space " 
		    					+ spaceView.getSpace().getRoomID());
		    		}
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
