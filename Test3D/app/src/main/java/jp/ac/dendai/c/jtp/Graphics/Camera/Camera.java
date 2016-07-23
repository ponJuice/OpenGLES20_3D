package jp.ac.dendai.c.jtp.Graphics.Camera;

import android.opengl.Matrix;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by テツヤ on 2016/07/23.
 */
public class Camera {
    public enum CAMERA_MODE{
        PERSPECTIVE,
        ORTHO
    }
    public enum POSITION{
        X,
        Y,
        Z
    }
    private float[] cameraMatrix = new float[16];
    private CAMERA_MODE camera_mode;
    private float x = -10f,y = 10f,z = 10f;
    private float lx = 0,ly = 0,lz = 0;
    private float angleOfView = 40;
    private boolean update = false;
    private boolean posUpdate = false;
    private boolean persUpdate = false;
    private float mNear=1f,mFar=100f;
    public Camera(CAMERA_MODE mode,float x,float y,float z){
        update = true;
        posUpdate = true;
        persUpdate = true;
        camera_mode = mode;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public float[] getMatrix(){
        return cameraMatrix;
    }
    public CAMERA_MODE getCameraMode(){
        return camera_mode;
    }
    public boolean getPosUpdate(){
        return  posUpdate;
    }
    public boolean getPersUpdate(){
        return persUpdate;
    }
    public float getPosition(POSITION flag){
        if(flag == POSITION.X)
            return x;
        else if(flag == POSITION.Y){
            return y;
        }else{
            return z;
        }
    }
    public float getAngleOfView(){
        return angleOfView;
    }
    public float getNear(){
        return mNear;
    }
    public float getmFar(){
        return mFar;
    }
    public void updateCamera() {
        if (update) {
            GLES20Util.setCamera(this);
            update = false;
            persUpdate = false;
            posUpdate = false;
        }
    }
    public void setNear(float value){
        update = true;
        persUpdate = true;
        mNear = value;
    }
    public void setFar(float value){
        mFar = value;
        persUpdate = true;
        update = true;
    }
    public void setAngleOfView(float value){
        this.angleOfView = value;
        persUpdate = true;
        update = true;
    }
    public void setCameraMode(CAMERA_MODE mode){
        update = true;
        persUpdate = true;
        this.camera_mode = mode;
    }
    public void setPosition(float x,float y,float z){
        this.x = x;
        this.y = y;
        this.z = z;
        update = true;
        posUpdate = true;
    }
    public void addPosition(float x,float y,float z){
        this.x += x;
        this.y += y;
        this.z += z;
        update = true;
        posUpdate = true;
    }
    public void setLookPosition(float x,float y,float z){
        lx = x;
        ly = y;
        lz = z;
    }
    public float getLookPosition(POSITION pos){
        if(pos == POSITION.X)
            return lx;
        else if(pos == POSITION.Y){
            return ly;
        }else{
            return lz;
        }
    }
    private static void setPerspectiveM(float[] m, int offset, double fovy, double aspect, double zNear, double zFar) {
        Matrix.setIdentityM(m, offset);
        double ymax = zNear * Math.tan(fovy * Math.PI / 360.0);
        double ymin = -ymax;
        double xmin = ymin * aspect;
        double xmax = ymax * aspect;
        Matrix.frustumM(m, offset, (float)xmin, (float)xmax, (float)ymin, (float)ymax, (float)zNear, (float)zFar);
    }
}
