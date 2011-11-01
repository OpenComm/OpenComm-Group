package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.PrivateSpacePreviewPopup;

public class PrivateSpacePreviewPopupController {

	private PrivateSpacePreviewPopup popup = null;
	public PrivateSpacePreviewPopupController(PrivateSpacePreviewPopup popup){
		this.popup = popup;
	}

	/** If clicked on the popup preview, then 
	 *  1) Close the popup preview
	 *  2) Notify the spaceview controller to change the spaceview 
	 *  to the room this preview represents
	 */
	public void handlePopupPreviewClicked(){
		
	}
	
	/** Close the popup preview window.
	 * This method will be called when the private space icon is clicked again when 
	 * the popup preview is open. Should be called by the PrivateSpaceIconController */
	public void closePopupPreview(){
		
	}
	
}
