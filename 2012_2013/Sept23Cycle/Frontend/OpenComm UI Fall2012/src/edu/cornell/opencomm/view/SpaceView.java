package edu.cornell.opencomm.view;

import edu.cornell.opencomm.model.Space;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class SpaceView extends View{
	Context context;
	Space space;
	public SpaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public SpaceView(Context context, Space space) {
		super(context);
		this.context = context;
		this.space = space;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	public void setSpace(Space mainSpace) {
		// TODO Store the parent view this space exist in
		
	}
	

}