package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.EmptySpaceMenuController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.controller.SpaceViewController;
import edu.cornell.opencomm.controller.UserIconMenuController;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;

/* A SpaceView is the graphical representation of a space, aka the screen you see on the monitor
 * showing all the icons of the people in the space (above the privatespace bar).
 * This view does not include the Main, Menu, Trash buttons, or bottom PrivateSpace bar.
 * Your icon will not show up on screen */

public class SpaceView extends View {
    private static String LOG_TAG = "OC_SpaceView"; // for error checking
    private Context context;
    /** The space that this SpaceViwe is currently representing */
    Space space;
    Bitmap voice_image;

    /** Temporary variables used in the onTouchEventMethods */
    public UserView selectedIcon;
    boolean clickOnIcon = false;
    private boolean dim = false;
    private boolean drag = true;
    private boolean lassomode = false;
    private boolean menuPopup = false;
    private Point savedPoint = null;
    private ArrayList<ArrayList<Point>> dragPoints = new ArrayList<ArrayList<Point>>();
    private ArrayList<UserView> lassoedIcons = new ArrayList<UserView>();

    /** Controllers for SpaceView*/
    SpaceViewController spaceViewController;
    EmptySpaceMenuController empytSpaceMenuController;
    UserIconMenuController userIconMenuController;
    ParticipantView pView;
    private UserView ghost;
    private boolean D = Values.D;

    private double INSENSITIVITY = 50.0;

