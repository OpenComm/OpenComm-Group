package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceView_v2;

public class ConferenceController_v2 
{
	ConferenceView_v2 view;
	ConferenceRoom room;
	
	private static final String TAG = "ConferenceController_v2";
	private static final boolean D = true;
	
	
	public ConferenceController_v2() 
	{
		this.view = ConferenceView_v2.getInstance();
		room = new ConferenceRoom("1234");
		try {
			room.create("test");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		try {
			room.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Change the title of the conference (conference name)
	 * @param   title      the new title
	 * @return  void
	 */
	public void setTitle(String title)
	{
		view.txtv_ConfTitle.setText(title);
	}
	
	
	/**
	 * handle the action when add person button was clicked
	 */
	public void HandleAddPerson(String username)
	{
		if(D)
			Log.d(TAG,"addPerson button clicked");
		room.invite(username, "lets chat");
	}
	
	
	
	/**
	 * handle the action when overflow button was clicked
	 */
	public void HandleOverflow()
	{
		if(D)
			Log.d(TAG,"Overflow button clicked");
		//TODO: add "Handle overflow" functions here
	}
	
	
	/**
	 * handle the action when back button was clicked
	 */
	public void HandleBackButton()
	{
		if(D)
			Log.d(TAG,"back button clicked");
		//TODO:add "go back" functions here
	}
	
	public void HandleLeaveButton(){
		if(D)
			Log.d(TAG, "leave button clicked");
		room.leave();
	}
}
