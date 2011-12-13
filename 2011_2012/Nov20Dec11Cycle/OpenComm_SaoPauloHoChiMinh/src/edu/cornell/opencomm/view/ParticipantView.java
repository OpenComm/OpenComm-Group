package edu.cornell.opencomm.view;

import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.XMPPException;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

/**
 * A pop-up that shows all the Users currently in a Space. May be merged into
 * another class later.
 * 
 * @author Kris
 * 
 */
public class ParticipantView {
	public static CharSequence[] usersInSpace; // Users currently in Space
	public static boolean[] userSelected; // array of boolean for user selection

	public static final String TAG = "ParticipantView";

	private static SpaceView spaceView;
	private static Context context;

	/**
	 * A ParticipantView associated with the Space represented by the SpaceView
	 */
	public ParticipantView(Context context, SpaceView spaceView) {
		ParticipantView.context = context;
		ParticipantView.spaceView = spaceView;
	}

	public static void deleteParticipants() {
		if (context == null) {
			Log.v(TAG, "Context is null!");
		}
		updateParticipants();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
			public void onClick(DialogInterface dialog, int clicked,
					boolean selected) {
				// Do nothing
			}
		}

		class DialogButtonClickHandler implements DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int clicked) {
				switch (clicked) {
				case DialogInterface.BUTTON_POSITIVE:
					deleteFromParticipants();
					break;
				}
			}
		}
		
		class DialogCancelButtonClickHandler implements DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
           }
		}
		
		builder.setTitle("Please select users to remove")
				.setMultiChoiceItems(usersInSpace, userSelected,
						new DialogSelectionClickHandler())
				.setPositiveButton("Ok", new DialogButtonClickHandler())
				.setNegativeButton("Cancel", new DialogCancelButtonClickHandler())
				.create();
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void grantOwnership(final Space space, final boolean isMainSpace) {
		if (context == null) {
			Log.v(TAG, "Context is null!");
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		class DialogButtonClickHandler implements DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int clicked) {
				makeOwner(clicked);
				MainApplication.screen.getActivity().delPrivateSpaceUI(space, isMainSpace);
				spaceView.getSpace().getParticipantController().leaveSpace(
						spaceView.getSpace().equals(Space.getMainSpace()));
			}
		}
		
		class DialogCancelButtonClickHandler implements DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
           }
		}
		
		updateParticipants();
		builder.setTitle("Please select new owner")
				.setItems(usersInSpace, new DialogButtonClickHandler())
				.setNegativeButton("Cancel", new DialogCancelButtonClickHandler())
				.create();
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*public static void leaveOrDestroy(final Space space) {
		if (context == null) {
			Log.v(TAG, "Context is null!");
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		class DialogButtonClickHandler implements
				DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int clicked) {
				Log.v(TAG, "Clicked = " + clicked);
								boolean isMainSpace = false;
								if(space==Space.getMainSpace())
									isMainSpace= true;
				switch (clicked) {
				case (0):
					Log.v(TAG, "clicked on Destroy!");
					//try {
						MainApplication.screen.getSpace().getSpaceController().deleteSpace();
						MainApplication.screen.getActivity().delPrivateSpaceUI(space, isMainSpace);
					//} catch (XMPPException e) {
					//	Log.v(TAG, "Can't destroy the space!");
					//}
					//if (spaceView.getSpace().equals(Space.getMainSpace())){
						Intent i = new Intent(MainApplication.screen.getSpace().getContext(),
								DashboardView.class);
						MainApplication.screen.getSpace().getContext().startActivity(i);
					//}
					//else{
						//TODO: update View to conference
					//}
						
					break;
				case (1):
					grantOwnership(space, isMainSpace);
					break;
				}
			}
		}
		updateParticipants();
		builder.setTitle("Would you rather destroy the chat or pass ownership?")
				.setItems(Values.whatHappen, new DialogButtonClickHandler()).create();
		AlertDialog alert = builder.create();
		alert.show();
	}*/

	private static void updateParticipants() {
		Collection<User> participants = MainApplication.screen.getSpace()
				.getAllParticipants().values();
		Iterator<User> participantItr = participants.iterator();
		usersInSpace = new CharSequence[participants.size() - 1];
		int i = 0;
		while (participantItr.hasNext()) {
			String next = participantItr.next().getNickname();
			if (!next.equals(MainApplication.user_primary.getNickname())) {
				Log.v("annie", " "+!next.equals(MainApplication.user_primary.getNickname()));
				usersInSpace[i++] = next;
			}
		}
		userSelected = new boolean[usersInSpace.length];
	} // end updateParticipants method

	private static void deleteFromParticipants() {
		for (int i = 0; i < userSelected.length; i++) {
			if (userSelected[i]) {
				User kickMe = MainApplication.screen.getSpace().getAllNicksnames()
						.get(usersInSpace[i]);
				try {
					MainApplication.screen.getSpace().getKickoutController()
							.kickoutUser(kickMe, Network.DEFAULT_KICKOUT);
				} catch (XMPPException e) {
					Log.v(TAG, "Couldn't kickout " + usersInSpace[i]);
					Log.v(TAG, "exception: " + e.getMessage());
				}
			}
		}
	} // end deleteFromParticipants method

	private static void makeOwner(int clicked) {
		String newOwner = MainApplication.screen.getSpace().getAllNicksnames()
				.get(usersInSpace[clicked]).getUsername();
		MainApplication.screen.getSpace().getParticipantController()
				.grantOwnership(newOwner, true);
	} // end makeOwner method
}
