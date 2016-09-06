package jp.ac.dendai.c.jtp.Graphics.Model;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/08/30.
 */
public class Texture {
    public enum COLOR{
        R,
        G,
        B
    }
    protected static final float[] plane = {
            /*-0.5f,-0.5f,0,
            0.5f,-0.5f,0,
            -0.5f,0.5f,0,
            0.5f,0.5f,0*/
            0,0,0,
            1f,0,0,
            0,1f,0,
            1f,1f,0
    };
    protected static final float[] texPos = {
            0.0f,1.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            1.0f,0.0f
    };
    protected static int[] bufferObject = {-1,-1};  //0:頂点 1:テクスチャ座標

    protected float r=0.5f,g=0.5f,b=0.5f;
    protected Bitmap texture;
    protected GLES20COMPOSITIONMODE mode;
    public Bitmap getTexture(){
        return texture;
    }
    public float getColor(COLOR col){
        if(col == COLOR.R)
            return r;
        else if(col == COLOR.G)
            return g;
        else
            return b;
    }
    public void setR(float value){
        r = value;
    }
    public void setG(float value){
        g = value;
    }
    public void setB(float value){
        b = value;
    }
    public void setTexture(Bitmap texture){
        this.texture = texture;
    }
    public void setBlendMode(GLES20COMPOSITIONMODE mode) {
        this.mode = mode;
    }
    public GLES20COMPOSITIONMODE getBlendMode(){
        return mode;
    }
    public int getVertexBufferObject(){
        if(bufferObject[0] == -1)
            throw new RuntimeException("[Texture] not create vertex buffer object");
        return bufferObject[0];
    }
    public int getTextureBufferObject(){
        if(bufferObject[0] == -1)
            throw new RuntimeException("[Texture] not create texture buffer object");
        return bufferObject[1];
    }
    public void setBufferObject(){
        FloatBuffer vertexBuffer = Mesh.makeFloatBuffer(plane);
        FloatBuffer texPosBuffer = Mesh.makeFloatBuffer(texPos);
        bufferObject = GLES20Util.createBufferObject(2);
        GLES20Util.setVertexBuffer(bufferObject[0], vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20Util.setVertexBuffer(bufferObject[1],texPosBuffer,GLES20.GL_STATIC_DRAW);
    }
    public void deleteBufferObject(){
        GLES20.glDeleteBuffers(2,bufferObject,0);
        bufferObject[0] = -1;
        bufferObject[1] = -1;
    }
    public Texture(Bitmap texture,GLES20COMPOSITIONMODE mode){
        this.texture = texture;
        this.mode = mode;
    }
}
