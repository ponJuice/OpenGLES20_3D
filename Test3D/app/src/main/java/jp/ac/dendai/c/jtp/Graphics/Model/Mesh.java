package jp.ac.dendai.c.jtp.Graphics.Model;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/08/04.
 */
public class Mesh {
    protected static float[] modelMatrix = new float[16];
    protected FloatBuffer vertex;
    protected IntBuffer index;
    protected int vertexBufferObject,indexBufferObject;
    protected Face[] faces;
    public int getVBO(){return vertexBufferObject;}
    public int getIBO(){return indexBufferObject;}
    protected void setVertexBufferObject(){
        GLES20Util.setVertexBuffer(vertexBufferObject, vertex, GLES20.GL_STATIC_DRAW);
    }
    protected void setIndexBufferObject(){
        GLES20Util.setIndexBuffer(indexBufferObject, index, GLES20.GL_STATIC_DRAW);
    }
    protected int[] createBufferObject(int num){
        // バッファオブジェクトを作成する
        int[] bufferObjects = new int[num];
        GLES20.glGenBuffers(num,bufferObjects, 0);
        return bufferObjects;
    }
    public Face[] getFaces(){return faces;}
}
