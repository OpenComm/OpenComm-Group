package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConferenceRoomView extends View {
	private int layout;
	private Context context;

	static boolean isCreated;
	public ConferenceRoomView(Context context, int layout) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layout = layout;
	}
	
	public void create(){
		if (isCreated){
			inflate(context, layout, (ViewGroup)findViewById(R.id.threepanelpager)); //TODO this is magic
			
		}
	}
	public void resume(){
		if(isCreated){
			//invalidate?
		}else{
			//create
		}
	}
 
}
