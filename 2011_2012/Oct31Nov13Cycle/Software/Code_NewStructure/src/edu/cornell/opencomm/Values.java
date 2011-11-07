package edu.cornell.opencomm;

import android.graphics.Bitmap;

public class Values {
	/** These values will be changed in MainApplication's adjustLayoutParams() */
	
	// Cellphone screen dimensions
	public static int screenW = 320;
	public static int screenH = 430;
	// Section heights
	public static int actionBarH = 72;
	public static int spaceViewH = 350;
	public static int bottomBarH = 70;
	// PrivateSpace buttons in bottom bar
	public static int privateSpaceButtonW = 94;
	public static int privateSpacePadding = 3;
	// User icon
	public static int userIconW = 80;
	public static int userIconH = 80;
	public static int iconTextH = userIconH/4;
	public static int iconTextPadding = 5;
	// Pop User Icon
	public static int popUserIconW = 42;
	public static int popUserIconH = 42;
	// Images
	public static int voice_image = R.drawable.yousmall;
	public static int icon_namebox = R.drawable.iconbox;
	// Buddylist
	public static CharSequence[] _options = {"Rahul", "Nora", "Najla","Risa","Justin","Crystal"};
	public static boolean[] _selections =  new boolean[_options.length];
	
	
}
