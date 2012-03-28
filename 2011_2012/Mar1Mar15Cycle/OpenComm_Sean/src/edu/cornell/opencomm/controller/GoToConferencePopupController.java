package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.util.Log;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.PopupNotificationView;

public class GoToConferencePopupController extends PopupNotificationController {

	public GoToConferencePopupController(
			PopupNotificationView popupNotificationView) {
		super(popupNotificationView);
	}
	
	public void handleOkButtonClicked(String[] args) {
		Log.d(LOG_TAG, "Go To Conference");
		DashboardView dv = (DashboardView) popupNotificationView.getContext();
		
		Intent i = new Intent(popupNotificationView.getContext(), MainApplication.class);
		i.putExtra(Network.KEY_USERNAME, args[0]);
		i.setAction(Network.ACTION_LOGIN);
		popupNotificationView.getContext().startActivity(i);
		
		popupNotificationView.dismiss();

	}

}