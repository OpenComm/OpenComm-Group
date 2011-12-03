package edu.cornell.opencomm;

public class Values {
	/** These values will be changed in MainApplication's adjustLayoutParams() */
	
	//font
	public static String font = "Delicious-Roman.otf";
	
	// Time
	public static int pressAndHold = 200; // amount of time for a touch to be considered a long press
	
	// Menu options
	public static CharSequence[] userviewMenu = {"Delete User", "Cancel"}; // after longpressing a user icon
	public static CharSequence[] emptyspaceMenu = {"Add Users", "Delete Users", 
		"Leave Conference","Cancel"}; // after longpressing the empty space in a spaceview
	public static CharSequence[] privatespaceiconMenu = {"Go to", "Leave","Cancel"};
	//when chat owner decides to leave
	public static CharSequence[] whatHappen = {"Destroy chat", "Pass ownership"};
	
	// Cellphone screen dimensions
	public static int screenW = 480;
	public static int screenH = 800;
	
	// Section heights
	public static int actionBarH = 72; // Upper strip at top that says "OpenComm"
	public static int spaceViewH = 350; // Area where user icons are shown
	public static int bottomBarH = 138; // Bottom strip that holds private space icons
	public static int voice_imageSpaceH = 20; // Space between you_image and the bottom bar
	
	// PrivateSpace buttons in bottom bar
	public static int privateSpaceButtonW = 116;
	public static int privateSpacePadding = 3;
	public static int selectedBorder=2;
	public static int squarePadding=4;
	public static int iconBorderPaddingV = 11;
	public static int iconBorderPaddingH=11;
	public static int plusButtonLength=83;
	public static int plusButtonWidth=12;
		
	// User icon
	public static int userIconW = 100;
	public static int userIconH = 100;
	public static int iconTextH = 15;
	public static int iconBorderPadding=5;
	public static int nameTextSize=13;
	public static int adminTextSize=13;
	public static int textAdjust=2;
	public static int staggeredAddStart = 10;
	public static int adminBox=20;
		
	// Pop User Icon
	public static int popUserIconW = 42;
	public static int popUserIconH = 42;
	
	// Images
	public static int voice_image = R.drawable.yousmall;
	public static int icon_namebox = R.drawable.iconbox;
	
	public void setValues(int screenW, int screenH){
		
		// QVGA Small-Low  240 X 320
		if((screenW == 240) && (screenH == 320)){
			Values.screenW = 240;
			Values.screenH = 320;
			
			Values.actionBarH = 50;
			Values.bottomBarH = 50;
			Values.spaceViewH = Values.screenH - Values.bottomBarH - 35;
			Values.privateSpaceButtonW = Values.bottomBarH - 5;
			
			Values.userIconW = 50;
			Values.userIconH = 50;	
		}
		
		// WQVGA400 Normal-Low 240 X 400
		else if((screenW == 240) && (screenH == 400)){
			Values.screenW = 240;
			Values.screenH = 400;
			
			Values.bottomBarH = 50;
			Values.spaceViewH = Values.screenH - Values.bottomBarH - 35;
			Values.privateSpaceButtonW = Values.bottomBarH - 5;
			
			Values.userIconW = 50;
			Values.userIconH = 50;
		}
		
		// WQVGA432 Normal-Low 240 X 432
		else if((screenW == 240) && (screenH == 432)){
			Values.screenW = 240;
			Values.screenH = 432;
			
			Values.bottomBarH = 50;
			Values.spaceViewH = Values.screenH - Values.bottomBarH - 35;
			Values.privateSpaceButtonW = Values.bottomBarH - 5;
			
			Values.userIconW = 50;
			Values.userIconH = 50;
			
		}
		
		// HVGA 	Normal-Med 320 X 480
		else if((screenW == 320) && (screenH == 480)){
			Values.screenW = 320;
			Values.screenH = 480;

			Values.bottomBarH = 60;
			Values.spaceViewH = Values.screenH - Values.bottomBarH - 50;
			Values.privateSpaceButtonW = Values.bottomBarH - 5;
			
			Values.userIconW = 60;
			Values.userIconH = 60;
		}
		
		// WVGA800  Normal-Hi  480 X 854
		else if((screenW == 480) && (screenH == 854)){
			Values.screenW = 480;
			Values.screenH = 854;
			
			Values.spaceViewH = Values.screenH - Values.bottomBarH - 75;
			Values.privateSpaceButtonW = Values.bottomBarH - 5;
			
			Values.userIconW = 100;
			Values.userIconH = 100;
			
		}
		else if((screenW == 540) && (screenH == 960)){
			Values.screenW = 540;
			Values.screenH = 960;
			
			Values.spaceViewH = Values.screenH - Values.bottomBarH - 75;
			Values.privateSpaceButtonW = Values.bottomBarH - 5;
			
			Values.userIconW = 110;
			Values.userIconH = 110;
			
		}
		// WVGA800  Normal-Hi  480 X 800
		//else if((screenW == 480) && (screenH == 800)){
		else{
			Values.screenW = 480;
			Values.screenH = 800;
			Values.bottomBarH = 150;
			Values.spaceViewH = 545;
			Values.voice_imageSpaceH = 20;
			Values.userIconW = 100;
			Values.userIconH = 100;		
		}
	}
	
	
	// Buddylist
	public static CharSequence[] _options = {"Rahul", "Nora", "Najla","Risa","Justin","Crystal"};
	public static boolean[] _selections =  new boolean[_options.length];	
}