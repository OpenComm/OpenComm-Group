package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class BottomScrollBarView extends HorizontalScrollView {
	public BottomScrollBarView assoc;
	private OnScrollViewListener mListener;
	
	public BottomScrollBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
    public BottomScrollBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
    public BottomScrollBarView(Context context) {
		super(context);
		
	}
    
	/**Set the BottomScrollBarView that is associated with this view,
	 * which will scroll simultaneously with this view*/
    public void setAssoc(BottomScrollBarView as){
		assoc=as;
	}
	
	public void setOnScrollViewListener(OnScrollViewListener l){
		this.mListener=l;
	}
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy){
	  super.onScrollChanged(x, y, oldx, oldy);
	  mListener.onScrollChanged(this, x, y,oldx,oldy);
	  
		
	}
	public interface OnScrollViewListener{
		void onScrollChanged(BottomScrollBarView bv,int l, int t, int oldl, int oldt);
	}
	

}
