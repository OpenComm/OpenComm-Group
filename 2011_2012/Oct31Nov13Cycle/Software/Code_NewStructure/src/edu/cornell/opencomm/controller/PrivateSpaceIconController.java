package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.PrivateSpaceIconView;

public class PrivateSpaceIconController {
	boolean clickedOnce=false;
	
	private PrivateSpaceIconView PSIcon = null;
	public PrivateSpaceIconController(PrivateSpaceIconView icon){
		this.PSIcon = icon;
	}
	
	/** Handle when a PrivateSpaceIcon is clicked up. Keep track of 
	 * if it has been clicked once or a second time. 
	 * @return - true if has clicked up and down once, which should open up 
	 * PopupPreview. False if clicked again. These should alternate
	 */
	public boolean handleClickUp(){
		clickedOnce= !clickedOnce;
		return clickedOnce;
	}
	
	/** If a UserView was dragged on top of a Private Space Icon
	 * but not yet released, then highlight the Private Space Icon
	 */
	public void handleIconHovered(){
		PSIcon.setHighlighted(true);
	}
	
	/** If a UserView used to be hovered over a Private Space Icon
	 * but then was moved such that it was no longering hovering, then
	 * unhighligh the Private Space Icon
	 */
	public void handleIconNotHovered(){
		PSIcon.setHighlighted(false);
	}
}
