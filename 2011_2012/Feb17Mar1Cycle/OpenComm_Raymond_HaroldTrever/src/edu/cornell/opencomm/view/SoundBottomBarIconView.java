package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageButton;

public class SoundBottomBarIconView extends ImageButton {

	public SoundBottomBarIconView(Context context) {
		super(context);
	}
	public void draw(Canvas canvas){
        super.onDraw(canvas);
        int backgroundColor = (getResources().getColor(R.color.dark_grey)); 
        canvas.drawColor(backgroundColor);
	}

	}
