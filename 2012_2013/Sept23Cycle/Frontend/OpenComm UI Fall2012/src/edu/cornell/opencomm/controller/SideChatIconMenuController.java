package edu.cornell.opencomm.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.util.Values;
import edu.cornell.opencomm.view.ConferenceView;
import edu.cornell.opencomm.view.PrivateSpaceIconView;



public class SideChatIconMenuController {


    private static Context context;

    public SideChatIconMenuController(Context context, PrivateSpaceIconView privateSpaceIconView) {
    }

    public void showSideChatMenu(){
        if (context == null)
            Log.v("Side Chat Icon Menu", "NULL");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //TODO: Ankit: Figure why the main context is required here
        final ConferenceView main= (ConferenceView)context;
        builder.setItems(Values.privatespaceiconMenu, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (Values.privatespaceiconMenu[item].equals("Go to")){
                    //Do something if user clicks on Go to
                    // Go to the space which this icon is representing
                }
                else if (Values.privatespaceiconMenu[item].equals("Leave")){
                    //Do something if user clicks on Leave Chat
                    // delete the icon and the room

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
