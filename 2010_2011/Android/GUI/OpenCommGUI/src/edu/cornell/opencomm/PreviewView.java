package edu.cornell.opencomm;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.widget.ImageButton;

public class PreviewView extends ImageButton {

	public PreviewView(Context context) {
		super(context);
	}

	public void draw(Canvas canvas, LinkedList<PersonView> people) {
		super.onDraw(canvas);

		RectShape rect = new RectShape();
		ShapeDrawable normalShape = new ShapeDrawable(rect);
		normalShape.getPaint().setColor(Color.BLUE);
		normalShape.setBounds(0, -0, 140, 140);
		normalShape.draw(canvas);
		int x = 5;
		int y = 5;
		for (PersonView p : people) {
			Bitmap newimage = Bitmap.createScaledBitmap(p.image, 40, 40, false);
			canvas.drawBitmap(newimage, x, y, null);

			if (x == 95) {
				x = 5;
				y += 45;
			} else
				x += 45;

		}
	}
}
