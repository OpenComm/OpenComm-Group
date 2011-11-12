package edu.cornell.opencomm.view;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.EmptySpaceMenuController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.controller.PrivateSpaceIconController;
import edu.cornell.opencomm.controller.PrivateSpacePreviewPopupController;
import edu.cornell.opencomm.controller.SideChatIconMenuController;
import edu.cornell.opencomm.controller.UserIconMenuController;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;

/** An icon representing a private space room (or sidechat)
 *  Appears at the bottom scrollable bar of the screen 
 *  
 *  @author - noranq 11/5
 */   
public class PrivateSpaceIconView extends ImageButton{
	/** LOG_TAG to be used with LOGCAT */
	private static String LOG_TAG="OC_PrivateSpaceView"; 
	/** Context - instance of MainApplication */
    Context context;
    /** The private space that this icon represents */
	public Space space; 
	/** The list of all current privatespace icons in existence */
    public static LinkedList<PrivateSpaceIconView> allPSIcons
    = new LinkedList<PrivateSpaceIconView>(); 
    
    /** Colors offered for private spaces */
    public static int[]COLORS = {Color.BLUE, Color.YELLOW, Color.GREEN, 
    	Color.MAGENTA, Color.CYAN, Color.DKGRAY}; 
    /** The current color int */
    public static int colorInt = -1; 
    /** The current color to use for an icon */
    private int color = Color.BLUE; 
    
    /** Controllers */
    PrivateSpaceIconController privateSpaceIconController;
    PrivateSpacePreviewPopupController privateSpacePreviewPopupController;
    SideChatIconMenuController sideChatIconMenuController;
    
    /** States of the icon */
    boolean isSelected=false; // if true, icon should appear highlighted
    boolean isHighlighted=false; // if true, is also highlighted, but will not be "selected" for deletion

    
    /** Constructor: Private Space Icon View
     * @param context - instance of the activity (MainApplication)
     * @param space - space object that this private space icon represents
     */
    public PrivateSpaceIconView(Context context, Space space){
    	super(context);
    	initializeIcon(context, space);
    }
    
    /** Constructor: Private Space Icon View - constructed from XML file 
     *  @param context - instance of the activity (MainApplication)
     *  @param attrs - AttributeSet of XML parameters
     *  @param defStyle - XML stuff 
     *  @param parent - space object that this private space icon represents
     */
    public PrivateSpaceIconView(Context context, AttributeSet attrs, int defStyle, Space space){
    	super(context, attrs, defStyle);
    	initializeIcon(context, space);
    }
    
    /** Constructor: Private Space Icon View - constructed from XML file 
     *  @param context - instance of the activity (MainApplication)
     *  @param attrs - AttributeSet of XML parameters
     *  @param parent - space object that this private space icon represents
     */
    public PrivateSpaceIconView(Context context, AttributeSet attrs, Space space){
    	super(context, attrs);
    	initializeIcon(context, space);
    }
    
    /** Method called by all PrivateSpaceIconView constructors.
     * (1) Initialize all variable.
     * (2) Initiatlizes PrivateSpaceIconController and PrivateSpacePreviewPopupController
     * (3) Adds the icon to the xml file so that it will show
     * (4) Initializes colors
     * (5) Initializes touch listener
     */
    public void initializeIcon(Context context, Space space){
    	this.context = context;
    	this.space = space;
    	privateSpaceIconController = new PrivateSpaceIconController(this);
    	privateSpacePreviewPopupController = new PrivateSpacePreviewPopupController(context, this);
    	sideChatIconMenuController = new SideChatIconMenuController(context,this);
    	// adds this PS Icon View to the static list of all PS Icons
    	allPSIcons.add(this);
    	// adds this PS Icon View to the XML so that it may show on screen
    	((MainApplication)context).addPrivateSpaceButton(this); 
    	// Initializes the icon's color
    	colorInt++;
    	this.color = PrivateSpaceIconView.COLORS[colorInt % PrivateSpaceIconView.COLORS.length];
    	// Initialize the touch listener
    	initTouch();
    }
    
