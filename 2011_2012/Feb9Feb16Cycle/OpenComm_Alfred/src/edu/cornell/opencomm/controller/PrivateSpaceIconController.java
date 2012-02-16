package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.PrivateSpaceIconView;

public class PrivateSpaceIconController {
    boolean clickedOnce = false;

    private PrivateSpaceIconView PSIcon = null;

    public PrivateSpaceIconController(PrivateSpaceIconView icon) {
        this.PSIcon = icon;
    }

    /**
     * Handle when a PrivateSpaceIcon is clicked up. Keep track of if it has
     * been clicked once or a second time.
     * 
     * @return - true if has clicked up and down once, which should open up
     *         PopupPreview. False if clicked again. These should alternate
     */
    public boolean handleClickUp() {
        clickedOnce = !clickedOnce;
        return clickedOnce;
    }

}
