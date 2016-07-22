package jp.ac.dendai.c.jtp.TouchUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Touch{
	public enum Pos_Flag{
		X,
		Y
	}
	protected float x;
	protected float y;
	protected float deltaX;
	protected float deltaY;
	protected int touchID;
	protected List<TouchListener> tl;
	public Touch(){
		x = 0;
		y = 0;
		deltaX = 0;
		deltaY = 0;
		touchID = -1;
		tl = new LinkedList<TouchListener>();
	}

	public void addTouchListener(TouchListener l){
		tl.add(l);
	}

	public void removeTouchListener(TouchListener l){
		tl.remove(l);
	}

	public void setTouch(float x,float y,int touchID){
		this.x = x;
		this.y = y;
		this.touchID = touchID;
		deltaX = 0;
		deltaY = 0;
	}
	public void updatePosition(float x,float y){
		deltaX = this.x - x;
		deltaY = this.y - y;
		this.x = x;
		this.y = y;
		for (TouchListener l : tl ) {
			l.execute(this);
		}
	}
	public float getPosition(Pos_Flag pos){
		if(pos == Pos_Flag.X){
			return x;
		}
		else{
			return y;
		}
	}
	public float getDelta(Pos_Flag pos){
		if(pos == Pos_Flag.X){
			return deltaX;
		}
		else{
			return deltaY;
		}
	}

	public float getDeltaAbs(Pos_Flag pos){
		if(pos == Pos_Flag.X){
			return Math.abs(deltaX);
		}
		else{
			return Math.abs(deltaY);
		}
	}

	public int getTouchID(){
		return touchID;
	}
	public void removeTouch(){
		touchID = -1;
	}
	@Override
	public String toString(){
		return "pos ["+x+":"+y+"] delta["+deltaX+":"+deltaY+"] touchID="+touchID;

	}
}
