package jp.ac.dendai.c.jtp.Graphics.UI.Button;

import android.util.Log;

import org.w3c.dom.Text;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.Util.Rect;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by Goto on 2016/09/06.
 */
public class Button implements UI {
    private enum BUTTON_STATE{
        NON,
        DOWN,
        HOVER,
        UP,
    }
    protected Camera camera;
    protected BUTTON_STATE state = BUTTON_STATE.NON;
    protected ButtonListener listener;
    protected float hover_alpha = 0.5f;
    protected float non_hover_alpha = 1f;
    protected Rect rect;
    protected Texture tex;
    public Button(float left,float top,float right,float bottom){
        rect = new Rect(left,top,right,bottom);
    }
    public void setTop(float value){
        rect.setTop(value);
    }

    public void setLeft(float value){
        rect.setLeft(value);
    }

    public void setBottom(float value){
        rect.setBottom(value);
    }

    public void setRight(float value){
        rect.setRight(value);
    }
    public void setBackground(Texture tex){
        this.tex = tex;
    }
    public void setCamera(Camera camera){
        this.camera = camera;
    }

    @Override
    public void touch(Touch touch) {
        float x = camera.convertTouchPosToGLPosX(touch.getPosition(Touch.Pos_Flag.X));
        float y = camera.convertTouchPosToGLPosY(touch.getPosition(Touch.Pos_Flag.Y));
        if(touch.getTouchID() == -1){
            //指が離された
            if(state != BUTTON_STATE.NON && rect.contains(x,y)){
                state = BUTTON_STATE.UP;
            }
            return;
        }
        Log.d("button touch pos","device pos:"+"("+touch.getPosition(Touch.Pos_Flag.X)+","+ touch.getPosition(Touch.Pos_Flag.Y)+")"+"camera pos:("+x+","+y+")");
        if(rect.contains(x,y)){
            if(state == BUTTON_STATE.NON)
                state = BUTTON_STATE.DOWN;
            else if(state == BUTTON_STATE.DOWN)
                state = BUTTON_STATE.HOVER;
        }else{
            if(state == BUTTON_STATE.DOWN || state == BUTTON_STATE.HOVER)
                state = BUTTON_STATE.NON;
            else if(state == BUTTON_STATE.UP)
                state = BUTTON_STATE.NON;
        }
    }

    @Override
    public void proc() {
        if(state == BUTTON_STATE.UP){
            //listener.proc(this);
            state = BUTTON_STATE.NON;
        }
    }

    @Override
    public void draw(UiShader shader) {
        if(state == BUTTON_STATE.NON)
            shader.draw(tex,rect.getLeft(),rect.getBottom(),rect.getRight()-rect.getLeft(),rect.getTop() - rect.getBottom(),0,non_hover_alpha);
        else
            shader.draw(tex,rect.getLeft(),rect.getBottom(),rect.getRight()-rect.getLeft(),rect.getTop() - rect.getBottom(),0,hover_alpha);
    }
}
