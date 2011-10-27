package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.layout;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;

public class ConfirmationView{
	private static String LOG_TAG = "OC_ConfirmationView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	PopupWindow window = null;

	public ConfirmationView(Context ctx) {
		context = ctx;
	}

	public ConfirmationView(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public void launch() {
		// LayoutInflater inflater = LayoutInflater.from(context);
		if (inflater == null)
			Log.v(LOG_TAG, "inflater null");
		else
			Log.v(LOG_TAG, "inflater not null");
		View confirmationLayout = inflater.inflate(
				R.layout.confirmation_layout, null);
		if (confirmationLayout != null) {
			Log.v(LOG_TAG, "confirmationLayout not null");
			confirmationLayout.setVisibility(View.VISIBLE);
			confirmationLayout.bringToFront();

			window = new PopupWindow(confirmationLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(confirmationLayout, 0, 1, 1);

			confirmationLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG, "confirmationLayout null");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			window.dismiss();

		}
	};
	
	/*protected void dispatchDraw(Canvas canvas) {

		canvas.se
		RectF drawRect = new RectF();
		drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());

		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);

		super.dispatchDraw(canvas);

		}*/
}
