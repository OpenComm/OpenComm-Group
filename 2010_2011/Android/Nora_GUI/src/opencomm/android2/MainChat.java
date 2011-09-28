package opencomm.android2;

import java.util.LinkedList;


import android.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainChat extends Activity {
	//private LinearLayout root;
	private View root;
	private PrivateSpace space; // holds the group of people currently in this space

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a space with everyone in it
        space = new PrivateSpace();
        space.initializeEveryone(); // add everyone to it!
        
        root = new ScreenView(this, space);
        setContentView(root);
    }
    
    
    public PrivateSpace getAllSpaces(){
    	return space;
    }
}