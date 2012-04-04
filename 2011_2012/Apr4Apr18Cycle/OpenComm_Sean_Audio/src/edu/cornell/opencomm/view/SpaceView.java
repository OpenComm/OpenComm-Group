package edu.cornell.opencomm.view;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

/* A SpaceView is the graphical representation of a space, aka the screen you see on the monitor
 * showing all the icons of the people in the space (above the privatespace bar).
 * This view does not include the Main, Menu, Trash buttons, or bottom PrivateSpace bar.
 * Your icon will not show up on screen */

public class SpaceView extends View {
    private static String LOG_TAG = "OC_SpaceView"; // for error checking
    private Context context;
    /** A variable containing this spaceView object */
    public SpaceView thisSpaceView;
    /** The space that this SpaceViwe is currently representing */
    Space space;
    Bitmap voice_image;

    /** Temporary variables used in the onTouchEventMethods */
    public UserView selectedIcon;
    boolean clickOnIcon = false;
    private boolean dim = false;

    /** Controllers for SpaceView*/
    SpaceViewController spaceViewController;
    EmptySpaceMenuController empytSpaceMenuController;
    UserIconMenuController userIconMenuController;
    ParticipantView pView;

    /**
     * Constructor: This one is used by the XML file to automatically generate a
     * SpaceView
     */
    public SpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        thisSpaceView = this;
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
        thisSpaceView = this;
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
                    // If clicked on an icon
                    selectedIcon = null;
                    for (UserView icon : space.getAllIcons()) {
                        if (icon.clickedInside(mouseX, mouseY)) {
                            selectedIcon = icon;
                            icon.getUserViewController().handleClickDown(
                                    icon.getX(), icon.getY());
                            clickOnIcon = true;
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // if clicked on an icon
                    if (clickOnIcon) {
                        selectedIcon.getUserViewController().handleClickUp(
                                mouseX, mouseY);
                        selectedIcon = null;
                        clickOnIcon = false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    /* If a person icon is selected, then move the icon to
					 the current position */
                    if (clickOnIcon) {
                        selectedIcon.getUserViewController().handleMoved(
                                mouseX, mouseY);
                    }
                }
                invalidate();
                return false;
            }
        });
        // The long click listener (responds to press and hold)
        this.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                if (clickOnIcon) {
                    boolean longpress = selectedIcon.getUserViewController()
                            .handleLongPress();
                    if (longpress) {
                        selectedIcon = null;
                        clickOnIcon = false;
                    }
                    return longpress;
                } else {
                    thisSpaceView.getSpaceViewController().handleLongClick();
                    return true;
                }
            }
        });
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
                if (!p.getPerson().getNickname().equals(MainApplication.userPrimary.getNickname()))
                    p.draw(canvas);
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
