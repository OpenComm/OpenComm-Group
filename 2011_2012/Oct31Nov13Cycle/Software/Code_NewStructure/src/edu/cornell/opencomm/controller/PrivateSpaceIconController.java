package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.PrivateSpaceIconView;

public class PrivateSpaceIconController {

	private PrivateSpaceIconView PSIcon = null;
	public PrivateSpaceIconController(PrivateSpaceIconView icon){
		this.PSIcon = icon;
	}

	/** If simply clicked a PrivateSpace Icon without dragging it then 
	 * toggle between highlighted and unhighlighted
	 */
	public void handleSimpleClick(){
		PSIcon.toggleSelected();
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
