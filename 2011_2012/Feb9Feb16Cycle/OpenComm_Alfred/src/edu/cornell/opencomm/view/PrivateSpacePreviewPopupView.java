package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
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

/**
 * Represents the popup window that displays the users inside of a space
 * @author jonathanpullano
 *
 */
public class PrivateSpacePreviewPopupView extends LinearLayout {
    private PrivateSpaceIconView psiv; //The Icon this popup is associated with
    private LinearLayout layout; //The layout representing the popup
    private PopupWindow popup; //The window containing the popup
    private Button goButton; //A reference to the Go Button
    
    private static String TAG = "PrivateSpacePreviewPopupView"; //Debug Tag
    
    private boolean D = false; //Debug Mode
    
    /**
     * Constructor
     * @param context     - MainApplication
     * @param personViews - list of UserViews for people in the space
     */
    public PrivateSpacePreviewPopupView(Context context, PrivateSpaceIconView psiv) {
        super(context);
        
        this.psiv = psiv;
        
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout = (LinearLayout) inflater.inflate(R.layout.private_space_preview_popup, this);
        
        LinearLayout scroll = (LinearLayout) layout.findViewById(R.id.private_space_popup_preview_linear_layout);
        
        final int USER_ICON_DIMENSION = 122;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(USER_ICON_DIMENSION, USER_ICON_DIMENSION);
        
        //Populate view with user icons except for yourself
        for(UserView view : psiv.space.getAllIcons()) {
            if (D) Log.d(TAG, "Adding View");
            if(!view.getPerson().getUsername().split("@")[0].equals(MainApplication.user_primary.getUsername()))
                scroll.addView(new UserView(context,view.getPerson(),R.drawable.question, psiv.space,11,11), lp);
            
        }
        scroll.invalidate();
        initClickHandlers();
    }
    
    /**
     * Causes the popup window to be rendered on screen
     */
    void createPopupWindow() {
        //TODO: Make these values scale to different resolutions
        //TODO: Move to Values.java
        final int PADDING = 100;
        final int PREVIEW_BAR_HEIGHT = 160;
        final int PREVIEW_BAR_POSITION_Y = 160;
        
        popup = new PopupWindow(this, Values.screenW + PADDING, PREVIEW_BAR_HEIGHT+100, true);
        popup.setOutsideTouchable(true); //TODO: Is this needed?
        
        popup.showAtLocation(layout, Gravity.BOTTOM, 0, PREVIEW_BAR_POSITION_Y-100);
    }
    
    /**
     * Initializes click handlers for go and cancel
     */
    private void initClickHandlers() {
        getGoButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Go Clicked");
                MainApplication.screen.getSpaceViewController().changeSpace(psiv.space);
                popup.dismiss();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Log.d(TAG, "Dismiss Click");
                popup.dismiss();
            }
        });
    }
    
    /**
     * Returns the Go Button from the layout
     * @return goButton - a Button representing the go button
     */
    public Button getGoButton() {
        if (goButton != null)
            return goButton;
        if (layout != null)
            goButton = (Button) layout.findViewById(R.id.go_button);
        return goButton;
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        int[] pos = new int[2];
        psiv.getLocationOnScreen(pos);
        
        int xTip = pos[0]+psiv.getWidth()/2;
        int yTip = 160 + 28;
        
        final int TRIANGLE_WIDTH = 30;
        final int TRIANGLE_HEIGHT = 37;
        
        drawTriangle(new Point(xTip, yTip), new Point(xTip - TRIANGLE_WIDTH/2, yTip - TRIANGLE_HEIGHT), new Point(xTip + TRIANGLE_WIDTH/2, yTip - TRIANGLE_HEIGHT), canvas);
    }
    
    /**
     * Draws a triangle on screen. Used to create the arrow pointing to the PrivateSpaceIconView.
     * @param p1 - Triangle Point 1
     * @param p2 - Triangle Point 2
     * @param p3 - Triangle Point 3
     * @param canvas - The canvas to render on
     */
    private void drawTriangle(Point p1, Point p2, Point p3, Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.light_grey_translucent));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(p1.x,p1.y);
        path.lineTo(p2.x,p2.y);
        path.lineTo(p3.x,p3.y);
        path.lineTo(p1.x,p1.y);
        path.close();

        canvas.drawPath(path, paint);
    }
}
