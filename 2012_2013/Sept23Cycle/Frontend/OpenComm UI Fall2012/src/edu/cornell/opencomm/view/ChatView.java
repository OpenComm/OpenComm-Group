package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ChatView extends View{
//	Side conversation:
//		•	We will have a sort of lasso mechanism for adding people to side conferences. Anytime the user touches his finger to a blank area and drags, it will start drawing a line. If that line goes through any of the icons, it should appear behind the icon, but the icon should fade into a “ghost.”
//		•	After the lasso has been triggered, clicking on any blank area will get rid of the effect. Clicking and dragging will instead continue the operation to the user can highlight more icons.
//		•	After the lasso has been triggered, clicking an icon and dragging it to a side conversation will add all of the highlighted icons to that side conversation. The dragged item should be the ghosts of all the highlighted icons cascaded, and upon dragging the original icons should be revealed. So only the “ghosts” move.
//		•	While the user is dragging icon(s), the lasso and line drawn through the icon will remain
//		•	If the user either releases the ghost icon or adds the icon in a side conversation, the lasso and line will disappear
//		•	Even if the user is adding multiple users into a side conversation, only one icon in the group needs to be dragged into the side chat icon for all of them to be added into the side conversation
//		•	Remove side chat preview. Tapping on a side conversation icon should just take you there.

	public ChatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	

}