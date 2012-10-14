package edu.cornell.opencomm.view;

import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class UserView extends View{
    Context context;
    User person;
    Space space;
	public UserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	 public UserView(Context context, User person, int imageID, Space space, int x, int y){
	        super(context);
	        this.context = context;
	        this.person = person;
	        this.space = space;
	 }
}
