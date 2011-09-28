package edu.cornell.opencomm;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class Space {
	SpaceView spaceView;
	Context context;
	LinkedList<PersonView> people;

	public Space(Context c) {
		context = c;
		spaceView = new SpaceView(context, this);
		people = new LinkedList<PersonView>();
	}

	public void addSpaceView(SpaceView sv) {
		spaceView = sv;
		sv.createIcons(this);
		sv.invalidate();
	}
	
	protected Space(){
		
	}

	/** Add a person to this space by putting in their icon */
	public void add(Person p) {
		PersonView pv = new PersonView(context, p,
				(int) (Math.floor((SpaceView.w - 55) * Math.random())),
				(int) (Math.floor(SpaceView.mainScreenH * Math.random())), 55,
				55);
		this.people.add(pv);
	}

	public LinkedList<PersonView> getPeople() {
		return people;
	}

	public SpaceView getSpaceView() {
		return spaceView;
	}

	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.LTGRAY);

		for (PersonView p : people) {
			p.draw(canvas);
		}

		for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
			p.draw(canvas);
		}
	}
}
