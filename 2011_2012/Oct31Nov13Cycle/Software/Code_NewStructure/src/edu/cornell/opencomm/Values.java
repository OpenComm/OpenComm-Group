package edu.cornell.opencomm;

public class Values {
	/** These values will be changed in MainApplication's adjustLayoutParams() */
	
	// Time
	public static int pressAndHold = 200;
	
	// Cellphone screen dimensions
	public static int screenW = 320;
	public static int screenH = 430;
	
	// Section heights
	public static int actionBarH = 72;
	public static int spaceViewH = 350;
	public static int bottomBarH = 138;
	
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
	public static int iconTextH = userIconH/5;
	public static int iconBorderPadding=5;
	public static int adminTextSize=13;
	public static int textAdjust=2;
		
	// Pop User Icon
	public static int popUserIconW = 42;
	public static int popUserIconH = 42;
	
	// Images
	public static int voice_image = R.drawable.yousmall;
	public static int icon_namebox = R.drawable.iconbox;
	
	public void setValues(int screenW, int screenH){
		
		// QVGA Small-Low  240 X 320
		if((screenW == 240) && (screenH == 320)){
			this.screenW = 240;
			this.screenH = 320;
			
			this.actionBarH = 50;
			this.bottomBarH = 50;
			this.spaceViewH = this.screenH - this.bottomBarH - 35;
			this.privateSpaceButtonW = this.bottomBarH - 5;
			
			this.userIconW = 50;
			this.userIconH = 50;	
		}
		
		// WQVGA400 Normal-Low 240 X 400
		else if((screenW == 240) && (screenH == 400)){
			this.screenW = 240;
			this.screenH = 400;
			
			this.bottomBarH = 50;
			this.spaceViewH = this.screenH - this.bottomBarH - 35;
			this.privateSpaceButtonW = this.bottomBarH - 5;
			
			this.userIconW = 50;
			this.userIconH = 50;
		}
		
		// WQVGA432 Normal-Low 240 X 432
		else if((screenW == 240) && (screenH == 432)){
			this.screenW = 240;
			this.screenH = 432;
			
			this.bottomBarH = 50;
			this.spaceViewH = this.screenH - this.bottomBarH - 35;
			this.privateSpaceButtonW = this.bottomBarH - 5;
			
			this.userIconW = 50;
			this.userIconH = 50;
			
		}
		
		// HVGA 	Normal-Med 320 X 480
		else if((screenW == 320) && (screenH == 480)){
			this.screenW = 320;
			this.screenH = 480;

			this.bottomBarH = 60;
			this.spaceViewH = this.screenH - this.bottomBarH - 50;
			this.privateSpaceButtonW = this.bottomBarH - 5;
			
			this.userIconW = 60;
			this.userIconH = 60;
		}
		
		// WVGA800  Normal-Hi  480 X 800
		else if((screenW == 480) && (screenH == 800)){
			this.screenW = 480;
			this.screenH = 800;
			
			this.spaceViewH = this.screenH - this.bottomBarH - 87;
			this.privateSpaceButtonW = this.privateSpaceButtonW;
			
			this.userIconW = 100;
			this.userIconH = 100;

		}
		
		// WVGA800  Normal-Hi  480 X 854
		else if((screenW == 480) && (screenH == 854)){
			this.screenW = 480;
			this.screenH = 854;
			
			this.spaceViewH = this.screenH - this.bottomBarH - 75;
			this.privateSpaceButtonW = this.bottomBarH - 5;
			
			this.userIconW = 100;
			this.userIconH = 100;
			
		}
	}
	
	// Buddylist
	public static CharSequence[] _options = {"Rahul", "Nora", "Najla","Risa","Justin","Crystal"};
	public static boolean[] _selections =  new boolean[_options.length];	
}