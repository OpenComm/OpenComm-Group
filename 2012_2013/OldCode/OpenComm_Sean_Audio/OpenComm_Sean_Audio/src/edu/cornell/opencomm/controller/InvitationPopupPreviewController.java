package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Invitation;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.InvitationPopupPreviewView;
import edu.cornell.opencomm.view.PrivateSpaceIconView;

public class InvitationPopupPreviewController {
	
    private static String TAG = "InvitationPopupPreviewController";
    private static boolean D = Values.D;
    private Context context;
    private InvitationPopupPreviewView view;
    
    public InvitationPopupPreviewController(InvitationPopupPreviewView ippv, Context context) {
        if (D) Log.d(TAG, "InvitationPopupPreviewController constructor called");
    	this.context = context;
    	this.view = ippv;
    }
    
    public void handleGoLeftClicked () {
    	
    	//PrivateSpaceIconView.allPSIcons.get(0).space.getSpaceController().deleteSpace();
//    	Space templeft = Space.allSpaces.get(String.valueOf(0));
//    	Space.allSpaces.remove(String.valueOf(0));
//    	templeft.getSpaceController().deleteSpace();
    	SpaceController.addExistingSpace(Space.getMainSpace().getContext(), false, view.getInvitation().getRoom(),true/*isLeft*/);
    	//PrivateSpaceIconView left = new PrivateSpaceIconView(Space.getMainSpace().getContext(), newleft);
    	//newleft.getSpaceController().setPSIV(left);s
    	
    	MainApplication.screen.getSpaceViewController().changeSpace(PrivateSpaceIconView.allPSIcons.get(0).space);
		view.dismiss();
    }

	public void handleGoRightClicked() {
	//	PrivateSpaceIconView.allPSIcons.get(2).space.getSpaceController().deleteSpace();
		
//		Space tempright = Space.allSpaces.get(String.valueOf(2));
//    	Space.allSpaces.remove(String.valueOf(2));
//    	tempright.getSpaceController().deleteSpace();
    	SpaceController.addExistingSpace(Space.getMainSpace().getContext(), false, view.getInvitation().getRoom(),false/*isLeft*/);
    	//PrivateSpaceIconView left = new PrivateSpaceIconView(Space.getMainSpace().getContext(), newright);
    	//newright.getSpaceController().setPSIV(left);
    	
		MainApplication.screen.getSpaceViewController().changeSpace(PrivateSpaceIconView.allPSIcons.get(2).space);
		view.dismiss();
	}

	public void handleCancelClicked() {
		 MultiUserChat.decline(view.getInvitation().getConnection(),view.getInvitation().getRoom(), view.getInvitation().getInviter(), null);
		view.dismiss();
	}
    
}

