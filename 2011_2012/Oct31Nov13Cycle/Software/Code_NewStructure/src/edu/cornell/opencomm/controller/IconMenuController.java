package edu.cornell.opencomm.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.view.SpaceView;

public class IconMenuController {
	final static CharSequence[] items = {"Delete User", "Cancel"};
	
	private SpaceView spaceView = null;
	private static Context context;

	public IconMenuController(Context context, SpaceView spaceView) {
		this.spaceView = spaceView;
		this.context = context;
	}

	
	public static void showIconMenu(){
		if (context == null)
			Log.v("Icon Menu", "NULLL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
		    }
		});

		
		builder.setTitle( "Icon Menu" )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}
