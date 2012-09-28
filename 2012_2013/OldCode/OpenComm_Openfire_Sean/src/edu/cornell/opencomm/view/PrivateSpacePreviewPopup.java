package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.MainApplication;

public class PrivateSpacePreviewPopup extends LinearLayout{
	//private Context context;
	private ArrayList<UserView> personViews = new ArrayList<UserView>();
	
	public PrivateSpacePreviewPopup(Context context, AttributeSet attrs) {
		super(context, attrs);
    	//setContext(context);
    	setFocusable(true);
    	setFocusableInTouchMode(true);
	}


	public void setContext(Context context) {
		//this.context = context;
	}



	public ArrayList<UserView> getPersonViews() {
		return personViews;
	}



	public void setPersonViews(ArrayList<UserView> personViews) {
		int k = 0;
		ArrayList<UserView> newPersonViews = new ArrayList<UserView>();
		for(UserView personView : personViews) {
			UserView newPersonView = new UserView(personView.getContext(), personView.getImage(), this.getWidth() + k, this.getHeight());
			k += 44;
			newPersonViews.add(newPersonView);
		}
		
		this.personViews = newPersonViews;
	}



	/* Draw everything on the screen including:
     * 1) Background color
     * 2) All icons (need loop, call the icon's draw function in class UserView)
     * 3) Preview (if not null), need to get icons from the SpaceView and draw them
     */
    protected void onDraw(Canvas canvas){
   	 //(1)
   	 canvas.drawColor(getColor());
   	 //(2)
   	 for (UserView p : personViews)
   		 if(p.getPerson()!=MainApplication.user_primary)
   			 p.draw(canvas);
    }
    
    /* Get the main background color */
    private int getColor(){
   	 return R.color.main_background;
    }
    
}
