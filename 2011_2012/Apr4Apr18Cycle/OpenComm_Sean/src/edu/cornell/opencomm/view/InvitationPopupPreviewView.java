package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.MainApplication;

public class InvitationPopupPreviewView extends LinearLayout {
	private PrivateSpaceIconView psiv; // The Icon this popup is associated with
	private LinearLayout layout; // The layout representing the popup
	private PopupWindow popup; // The window containing the popup

	private static String TAG = "InvitationPopupPreviewView"; // Debug Tag

	private boolean D = false; // Debug Mode

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - MainApplication
	 * @param personViews
	 *            - list of UserViews for people in the space
	 */
	public InvitationPopupPreviewView(Context context, PrivateSpaceIconView psiv) {
		super(context);

		this.psiv = psiv;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = (LinearLayout) inflater.inflate(
				R.layout.invitation_popup_preview_layout, this);

		LinearLayout scroll = (LinearLayout) layout
				.findViewById(R.id.preview_icon_bar);

		final int USER_ICON_DIMENSION = 90;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				USER_ICON_DIMENSION, USER_ICON_DIMENSION);

		// Populate view with user icons except for yourself
		for (UserView view : psiv.space.getAllIcons()) {
			if (D)
				Log.d(TAG, "Adding View");
			if (!view.getPerson().getUsername().split("@")[0]
					.equals(MainApplication.userPrimary.getUsername()))
				scroll.addView(new UserView(context, view.getPerson(),
						R.drawable.question, psiv.space, 11, 11), lp);

		}
		initClickHandlers();
	}

	/**
	 * Causes the popup window to be rendered on screen
	 */
	void createPopupWindow() {
		// TODO: Make these values scale to different resolutions
		// TODO: Move to Values.java
		//final int PADDING = 140;
		final int PREVIEW_BAR_HEIGHT = 220;
		final int PREVIEW_BAR_POSITION_Y = 135;

		popup = new PopupWindow(this, Values.screenW ,
				PREVIEW_BAR_HEIGHT, true);
		popup.setOutsideTouchable(true); // TODO: Is this needed?

		popup.showAtLocation(layout, Gravity.BOTTOM, 0,
				PREVIEW_BAR_POSITION_Y);
	}

	/**
	 * Initializes click handlers for go and cancel
	 */
	private void initClickHandlers() {
		getLeftButton().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "Go Clicked");
				MainApplication.screen.getSpaceViewController().changeSpace(
						psiv.space);
				popup.dismiss();
			}
		});
		getMiddleButton().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "Dismiss Click");
				popup.dismiss();
			}
		});
	}

	/**
	 * Returns the Go Button from the layout
	 * 
	 * @return	leftButton - a LinearLayout (button) representing the go button
	 */
	public LinearLayout getLeftButton() {
		 return (LinearLayout) layout.findViewById(R.id.left_accept);
		
	}
	
	/**
	 * Returns the Go Button from the layout
	 * 
	 * @return	middleButton - a LinearLayout (button) representing the go button
	 */
	public LinearLayout getMiddleButton() {
		 return (LinearLayout) layout.findViewById(R.id.middle_decline);
		
	}
	
	/**
	 * Returns the Go Button from the layout
	 * 
	 * @return	rightButton - a LinearLayout (button) representing the go button
	 */
	public LinearLayout getRightButton() {
		 return (LinearLayout) layout.findViewById(R.id.right_accept);
		
	}

	@Override
	public void onDraw(Canvas canvas) {
		final int TRIANGLE_WIDTH = 38;
		final int TRIANGLE_HEIGHT = 33;

		int yTip = 215;
		int xTip1 = Values.screenW*1/6;
		int xTip2 = Values.screenW*5/6;



		drawTriangle(new Point(xTip1, yTip), new Point(
				xTip1 - TRIANGLE_WIDTH / 2, yTip - TRIANGLE_HEIGHT), new Point(
				xTip1 + TRIANGLE_WIDTH / 2, yTip - TRIANGLE_HEIGHT), canvas);
		drawTriangle(new Point(xTip2, yTip), new Point(
				xTip2 - TRIANGLE_WIDTH / 2, yTip - TRIANGLE_HEIGHT), new Point(
				xTip2 + TRIANGLE_WIDTH / 2, yTip - TRIANGLE_HEIGHT), canvas);
	}

	/**
	 * Draws a triangle on screen. Used to create the arrow pointing to the
	 * PrivateSpaceIconView.
	 * 
	 * @param p1
	 *            - Triangle Point 1
	 * @param p2
	 *            - Triangle Point 2
	 * @param p3
	 *            - Triangle Point 3
	 * @param canvas
	 *            - The canvas to render on
	 */
	private void drawTriangle(Point p1, Point p2, Point p3, Canvas canvas) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(2);
		paint.setColor(getResources().getColor(R.color.seventyfivepercentblack));
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);

		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p1.x, p1.y);
		path.close();

		canvas.drawPath(path, paint);
	}
}
