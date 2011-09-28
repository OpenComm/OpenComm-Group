package opencomm.android2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class Button extends View {
    Context context;
	Bitmap image, image_clickdown;
	boolean clickedDown;
	int x, y, w, h;
	ButtonType buttonType;
	
	public enum ButtonType{main, menu, addPS, deletePS};
	
	public Button(Context context, ButtonType type, int image, int image_clickdown, int x, int y, int w, int h){
		super(context);
		buttonType = type;
		clickedDown = false;
		
		BitmapFactory.Options opts= new BitmapFactory.Options();
		opts.inJustDecodeBounds= true;
		this.image= BitmapFactory.decodeResource(context.getResources(),image);
		this.image_clickdown= BitmapFactory.decodeResource(context.getResources(),image_clickdown);
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Bitmap getTexture(){
		if(clickedDown)
			return image_clickdown;
		return image;
	}
	
	public void setClickedDown(boolean bool){
		clickedDown= bool;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}

	
	public boolean clickedInside(int mouseX, int mouseY){
		if ((mouseX >= x) && (mouseX <= (x + w)) && (mouseY>=y) && (mouseY<=(y + h)) )
			return true;
		return false;
	}
	
	public ButtonType getButtonType(){
		return buttonType;
	}
}
