package opencomm.android2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PersonView {
	Context context;
	Person person;
	int x, y, w, h;
	Bitmap image;
	
	public PersonView(Context context, Person person, int x, int y, int w, int h){
		this.context = context;
		this.person= person;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		
		BitmapFactory.Options opts= new BitmapFactory.Options();
		opts.inJustDecodeBounds= true;
		this.image= BitmapFactory.decodeResource(context.getResources(), person.getImage());
	}
	
	public Bitmap getIcon(){
		return image;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getW(){
		return w;
	}
	public int getH(){
		return h;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y=y;
	}
	
	public boolean clickedInside(int mouseX, int mouseY){
		if((mouseX>=x) && (mouseX<=(x+w)) && (mouseY>=y) && (mouseY<=(y+h)))
			return true;
		return false;
	}
}
