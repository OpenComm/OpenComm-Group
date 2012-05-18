package edu.cornell.opencomm.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.ParticipantView;

/** When a user press and hold an empty space of the mainscreen, this controller
 *  is invoked. It displays a dialog menu.
 * 
 *  @author - rahularora
 */
public class EmptySpaceMenuController {

    protected static final String TAG = "EmptySpaceMenuController";

    static ContactListController contactListController;

    private static Context context;
    public EmptySpaceMenuController(Context context) {
        EmptySpaceMenuController.context = context;
        contactListController = new ContactListController(context);
    }

    public static void showFreeSpaceMenu(){
        if (context == null)
            Log.v("Empty Space Menu", "NULL");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(Values.emptyspaceMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
                if (Values.emptyspaceMenu[item].equals("Add Users")){
                    Log.v(TAG, "pressed Add Users");
                    ContactListController.showBuddyList();
                    Log.v(TAG, "pressed Add Users 2");
                }
                else if (Values.emptyspaceMenu[item].equals("Delete Users")){
                    Log.v(TAG, "pressed Delete Users");
                    ParticipantView.deleteParticipants();
                    MainApplication.screen.getSpace().getSpaceController().getPSIV().invalidate();
                }
                else if (Values.emptyspaceMenu[item].equals("Leave Conference")){
                    Log.v(TAG, "pressed Leave Chat");
                    MainApplication.screen.getSpace().getParticipantController()
                    .leaveSpace(MainApplication.screen.getSpace().equals(
                            Space.getMainSpace()));
                }
                else if (Values.emptyspaceMenu[item].equals("Cancel")){
                    Log.v(TAG, "pressed Cancel");
                    //Do nothing
                }
            }
        });

        Log.v(TAG, "showfreespacemenu() 1");

        builder.setTitle( "Empty Space Menu" )
        .create();
        Log.v(TAG, "showfreespacemenu() 2");
        AlertDialog alert = builder.create();
        Log.v(TAG, "showfreespacemenu() 3");
        alert.show();
        Log.v(TAG, "showfreespacemenu() 4");
    }


}
