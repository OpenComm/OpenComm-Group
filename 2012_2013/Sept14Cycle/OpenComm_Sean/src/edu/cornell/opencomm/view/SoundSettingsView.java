package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.SoundSettingsController;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.BottomScrollBarView.OnScrollViewListener;

public class SoundSettingsView {
	private static String LOG_TAG = "OC_SoundSettingsView"; // for error
															// checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private SoundSettingsController soundSettingsController = new SoundSettingsController(
			this);
	private View soundSettingsLayout = null;
	ArrayList<VerticalSlideBar> sideChatVolumeControls = null;

	public SoundSettingsView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	private void initEventsAndProperties() {
		// create property soundSettingsLayout from infalter and store it as a
		// property
		if (inflater != null) {
			View soundSettingsViewFromInflater = inflater.inflate(
					R.layout.sound_settings, null);
			if (soundSettingsViewFromInflater != null) {
				this.soundSettingsLayout = soundSettingsViewFromInflater;
			}
			
			initializeSideChatVolumeControlsArray();
		}
	}

	private void initializeSideChatVolumeControlsArray() {
		sideChatVolumeControls = new ArrayList<VerticalSlideBar>();
		
		VerticalSlideBar volumeControlSideChat1 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat1);
		VerticalSlideBar volumeControlSideChat2 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat2);
		VerticalSlideBar volumeControlSideChat3 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat3);
		VerticalSlideBar volumeControlSideChat4 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat4);
		VerticalSlideBar volumeControlSideChat5 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat5);
		VerticalSlideBar volumeControlSideChat6 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat6);
		VerticalSlideBar volumeControlSideChat7 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat7);
		VerticalSlideBar volumeControlSideChat8 = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.volumeControlSideChat8);
		
		sideChatVolumeControls.add(volumeControlSideChat1);
		sideChatVolumeControls.add(volumeControlSideChat2);
		sideChatVolumeControls.add(volumeControlSideChat3);
		sideChatVolumeControls.add(volumeControlSideChat4);
		sideChatVolumeControls.add(volumeControlSideChat5);
		sideChatVolumeControls.add(volumeControlSideChat6);
		sideChatVolumeControls.add(volumeControlSideChat7);
		sideChatVolumeControls.add(volumeControlSideChat8);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public PopupWindow getWindow() {
		return window;
	}

	public void setWindow(PopupWindow window) {
		this.window = window;
	}
/**Crystal: set the BottomBar in the sound_setting*/
	public void setBottomBar(Context context){
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Values.privateSpaceButtonW,  Values.privateSpaceButtonW);
	        lp.setMargins(0, 0, Values.iconBorderPaddingH, 0);
			for(int i=0;i<PrivateSpaceIconView.allPSIcons.size(); i++){
				SoundBottomBarIconView sv= new SoundBottomBarIconView(context);
			sv.setLayoutParams(new LinearLayout.LayoutParams(Values.privateSpaceButtonW,  Values.privateSpaceButtonW));
			sv.setPadding(Values.iconBorderPaddingH, Values.iconBorderPaddingV,Values.iconBorderPaddingH, Values.iconBorderPaddingV);
			LinearLayout bar=(LinearLayout)soundSettingsLayout.findViewById(R.id.icon_clone);
			bar.addView(sv, lp);
			}
			final BottomScrollBarView bv1=(BottomScrollBarView) soundSettingsLayout.findViewById(R.id.horizontalScrollView1);
			final BottomScrollBarView bv2=(BottomScrollBarView) soundSettingsLayout.findViewById(R.id.bottom_scroll);
			bv1.setAssoc(bv2);
		     bv2.setAssoc(bv1); 
			bv1.setOnScrollViewListener(new OnScrollViewListener(){

				@Override
				public void onScrollChanged(BottomScrollBarView bv, int l,
						int t, int oldl, int oldt) {
					bv1.assoc.scrollTo(l, t);
				}
		    	 
		     });
			bv2.setOnScrollViewListener(new OnScrollViewListener(){

				@Override
				public void onScrollChanged(BottomScrollBarView bv, int l,
						int t, int oldl, int oldt) {
					Log.d("scroll","I changed");
					bv2.assoc.scrollTo(l, t);
				}
		    	 
		     });
		     
	}

	/*
	 * this method launches the confirmation layout on a popupwindiw, can be
	 * changed later to launch like a normal view
	 */
	public void launch() {
		if (inflater != null && soundSettingsLayout != null) {
			setBottomBar(Space.getMainSpace().getContext());
			
			window = new PopupWindow(soundSettingsLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(soundSettingsLayout, 0, 1, 1);
			soundSettingsLayout.setOnClickListener(onClickListener);
			
			/*
			 * Iterates over the spaces and makes only the volume controls for existing spaces visible
			 */
			soundSettingsController.setSideChatVolumeControlsOnView(sideChatVolumeControls);
			
		} else {
			Log.v(LOG_TAG,
					"Cannot launch sound settings view as inflater layout is null");
		}
	}
	
	public VerticalSlideBar getMainChatVolumeControl() {
		VerticalSlideBar mainChatVolumeControl = (VerticalSlideBar)soundSettingsLayout.findViewById(R.id.mainChatVolumeSlide);
		
		return mainChatVolumeControl;
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			soundSettingsController.handlePopupWindowClicked();
		}
	};

}
