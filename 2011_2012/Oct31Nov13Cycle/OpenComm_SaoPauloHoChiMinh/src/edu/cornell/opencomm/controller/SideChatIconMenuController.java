package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SpaceView;

/** When a user press and hold a SideChat Icon, this controller
 *  is invoked. It displays a dialog menu.
 *  
 *  @author - rahularora
 */  

public class SideChatIconMenuController {
	
	static ContactListController contactListController;
	
	private PrivateSpaceIconView privateSpaceIconView = null;
	private static Context context;
	
	public SideChatIconMenuController(Context context, PrivateSpaceIconView privateSpaceIconView) {
		this.privateSpaceIconView = privateSpaceIconView;
		SideChatIconMenuController.context = context;
	}

	public static void showSideChatMenu(){
		if (context == null)
			Log.v("Side Chat Icon Menu", "NULL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(Values.privatespaceiconMenu, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (Values.privatespaceiconMenu[item].equals("Go to")){
		    		//Do something if user clicks on Go to 
		    	}
		    	else if (Values.privatespaceiconMenu[item].equals("Leave chat")){
		    		//Do something if user clicks on Leave Chat
		    	}
		    	else if (Values.privatespaceiconMenu[item].equals("Cancel")){
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