    /** Delete this PrivateSpace Icon officially from the XML. Remove from the static list of 
     *  all PS icons */
    public void deleteThisPSView(){
        allPSIcons.remove(this);
        ((MainApplication)context).delPrivateSpaceButton(this); 
    }
    
    
    public static ImageView plusSpaceButton(int color, Context context){
        Log.v(LOG_TAG, "PLUS BUTTON");
        int w=Values.privateSpaceButtonW;
        int h=Values.privateSpaceButtonW;
        Bitmap plus = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
         Canvas c = new Canvas(plus);
         Paint p= new Paint();
         p.setColor(color);
         //c.drawRect(w/9, (3*h)/7, (8*w)/9, (4*h)/7, p);
         //c.drawRect((3*w)/7, h/9, (4*w)/7, (8*h)/9,p);
      
         c.drawRect((w-Values.plusButtonLength)/2, (h-Values.plusButtonWidth)/2, 
        		 (w-Values.plusButtonLength)/2+Values.plusButtonLength,
        		 (h-Values.plusButtonWidth)/2+Values.plusButtonWidth,p);
         c.drawRect((w-Values.plusButtonWidth)/2, (h-Values.plusButtonLength)/2,
        		 (w-Values.plusButtonWidth)/2+Values.plusButtonWidth,
        		 (h-Values.plusButtonLength)/2+Values.plusButtonLength,p);
         //canvas.drawBitmap(plus,0, 0, null);
         final ImageView iv=new ImageView(context);
         iv.setImageBitmap(plus);
     	 
     	
      return  iv;
    }
    
    
    /* Set up a touch listener that will adjust the selections and highlights according
     * to if you touched the icon. 
     * 1) Click once should highlight button and open preview (close other previews) TODO VINAY
     * 2) Click a second time should open up that private space (call method from main application), and unhighlight the button */
    
    long startTime = 0;
    long endTime = 0;
	
    public void initTouch(){
    	this.setOnTouchListener(new View.OnTouchListener(){
    		public boolean onTouch(View view, MotionEvent evt){
    			switch(evt.getAction()){
    			/* TODO VINAY - this is where the clicking of the private space
    			 * icon is happening, and it seems to freeze right after opening up a 
    			 * popup preview! - NORA
    			 */

    				case MotionEvent.ACTION_DOWN:
    					startTime = evt.getEventTime();
    				break;
    				
    				case MotionEvent.ACTION_UP:
    					// Highlight the icon on or off, and return true if on
    					boolean showPopup = privateSpaceIconController.handleClickUp();
    					Log.v("PSIconView", "Clicked up, highlight " + showPopup);
    					endTime = evt.getEventTime();
    					if (showPopup)
    						// UI Team - for now do not do anything
    						;
    				
    						// if the highlight is on, then show the popup preview
    						//privateSpacePreviewPopupController.openPopupPreview();
    					else
    						// UI Team - for now will change the screen of the spaceview
    						MainApplication.screen.getSpaceViewController().changeSpace(space);
    				
    						// if not, then close the popup preview
    						//privateSpacePreviewPopupController.closePopupPreview();
    				
    				break;

    			}
    			Log.v(LOG_TAG, "TimeDiff : "+Long.toString(endTime - startTime));
    			if(endTime - startTime > 200){
    	        	SideChatIconMenuController.showSideChatMenu(); 
    	 			return true;
    	        }
    			return false;
    		}
    	});
    	invalidate();
    }

    /** Draw this icon, draw darker (or brighter) if this PrivateSpaceIconView is highlighted or selected. 
     * 
     * This particular version will draw a square for the PS icon and then will fill in the square 
     * if highlighted or selected with color
     */
    public void draw(Canvas canvas){
        super.onDraw(canvas);
        /* Draw...:
         * 1. Draw background to erase old state
         * 2. Draw this private space's color square
         * 3. Draw smaller darker square if the private space isn't open or being previewed TO-DO:Crystal: SHOULD BE DELETED
         * 4. Draw the image for an empty privatespace button
         * 5. Draw the preview if open */
       
        Log.v(LOG_TAG, "THIS HEIGHT"+this.getHeight());
        //(1)
       
         int backgroundColor = (getResources().getColor(R.color.darkgray)); 
         canvas.drawColor(backgroundColor);
        //(2)Crystal   
         /* This function checks for people in the space. If there are at least 1 person,
          * then the function draws a filled space at the bottom of the mainview */
       
             Bitmap subview = Bitmap.createBitmap(this.getWidth()-2*Values.selectedBorder
            		 , this.getHeight()-2*Values.selectedBorder, Bitmap.Config.ARGB_8888);
             Canvas c = new Canvas(subview);
             if(this.getSpace().getAllParticipants().size()>0){
             int size=this.space.getAllParticipants().size();
             Rect[] layoutArray=this.getLayout((size>9? 9:size), subview.getWidth(),subview.getHeight());
             int counter=0;
             
             HashMap<String, User> allPeople = this.space.getAllParticipants();
             Object[] people = allPeople.values().toArray();
             for (Object p : people){
                       Log.v(LOG_TAG, "add people square");
                       if((User)p != MainApplication.user_primary){ Paint pan= new Paint();
                        pan.setStyle(Paint.Style.FILL);
                        pan.setColor(getResources().getColor(((User)p).user_color));
                        c.drawRect(layoutArray[counter], pan);
                        counter++;
                       if(counter> 9) {
                       break;
                   }
                       }
            }
             }
             RectShape r= new RectShape();
             ShapeDrawable myPaint = new ShapeDrawable(r);
           
             myPaint.getPaint().setStyle(Paint.Style.STROKE);
             myPaint.getPaint().setStrokeWidth(2);
             myPaint.getPaint().setColor(Color.BLACK);
             myPaint.setBounds(0,0,this.getWidth(), this.getHeight());
             myPaint.draw(canvas);
             canvas.drawBitmap(subview, Values.selectedBorder, Values.selectedBorder, null);
         
         
        
         //(5)
      /*   if(showPreview){
             // TODO VINAY you could draw the preview here if you like
         } *
   
       /*  if(this.isSelected){
             Log.v(LOG_TAG, "IS SELECTED");
             RectShape rect = new RectShape();
                ShapeDrawable s = new ShapeDrawable(rect);
                s.getPaint().setColor(Color.YELLOW);
                s.setBounds(0,0,this.getWidth(),this.getHeight());//Crystal change
                s.getPaint().setStyle(Paint.Style.STROKE);
                s.getPaint().setStrokeWidth(Values.selectedBorder);
                s.draw(canvas);
         }*/
   } 
     
