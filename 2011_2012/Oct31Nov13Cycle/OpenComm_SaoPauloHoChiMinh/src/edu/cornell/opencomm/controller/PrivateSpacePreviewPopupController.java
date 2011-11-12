package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.PrivateSpacePreviewPopup;
import edu.cornell.opencomm.view.UserView;
/** Handles the user interaction with the Private Space Preview Popups
 * 
 * @author noranq 
 */
public class PrivateSpacePreviewPopupController {
	/** The main activity */
	public Context context;
	/** The Private Space Icon that this preview is representing */
	public PrivateSpaceIconView privateSpaceIconView = null;
	/** The Private Space Preview Popup object */
	private PrivateSpacePreviewPopup popup = null;
	/** The actual preview window itself */
	private PopupWindow privateSpacePreviewPopupWindow = null;
	
	/** Constructor: Private Space Preview Popup Controller */
	public PrivateSpacePreviewPopupController(Context context, PrivateSpaceIconView privateSpaceIconView){
		this.privateSpaceIconView = privateSpaceIconView;
		this.context = context;
	}
	
	/** Creates a PrivateSpacePreviewPopup, initializes it with the appropriate icons,
	 * and shows the popup centered over its corresponding Private Space Icon.
	 * 
	 * Should be called when the Private Space Icon has been clicked for the first time.
	 */
	/* TODO VINAY -
	 * (1) Make sure the popup preview is directly centered over the private space icon
	 * no matter the cellphone size
	 * (2) I tried fussing with values in the showLocation() method and I think I messed something up XD
	 * (3) Don't hardcode values if possible! Put values in Values class
	 * (4)  
	 */
	public void openPopupPreview() {
		try {
			LayoutInflater inflater = (LayoutInflater) ((MainApplication)context)
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			// Retrieve the layout and view items from the XML file
			View layout = inflater.inflate(R.layout.space_preview_popup,
					(ViewGroup) ((MainApplication)context).findViewById(R.id.private_space_preview_popup_layout));
			PrivateSpacePreviewPopup popupLayout = (PrivateSpacePreviewPopup)layout.findViewById(R.id.private_space_preview_popup);
			
			// Create a new Popup Preview Window based off this layout
			// TODO Vinay - Don't hardcode in values! Put these values in Values class
			privateSpacePreviewPopupWindow = new PopupWindow(layout, 150, 158, true);
			
			// Add in the appropriate icons
			PrivateSpaceIconView psv = privateSpaceIconView;
			if (psv != null) {
				ArrayList<UserView> personViews = new ArrayList<UserView>();
				for (UserView personView : psv.space.getAllIcons()) {
					personViews.add(personView);
				}

				popupLayout.setPersonViews(personViews);
			}

			// display the popup in the center
			privateSpacePreviewPopupWindow.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, 
					psv.getLeft(), psv.getTop() - privateSpacePreviewPopupWindow.getHeight());
			
			layout.setOnClickListener(onClickListener);

		} catch (Exception e) {
			//System.out.println(e.getMessage());
			Log.v("PrivateSpacePreviewPopupController", "Exception while inflating popup:\n" + e.getMessage());
		}
	} 
	
	
	/** Close the popup preview window.
	 * This method will be called when the private space icon is clicked after 
	 * the popup preview has already been open. 
	 */
	// TODO VINAY please find out why this method is never called!
	
	public void closePopupPreview(){
		privateSpacePreviewPopupWindow.dismiss();
		Log.v("PreviewPopupController", "dismissing window");
	} 
	
	
	/** When the PREVIEW POPUP is clicked, change the space that the 
	 * spaceview is representing to the space that this popup preview represents 
	 */
	/* TODO VINAY - doesn't seem to work :/ I think it's because your preview layout
	 * covers the whole screen? Or at least I think it prevents the computer from 
	 * detecting that you can click on a view or something. Please check it out - Nora 11/5
	 */
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		public void onClick(View v) {
			// close the popup preview
			closePopupPreview();
			// change the room that we are in
			MainApplication.screen.getSpaceViewController().changeSpace(privateSpaceIconView.getSpace());
		}
	};
	
	
	
	
}
