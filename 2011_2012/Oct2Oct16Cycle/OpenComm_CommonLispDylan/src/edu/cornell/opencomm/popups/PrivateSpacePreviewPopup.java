package edu.cornell.opencomm.popups;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.view.PersonView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PrivateSpacePreviewPopup extends LinearLayout{
	private Context context;
	private ArrayList<PersonView> personViews = new ArrayList<PersonView>();
	
	public PrivateSpacePreviewPopup(Context context, AttributeSet attrs) {
		super(context, attrs);
    	this.context = context;
    	setFocusable(true);
    	setFocusableInTouchMode(true);
	}


	public void setContext(Context context) {
		this.context = context;
	}



	public ArrayList<PersonView> getPersonViews() {
		return personViews;
	}



	public void setPersonViews(ArrayList<PersonView> personViews) {
		int k = 0;
		ArrayList<PersonView> newPersonViews = new ArrayList<PersonView>();
		for(PersonView personView : personViews) {
			PersonView newPersonView = new PersonView(personView.getContext(), personView.getImage(), this.getWidth() + k, this.getHeight());
			k += 44;
			newPersonViews.add(newPersonView);
		}
		
		this.personViews = newPersonViews;
	}



	/* Draw everything on the screen including:
     * 1) Background color
     * 2) All icons (need loop, call the icon's draw function in class PersonView)
     * 3) Preview (if not null), need to get icons from the SpaceView and draw them
     */
    protected void onDraw(Canvas canvas){
   	 //(1)
   	 canvas.drawColor(getColor());
   	 //(2)
   	 for (PersonView p : personViews)
   		 if(p.getPerson()!=MainApplication.user_you)
   			 p.draw(canvas);
    }
    
    /* Get the main background color */
    private int getColor(){
   	 return R.color.main_background;
    }
    
    
}
