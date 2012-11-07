package edu.cornell.opencomm.view;

import android.content.Context;
import android.view.View;

public class ChatSpaceView extends View{

	public ChatSpaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	//Main View Methods:
	//When the screen is clicked- need to display the action bar/context bar and display usernames
	public void onScreenClicked(){

	}

	//When the user drags the person to another location inside the circle
	public void onDrag(){

	}

	public void createCircle(){

	}

	//Call this method whenever you want to refresh the circle. Adds placeholders at appropriate places and redraws the locations
	public void updateCircle(){

	}
	//This method is also a part of the action bar/context methods
	//Do this when the person is dragged outside the circle's radius - especially when the picture reaches the ends of the screen
	public void addPersonToSideChat(){

	}

	//Context/Action Bar Methods:
	//When add is pressed- need to pop up the invite page
	//For now: Just resize the circle and place the person
	//Moderator and User 
	public void addPressed(){

	}
	
	//Moderator only
	public void removePerson(){

	}
	
	//Moderator Only
	public boolean endConferencePressed(){
		//Stubbed
		return true; 
	}
	
	//Moderator only- Pressed when moderator wants to select someone else as a moderator?
	public void moderatorPressed(){
		
	}
	
	//Moderatory and User
	public void leavePressed(){
		
	}

	//When the moderator leaves the conference
	public void moderatorLeft(){

	}
	
	//Moderator and User
	public void profilePressed(){
		
	}


}