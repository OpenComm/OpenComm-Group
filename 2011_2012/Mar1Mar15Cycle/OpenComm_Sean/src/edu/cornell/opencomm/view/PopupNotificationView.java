package edu.cornell.opencomm.view;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.PopupNotificationController;
import edu.cornell.opencomm.model.Space;

public class PopupNotificationView extends LinearLayout {

	private static String LOG_TAG = "OC_PopupNotificationView"; // for error checking
    private Context context;
    private LayoutInflater inflater;
    private PopupWindow window = null;
    private PopupNotificationController popupNotificationController = new PopupNotificationController(this);
    private View PopupNotificationLayout = null;
    
    private LinearLayout layout; //The layout representing the popup
    private PopupWindow popup; //The window containing the popup
    private Button goButton; //A reference to the Go Button

    
    private String headerText;
    private String line1;
    private String line2;
    private int type;

    
    public PopupNotificationView(Context context, String head, String line1, String line2, int type) {
    	super(context);
    	this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	if(type != Values.confirmation) {
    		this.layout = (LinearLayout) inflater.inflate(R.layout.popup_notification_layout, this);
    	}
    	else {
    		this.layout = (LinearLayout) inflater.inflate(R.layout.popup_confirmation_layout, this);
    	}
    	this.headerText = head;
    	this.line1 = line1;
    	this.line2 = line2;
    	this.type = type;
    	this.context = context;
    	
    	initEventsAndProperties();
    }
    
    public void initEventsAndProperties() {
    	if (inflater != null) {
    		View PNLayoutFromInflater = inflater.inflate(R.layout.popup_notification_layout, this);
    		TextView t = new TextView(context);
    		t = (TextView)findViewById(R.id.header);
    		t.setText(headerText);
    		t = (TextView)findViewById(R.id.line1);
    		t.setText(line1);
    		t = (TextView)findViewById(R.id.line1);
    		t.setText(line2);
    		if(type == Values.tip) {
    			t.setTextColor(R.color.blue);
    		}
    		if(type == Values.notification) {
    			t.setTextColor(R.color.green);
    		}
    		if(type == Values.confirmation) {
    			t.setTextColor(R.color.green);
    			PNLayoutFromInflater = inflater.inflate(R.layout.popup_confirmation_layout, this);
    		}
    		if(PNLayoutFromInflater != null) {
    			this.PopupNotificationLayout = PNLayoutFromInflater;
    		}
    	}
    	if(type == Values.confirmation) {
    		initializeEvents();
    	}
    }

    void createPopupWindow() {
    
        popup = new PopupWindow(this, Values.screenW, 140, true);
        popup.showAtLocation(layout, Gravity.BOTTOM, 0, 60);
    }
    
    public ImageButton getButton() {
    	ImageButton imgb = null;
    	if(type == Values.confirmation && layout != null) {
    		imgb = (ImageButton) layout.findViewById(R.id.gotoButton);
    	}
    	return imgb;
    }
    
	private void initializeEvents() {
		ImageButton gotoButton = getButton();
		if(gotoButton != null) {
			gotoButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(LOG_TAG, "Confirm click");
					findViewById(R.id.popOverlay).setVisibility(View.VISIBLE);
					popupNotificationController.handleOkButtonClicked();
					popup.dismiss();
				}
			});
		}
		this.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(LOG_TAG, "Focus change");
				popupNotificationController.handleOnFocusChange();
				popup.dismiss();
			}
		});
	}
    
}
