package jp.ac.dendai.c.jtp.Graphics.UI.Util;

/**
 * Created by Goto on 2016/09/06.
 */
public class Rect {
    protected float cx,cy;
    protected float top,left,bottom,right;
    public Rect(float left,float top,float right,float bottom){
        setRect(left,top,right,bottom);
    }
    public Rect(float centerX,float centerY,float topLength,float leftLength,float bottomLength,float rightLength){
        setRect(centerX, centerY, topLength, leftLength, bottomLength, rightLength);
    }

    public void setRect(float centerX,float centerY,float topLength,float leftLength,float bottomLength,float rightLength){
        top = centerY+topLength;
        left = centerX-leftLength;
        bottom = centerY-bottomLength;
        right = centerX+rightLength;
        calcCenter();
    }

    public float getRight() {
        return right;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getTop() {
        return top;
    }

    public float getLeft() {
        return left;
    }

    public float getBottom() {
        return bottom;
    }

    public void setRect(float left,float top,float right,float bottom){
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        calcCenter();
    }

    public void setTop(float value){
        top = value;
        calcCenter();
    }

    public void setLeft(float value){
        left = value;
        calcCenter();
    }

    public void setBottom(float value){
        bottom = value;
        calcCenter();
    }

    public void setRight(float value){
        right = value;
        calcCenter();
    }

    public void calcCenter(){
        cx = this.left - this.right;
        cy = this.top - this.bottom;
    }
    public boolean contains(float x,float y){
        return left <= x && x <= right && bottom <= y && y <= top;
    }
}
