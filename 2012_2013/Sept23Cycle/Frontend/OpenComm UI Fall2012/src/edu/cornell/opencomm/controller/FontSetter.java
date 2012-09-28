/**
* Controller called by any UI activity. Takes care of setting font.
* It sets the font of all views and its children to the Design-specified font.
* 
* Example:
* inside an Activity's onCreate method:
* 
* FontSetter.applySanSerifFont(this, findViewById(R.id.login_layout));
*
* @author risa.naka[frontend]
* */

package edu.cornell.opencomm.controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontSetter {
	// Debugging
	private static final boolean D = false;
	
	// Logs
	private static String TAG = "FontSetter";
	
	// Design-specified fonts
	private static String serifFont = "Delicious-Roman.otf";
	private static String sanserifFont = "Delicious.otf";
	
	private static void applyFont(Context context, View root, String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(
                		Typeface.createFromAsset(context.getAssets(), fontName));
        } catch (Exception e) {
        	if (D) Log.e(TAG, String.format(
            		"Error occured when trying to apply %s font for %s view", 
            		fontName, root));
            e.printStackTrace();
        }
    }
	
	public static void applySerifFont(Context context, View root) {
		applyFont(context, root, serifFont);
	}
	
	public static void applySanSerifFont(Context context, View root) {
		applyFont(context, root, sanserifFont);
	}
}
