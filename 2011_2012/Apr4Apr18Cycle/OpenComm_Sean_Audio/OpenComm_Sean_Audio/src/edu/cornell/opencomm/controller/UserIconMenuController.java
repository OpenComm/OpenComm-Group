/** 
 * Controller called when a user press and hold an icon of the Main conference. 
 * It displays a dialog menu.
 * 
 * Issues [TODO]
 * - No notification on screen if user cannot be kicked out
 * - Do something if user clicks on Cancel
 * - For any other issues search for string "TODO"
 * 
 * @author rahularora[hcisec], vinaymaloo[ui]
 */

package edu.cornell.opencomm.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;


public class UserIconMenuController {
	
	private static UserView userView = null;
    private static SpaceView spaceView = null;
    private static Context context;
    private static boolean kickoutStatus;
    
    /** Constructor
     **/
    public UserIconMenuController(Context context, SpaceView spaceView) {
        this.spaceView = spaceView;
        UserIconMenuController.context = context;
    }

    /** 
     * Show a menu after long-pressing on a user icon (UserView)
     * Menu options are: Delete User, Cancel 
     * */
    public static void showIconMenu(UserView uv){
    	userView = uv;
        if (context == null){
            Log.v("User Icon Menu", "NULL");
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(Values.userviewMenu, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int item) {
   
            	/**
            	 * Event when User clicks on Delete User
            	 */
                if (Values.userviewMenu[item].equals("Delete User")){
                    /**
                   	 * Kick out the selected user
                   	 */
                	kickoutStatus = spaceView.getSpace().getKickoutController().kickoutUser(
                									userView.getPerson(),"Because you should leave");
                   
                	/**
                   	 * For dealing with unsuccessful kick-out
                   	 */
                	if  (!kickoutStatus){
                    	//TODO Some notification on screen if user cannot be kicked out
                
                    }
                }
                else if (Values.userviewMenu[item].equals("Cancel")){
                    //TODO Do something if user clicks on Cancel
                }
            }
        });
        builder.setTitle( "Icon Menu" ).create();
        AlertDialog alert = builder.create();
        alert.show();
    }

}