     /** Open a different space to the screen */
     // NORA- updated 9/3
     public void openPrivateSpace(){
    	 Log.v(LOG_TAG, "Trying to open this space " + space.getRoomID() + ". Screen on is " + space.isScreenOn());
    	 if(!space.isScreenOn()){
    		 ((MainApplication)context).changeSpace(space);
    	 }
     }
     
     /** Return true if this person clicked within this icon's area on the screen */
     public boolean contains(int x, int y) {
 		int[] location = new int[2];
 		this.getLocationOnScreen(location);
 		if (!this.isShown())
 			return false;
 		return (x > location[0] && x < location[0] + this.getWidth()
 				&& y > location[1] - this.getHeight() && y < location[1]
 				+ this.getHeight());
 	}
     
    // GETTERS
     //Crystal
     public Rect[] getLayout(int size,int w, int h){
    	 Rect[] layout=new Rect[9];
    	 //int w= this.getWidth();
    	 //int h=this.getHeight();
    	 int d=Values.squarePadding;// the distance between squares
    	 if(size<5){
    		 layout[0]=new Rect(d, d, w/2-1,h/2-1);
    		 layout[1]=new Rect(w/2-1+d, d, w-d, h/2-1);
    		 layout[2]=new Rect(d,h/2-1+d,w/2-1,h-d );
    		 layout[3]=new Rect(w/2-1+d, h/2-1+d,w-d,h-d);
    		 }else{
    			for(int r=0; r<3; r++){
    				for(int c=0;c<3;c++){
    					layout[3*r+c]=new Rect(d*(c+1)+((w-4*d)/3)*c, d*(r+1)+((h-4*d)/3)*r,
    							d*(c+1)+((w-4*d)/3)*(c+1),  d*(r+1)+((h-4*d)/3)*(r+1));
    				}
    			}
    		 //layout[0]= new Rect(d,d,(w-d*4)/3+d,(h-d*4)/4+d);
    		 //layout[1]= new Rect((w-d*4)/3+2*d,d,(h-d*4)/3+2*d, )
    		 }
    	 return layout;
     }
     
     
    /** Returns the space this object represents */
    public Space getSpace(){
        return space;
    }
    /** Returns true if this object is highlighted but not selected */
    public boolean isHighlighted(){
        return isHighlighted;
    }
    /** Returns true if this object is selected. Users may perform 
     * actions such as deletion on objects that are selected */
    public boolean isSelected(){
        return isSelected;
    }
    /** Returns the PrivateSpaceIconController of this icon. 
     * This controls the appearance of the privatespaceiconview */
    public PrivateSpaceIconController getPrivateSpaceIconController(){
    	return privateSpaceIconController;
    }
    /** Returns the PrivateSpacePreviewPopupController of this icon. 
     * This controls the appearance and touch interactions of the popup preview 
     * representing this room */
    public PrivateSpacePreviewPopupController getPrivateSpacePreviewPopupController(){
    	return privateSpacePreviewPopupController;
    }
    
    // SETTERS
    /** Set the icon to be highlighted or not (in cases where the icon is tapped) */
    public void setSelected(boolean selected){
        isSelected = selected;
        invalidate();
    }
    /** Not the same as selected, but merely highlighted
     * for cases where you drag a person's icon over the privatespace icon and you
     * want to see some feedback */
    public void setHighlighted(boolean highlighted){
        isHighlighted = highlighted;
        invalidate();
    }
    /** Set if the preview of this icon is open */
  /*  public void setPreview(boolean open){
    	showPreview= open;
    } */
    
    /** Toggle between being selected and not selected */
    public void toggleSelected(){
    	isSelected = !isSelected;
    }
   
}
