package edu.cornell.opencomm.controller;
import android.os.AsyncTask;
import android.widget.EditText;
import edu.cornell.opencomm.view.ConfirmationView;
import android.util.Log; 


//Author:Spandana Govindgari sg754

public class ConfirmationController {
	
	private ConfirmationView confirmView; 
	
	private static final String LOG_TAG= "ConfirmationController"; 
	
	public ConfirmationController(ConfirmationView confirmView){
		this.confirmView = confirmView;
	}
	
	//[TODO] - When accept is pressed add functionality
	//Ask design team about more information for this page
	public void acceptPressed(){
		
	}
	//[TODO] - When cancel is pressed do not add
	public void cancelPressed(){
		
	}
	
	//[TODO]- Do something with asyncTask
	
	//[TODO]- Ask design team about 
	//1. The contact picture
	//2. Layout- Open comm logo needed? And specs?
	//3. The confirmation text and more text
	//4. The functionalities of accept and reject buttons
	//Is this for chat or for conference page

}
