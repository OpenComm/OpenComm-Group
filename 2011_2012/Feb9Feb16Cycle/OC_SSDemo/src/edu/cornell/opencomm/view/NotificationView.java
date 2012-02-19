package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationView {

	private Context context;
	private static String TAG = "View.NotificationView";
	
	public NotificationView(Context context) {
		this.setContext(context);
	}
	
	public void launch(String tip){
		TextView textView = new TextView(context);
		textView.setPadding(10,10,10,10);
		textView.setBackgroundColor(Color.BLACK);
		textView.setTextColor(Color.WHITE);
		
		if (tip.equals("admin")){
			textView.setText(context.getString(R.string.admin_tip));
			Log.d(TAG,"Rendering admin tip with text : "+ context.getString(R.string.admin_tip));
			
		}
		else if (tip.equals("sidechat")){
			textView.setText(context.getString(R.string.sidechat_tip));
			Log.d(TAG,"Rendering sidechat tip with text : "+ context.getString(R.string.sidechat_tip));
		}
		else{
			textView.setText("unknown tip");
			Log.d(TAG,"Rendering unknown tip");
		}
				
		Toast toastView = new Toast(context);
		toastView.setView(textView);
		toastView.setDuration(Toast.LENGTH_LONG);
		toastView.setGravity(Gravity.TOP, 0, 530);
		toastView.show();
	}
	
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	public void launch(String nickname, String tip) {
		TextView textView = new TextView(context);
		textView.setPadding(10,10,10,10);
		textView.setBackgroundColor(Color.BLACK);
		textView.setTextColor(Color.WHITE);
		
		if (tip.equals("adduser")){
			textView.setText(nickname + " " + context.getString(R.string.adduser_tip));
			Log.d(TAG,"Rendering add user tip with text : "+ context.getString(R.string.adduser_tip));
		}
		else if (tip.equals("deleteuser")){
			textView.setText(nickname + " " + context.getString(R.string.deleteuser_tip));
			Log.d(TAG,"Rendering delete user tip with text : "+ context.getString(R.string.deleteuser_tip));
		}
		else{
			textView.setText("unknown tip");
			Log.d(TAG,"Rendering unknown tip");
		}
				
		Toast toastView = new Toast(context);
		toastView.setView(textView);
		toastView.setDuration(Toast.LENGTH_LONG);
		toastView.setGravity(Gravity.TOP, 0, 530);
		toastView.show();
		
	}
	
}
