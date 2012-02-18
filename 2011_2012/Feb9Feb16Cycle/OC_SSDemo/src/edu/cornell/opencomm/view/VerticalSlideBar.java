package edu.cornell.opencomm.view;

import android.widget.SeekBar;
import edu.cornell.opencomm.Values;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class VerticalSlideBar extends SeekBar {

	//private OnSeekBarChangeListener myListener;

	public VerticalSlideBar(Context context) {
		super(context);
	}

	public VerticalSlideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalSlideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	/*@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
		this.myListener = mListener;
	}*/

	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-getHeight(), 0);

		super.onDraw(c);
	}

	public boolean onTouchEvent(MotionEvent event) {
		this.setProgress((int) (100 * (1 - event.getY() / this.getBottom())));
		return true;
	}

	private int x, y, z, w;

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
		this.x = w;
		this.y = h;
		this.z = oldw;
		this.w = oldh;
	}

	@Override
	public synchronized void setProgress(int progress) {

		super.setProgress(progress);

		onSizeChanged(x, y, z, w);

	}
	
    /*float xPos = event.getX();
    float yPos = event.getY();
    float progress = (yPos-this.getTop())/(this.getBottom()-this.getTop());
    int oOffset = this.getThumbOffset();
    int oProgress = this.getProgress();
    Log.d("offset" + System.nanoTime(), new Integer(oOffset).toString());
    Log.d("progress" + System.nanoTime(), new Integer(oProgress).toString());

    float offset;

    offset = progress * (this.getBottom()-this.getTop());

    this.setThumbOffset((int)offset);

    Log.d("offset_postsetprogress" + System.nanoTime(), new Integer(oOffset).toString());
    Log.d("progress_postsetprogress" + System.nanoTime(), new Integer(oProgress).toString());
*/
}