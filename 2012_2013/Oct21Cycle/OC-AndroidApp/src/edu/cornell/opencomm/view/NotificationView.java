package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationView {

	private Context context;

	public NotificationView(Context context) {
		this.context = context;
	}

	/**
	 * @param padding
	 * @param colorBean
	 * @return
	 */
	private TextView createTextView(NotificationView.Padding padding,
			NotificationView.ColorBean colorBean) {
		TextView textView = new TextView(context);
		textView.setPadding(padding.left, padding.top, padding.right,
				padding.top);
		textView.setBackgroundColor(colorBean.background);
		textView.setTextColor(colorBean.text);
		return textView;
	}

	private Toast creatToastView(View textView) {
		Toast toastView = new Toast(context);
		toastView.setView(textView);
		toastView.setDuration(Toast.LENGTH_LONG);
		toastView.setGravity(Gravity.TOP, 0, 530);
		return toastView;
	}

	public void launch(String message, NotificationView.Padding padding) {
		launch(message,padding,new NotificationView.ColorBean());
	}

	public void launch(String message) {
		launch(message, new NotificationView.Padding(),new NotificationView.ColorBean());
	}

	public void launch(String message, NotificationView.Padding padding,
			NotificationView.ColorBean colorBean) {
		TextView textView = createTextView(padding, colorBean);
		textView.setText(message);
		Toast toast = creatToastView(textView);
		toast.show();
	}

	public class Padding {
		public int left = 10;
		public int right = 10;
		public int top = 10;
		public int bottom = 10;
	}

	/**
	 * @author ankitsingh : Color has to be one of #RRGGBB #AARRGGBB 'red',
	 *         'blue', 'green', 'black', 'white', 'gray', 'cyan', 'magenta',
	 *         'yellow', 'lightgray', 'darkgray
	 */
	public class ColorBean {
		public int background = Color.BLACK;
		public int text = Color.WHITE;
	}
}
