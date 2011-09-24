package opencomm.android_1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/** The main chat screen with EVERYBODY'S icon in it 
 * 
 * 1) Menu button @ bottom-left, should open up into an "Add Private Space" and "Delete PrivateSpace"
 * 2) Bottom bar with icons representing all existing Private Spaces
 * 3) Main button @ bottom-right, should take you back to this window
 */



public class MainChat extends Activity {
    /** Called when the activity is first created. */
	private LinearLayout root;
	
    @Override
    /** Initialize all views (buttons, bars, etc.) and set OnClickListeners for necessary components
     * 
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // example of how to set View leaves using code (not XML) as described in book, can erase if necessary
        
        // can set up parameters
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
        		ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
        
        LinearLayout.LayoutParams widgetParams = new LinearLayout.LayoutParams(
        		ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, 1.0f);
        
        // root is always the first node
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.LTGRAY);
        root.setLayoutParams(containerParams);
        
        // root's leaf
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setBackgroundColor(Color.GRAY);
        ll.setLayoutParams(containerParams);
        root.addView(ll);
        
        // leaf's leaf
        Button b = new Button(this);
        b.setText("Menu");
        b.setTextColor(Color.RED);
        b.setLayoutParams(widgetParams);
        ll.addView(b);
        
        setContentView(root);
        
        // can add on click listeners and specify own methods within it
        b.setOnClickListener(
        		new Button.OnClickListener(){
        			public void onClick(View v){
        				// blahblah
        			}
        			// add more methods here if you want
        			});
        			

        
    }
}