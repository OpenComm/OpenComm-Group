/*package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.model.Space;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class SpaceView extends View{
	Context context;
	Space space;
	private UserView ghost;
	private ArrayList<ArrayList<Point>> dragPoints = new ArrayList<ArrayList<Point>>();
	public UserView selectedIcon;
	
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
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw (Canvas canvas){
		if (canvas != null && space != null && space.getAllIcons() != null) {
			for (UserView p : space.getAllIcons()) {
				//if (!p.getPerson().getNickname()
				//		.equals(MainApplication.userPrimary.getNickname())) {
					p.draw(canvas);
				//}

			}
			if (ghost != null)
				ghost.draw(canvas);
		}
		// [FIXIT] Compiler warning: don't allocate objects during draw methods
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		for (ArrayList<Point> pointList : dragPoints) {
			for (int i = 0; i < pointList.size() - 1; i++) {
				Point p1 = pointList.get(i);
				Point p2 = pointList.get(i + 1);
				canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void performGhostDrag(int mouseX, int mouseY) {
		if (ghost == null) {
			ghost = selectedIcon.getGhost();
		}
	}
	

}*/