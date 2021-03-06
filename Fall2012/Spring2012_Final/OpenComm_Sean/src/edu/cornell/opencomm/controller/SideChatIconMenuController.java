package edu.cornell.opencomm.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.PrivateSpaceIconView;

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

    public void showSideChatMenu(){
        if (context == null)
            Log.v("Side Chat Icon Menu", "NULL");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final MainApplication main= (MainApplication)context;
        builder.setItems(Values.privatespaceiconMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (Values.privatespaceiconMenu[item].equals("Go to")){
                    //Do something if user clicks on Go to
                    // Go to the space which this icon is representing
                    MainApplication.screen.getSpaceViewController().changeSpace(privateSpaceIconView.getSpace());
                }
                else if (Values.privatespaceiconMenu[item].equals("Leave")){
                    //Do something if user clicks on Leave Chat
                    // delete the icon and the room
                    Space space = privateSpaceIconView.getSpace();
                    space.getParticipantController().leaveSpace(space==Space.getMainSpace());
                    //main.delPrivateSpaceButton(privateSpaceIconView);
                    //MainApplication.screen.getSpaceViewController().changeSpace(Space.getMainSpace());

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