    /**
     * Constructor: This one is used by the XML file to automatically generate a
     * SpaceView
     */
    public SpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupControllers();
        setupImage();
        setupEventListeners();
    }

    /**
     * Constructor: Create a screen for a NEW privatespace that is empty (except
     * for you) Initialize all variables
     */
    public SpaceView(Context context, Space space) {
        super(context);
        this.context = context;
        this.space = space;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupControllers();
        setupImage();
        setupEventListeners();
    }

    /** Initialize all Controllers for this SpaceView */
    public void setupControllers(){
        spaceViewController = new SpaceViewController(this);
        empytSpaceMenuController = new EmptySpaceMenuController(context);
        userIconMenuController = new UserIconMenuController(context, this);
        pView = new ParticipantView(this.context, this);
    }

    /** Setup all Event Listeners for this SpaceView: onTouchListener
     * and onLongClickListener
     */
    public void setupEventListeners() {
        // The onTouch Listener (responds to any touch events)
        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int mouseX = (int) event.getX();
                int mouseY = (int) event.getY();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    drag = menuPopup = false;
                    dragPoints.add(new ArrayList<Point>());
                    savedPoint = new Point(mouseX, mouseY);
                    // If clicked on an icon
                    selectedIcon = null;
                    for (UserView icon : space.getAllIcons()) {
                        if (icon.clickedInside(mouseX, mouseY)) {
                            if(!icon.isLassoed()) {
                                icon.getUserViewController().handleClickDown(
                                        icon.getX(), icon.getY());
                            }
                            clickOnIcon = true;
                            selectedIcon = icon;
                        }
                        if(icon.isLassoed())
                            lassoedIcons.add(icon);
                    }
                    Log.d("TEXAS", "hog 1size" + lassoedIcons.size());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(!drag && !menuPopup && ghost == null) {
                        Log.d("TEXAS", "hog got here" + drag + menuPopup);
                        cancelLassoMode();
                    }
                    if(savedPoint != null)
                        savedPoint = null;
                    // if clicked on an icon
                    if (clickOnIcon) {
                        Log.d("TEXAS", "hog icon was clicked");
                        if (ghost == null) {
                            selectedIcon.getUserViewController().handleClickUp(
                                    mouseX, mouseY);
                        } else {
                            Log.d("TEXAS", "hog ghosted");
                            ghost = null;
                            MainApplication mainApp = (MainApplication)context;
                            if(mainApp.side1.contains(mouseX, mouseY)) {
                                Log.d("TEXAS", "hog contains1");
                                Log.d("TEXAS", "hog size" + lassoedIcons.size());
                                for(UserView user : lassoedIcons) {
                                    mainApp.side1.space.getInvitationController().inviteUser(user.getPerson(), Network.DEFAULT_INVITE);
                                }
                                cancelLassoMode();
                            } else if(mainApp.side2.contains(mouseX, mouseY)) {
                                Log.d("TEXAS", "hog contains2");
                                Log.d("TEXAS", "hog size" + lassoedIcons.size());
                                for(UserView user : lassoedIcons) {
                                    mainApp.side2.space.getInvitationController().inviteUser(user.getPerson(), Network.DEFAULT_INVITE);
                                }
                                cancelLassoMode();
                            }

                        }
                        selectedIcon = null;
                        clickOnIcon = false;
                    }
                    lassoedIcons.clear();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //drag = true;
                    /* If a person icon is selected, then move the icon to
					 the current position */
                    if (clickOnIcon && !lassomode) {
                        selectedIcon.getUserViewController().handleMoved(
                                mouseX, mouseY);
                    } else if(clickOnIcon && lassomode) {
                        if(!selectedIcon.isLassoed()) {
                            cancelLassoMode();
                            selectedIcon.getUserViewController().handleMoved(
                                    mouseX, mouseY);
                        } else {
                            performGhostDrag(mouseX, mouseY);
                        }
                    } else {
                        createLasso(view, event);
                    }
                }
                invalidate();
                return false;
            }
        });
        // The long click listener (responds to press and hold)
        this.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public synchronized boolean onLongClick(View arg0) {
                //Log.d("TEXAS", "hogsize " + dragPoints.get(0).size());
                if (dragPoints.size() == 0 || dragPoints.size() == 1 && dragPoints.get(0).size() == 0) {
                    if (clickOnIcon) {
                        boolean longpress = selectedIcon.getUserViewController()
                                .handleLongPress();
                        if (longpress) {
                            selectedIcon = null;
                            clickOnIcon = false;
                            menuPopup = true;
                            cancelLassoMode();
                        }
                        return longpress;
                    } else {
                        SpaceView.this.getSpaceViewController().handleLongClick();
                        menuPopup = true;
                        cancelLassoMode();
                        return true;
                    }
                } else {
                    return true;
                }
            }
        });
    }

    public void cancelLassoMode() {
        Log.d("LOL", "hog CANCEL LASSSOMODEEEEE");
        lassomode = false;
        dragPoints.clear();
        lassoedIcons.clear();
        for (UserView p : space.getAllIcons()) {
            p.setLassoed(false);
        }
    }

    private void performGhostDrag(int mouseX, int mouseY) {
        if(ghost == null) {
            ghost = selectedIcon.getGhost();

        }
        ghost.getUserViewController().handleMoved(
                mouseX, mouseY);
    }

    private double distance(Point p1, Point p2) {
        double diffX = p1.x - p2.x;
        double diffY = p1.y - p2.y;
        return Math.sqrt(diffX*diffX + diffY*diffY);
    }

    private synchronized void createLasso(View view, MotionEvent event) {
        Log.d("TEXAS", "hog create lasso called");
        if(menuPopup)
            return;
        lassomode = true;
        ArrayList<Point> curPath = dragPoints.get(dragPoints.size() - 1);
        if(savedPoint != null) {
            curPath.add(savedPoint);
            savedPoint = null;
            Log.d("TEXAS", "hogSavedPoint added");
        }

        Point newPoint = new Point((int)event.getX(), (int)event.getY());
        if(/*dragPoints.size() == 1 && */curPath.size() == 1) {
            Log.d("TEXAS", "hog SinglePoint");
            Point lastPoint = curPath.get(0);
            if(distance(newPoint, lastPoint) < INSENSITIVITY) {
                Log.d("TEXAS", "hog insensitive");
                Log.d("Last Point", "hog:" + lastPoint.toString());
                Log.d("Cur Point", "hog" + newPoint.toString());
                Log.d("Distance", "hog" + distance(newPoint, lastPoint));
                return;
            }
        }
        drag = true;
        if(curPath.size() > 0) {
            Point prevPoint = curPath.get(curPath.size()-1);
            if(newPoint.x == prevPoint.x && newPoint.y == prevPoint.y)
                return;
            for (UserView p : space.getAllIcons()) {
                if (!p.getPerson().getNickname().equals(MainApplication.userPrimary.getNickname())) {
                    if(p.segmentIntersects(prevPoint, newPoint)) {
                        p.setLassoed(true);
                        if (D) Log.d("TEXAS", "I hogtied that userview");
                    }
                }
            }
        }
        curPath.add(newPoint);
    }

    /** Add the image of the voice coming from you */
    public void setupImage() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        voice_image = BitmapFactory.decodeResource(this.getResources(),
                Values.voice_image);
    }

    /**
     * Manually set the space that this SpaceView corresponds to (used in
     * conjunction with the constructor for the XMl file
     */
    public void setSpace(Space space) {
        this.space = space;
    }

    /*
     * Draw everything on the screen including:
     * 1) voice image
     * 2) All icons of people in this space
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Space mainSpace = Space.getMainSpace();
        if(mainSpace == null) return;
        Log.v(LOG_TAG, mainSpace.getOwner().getNickname());
        Log.v(LOG_TAG, "Room ID is "+ mainSpace.getRoomID());

        if (getSpace() == mainSpace) {
            if (!getSpace().getEntered()) {
                mainSpace.setEntered(true);
                /*LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //AdminTipView adminTipView = new AdminTipView(inflater);
                //adminTipView.launch();*/

                //TO DO: put values for header and such in strings table
                PopupNotificationView pnv = new PopupNotificationView(context, null, "tip", context.getString(R.string.admin_tip), "", Values.tip);
                pnv.createPopupWindow();
            }
        }
        if (canvas != null && space != null && space.getAllIcons() != null) {
            for (UserView p : space.getAllIcons()) {
                if (!p.getPerson().getNickname().equals(MainApplication.userPrimary.getNickname())) {
                    p.draw(canvas);
                }

            }
            if(ghost != null)
                ghost.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        for(ArrayList<Point> pointList : dragPoints) {
            for(int i = 0; i < pointList.size() - 1; i++) {
                Point p1 = pointList.get(i);
                Point p2 = pointList.get(i+1);
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
            }
        }
    }

    // GETTERS

    /** Get the main background color */
    private int getColor() {
        return R.color.main_background;
    }

    /** Get the list of all Icons shown in this spaceview */
    public LinkedList<UserView> getAllIcons() {
        return space.getAllIcons();
    }

    /** Get the Space that this SpaceView is representing */
    public Space getSpace() {
        return space;
    }

    /** Get the MainApplication object */
    public MainApplication getActivity() {
        return (MainApplication) context;
    }

    /** Get the SpaceViewController */
    public SpaceViewController getSpaceViewController() {
        return spaceViewController;
    }
}
