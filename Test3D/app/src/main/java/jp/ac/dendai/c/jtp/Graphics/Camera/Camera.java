package jp.ac.dendai.c.jtp.Graphics.Camera;

import android.opengl.GLES20;
import android.opengl.Matrix;

import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
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
    private float[] viewProjMatrix = new float[16];
    private float[] transformMatrix = new float[16];
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
    public float[] getCameraMatrix(){
        return cameraMatrix;
    }
    public float[] getTransformMatrix(){return transformMatrix;}
    public float[] getViewProjMatrix(){return viewProjMatrix;}
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
    public void updateCamera(Shader shader) {
        if (update) {
            setCamera(this);
            GLES20.glUniformMatrix4fv(shader.getProjMatrixPosition(), 1,false,viewProjMatrix,0);
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
    public static void setCamera(Camera camera) {
        if (camera.getCameraMode() == Camera.CAMERA_MODE.PERSPECTIVE) {
            if (camera.getPersUpdate())
                setPerspectiveM(camera.getCameraMatrix(), 0, camera.getAngleOfView(), (double) GLES20Util.getWidth() / GLES20Util.getHight(), camera.getNear(), camera.getmFar());
            if (camera.getPosUpdate())
                Matrix.setLookAtM(camera.getTransformMatrix(), 0, camera.getPosition(Camera.POSITION.X),
                        camera.getPosition(Camera.POSITION.Y),
                        camera.getPosition(Camera.POSITION.Z),
                        camera.getLookPosition(Camera.POSITION.X),
                        camera.getLookPosition(Camera.POSITION.Y),
                        camera.getLookPosition(Camera.POSITION.Z),
                        0.0f, 1.0f, 0.0f);
            Matrix.multiplyMM(camera.getViewProjMatrix(), 0, camera.getCameraMatrix(), 0, camera.getTransformMatrix(), 0);
        } else {
            if (camera.getPosUpdate()) {
                Matrix.setIdentityM(camera.getTransformMatrix(), 0);
                Matrix.translateM(camera.getTransformMatrix(), 0, camera.getPosition(POSITION.X), camera.getPosition(POSITION.Y), camera.getPosition(POSITION.Z));
            }
            if (camera.getPersUpdate())
                Matrix.orthoM(camera.getCameraMatrix(), 0, -GLES20Util.getAspect(), GLES20Util.getAspect(), -1.0f, 1.0f, camera.getNear() / 100, camera.getmFar() / 100);
            Matrix.multiplyMM(camera.getViewProjMatrix(), 0, camera.getCameraMatrix(), 0, camera.getTransformMatrix(), 0);
        }
    }
}
