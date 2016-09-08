package jp.ac.dendai.c.jtp.TouchUtil;

import android.content.res.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

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
	protected TouchListener tl;
	public Touch(){
		x = 0;
		y = 0;
		deltaX = 0;
		deltaY = 0;
		touchID = -1;
	}

	public void addTouchListener(TouchListener l){
		tl = l;
	}

	public void removeTouchListener(TouchListener l){
		//tl.remove(l);
	}

	public void setTouch(float x,float y,int touchID){
		this.x = getOrientPosition(x,y,Pos_Flag.X);
		this.y = getOrientPosition(x,y,Pos_Flag.Y);
		this.touchID = touchID;
		deltaX = 0;
		deltaY = 0;
	}
	public void updatePosition(float x,float y){
		deltaX = this.x - getOrientPosition(x,y,Pos_Flag.X);
		deltaY = this.y - getOrientPosition(x,y,Pos_Flag.Y);
		this.x = getOrientPosition(x,y,Pos_Flag.X);
		this.y = getOrientPosition(x,y,Pos_Flag.Y);
		if(tl != null)
			tl.execute(this);
	}
	private float getOrientPosition(float x,float y,Pos_Flag flag){
		if(flag == Pos_Flag.X){
			if(Input.getOrientation() == 2){
				return x;
			}else {
				return x;
			}
		}else{
			if(Input.getOrientation() == 2){
				return GLES20Util.getHight() - y;
			}else {
				return y;
			}
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
