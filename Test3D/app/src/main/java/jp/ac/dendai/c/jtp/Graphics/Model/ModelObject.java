package jp.ac.dendai.c.jtp.Graphics.Model;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/21.
 */
public class ModelObject extends Mesh{
    private TouchListener touchListener;
    private FloatBuffer vertex;
    private IntBuffer v_indices;
    public void setTouchListener(TouchListener listener){
        touchListener = listener;
    }
    public void removeTouchListener(){
        touchListener = null;
    }
    public ModelObject(FloatBuffer vertex,IntBuffer v_indices,Face[] faces){
        this.vertex = vertex;
        this.v_indices = v_indices;
        this.faces = faces;
    }
    public ModelObject(Float[] vertex,Integer[] v_indices,Face[] faces){
        this.vertex = Model.makeFloatBuffer(vertex);
        this.v_indices = Model.makeIntBuffer(v_indices);

        this.faces = faces;
    }
    public void setVertexBufferObject(int object){
        vertexBufferObject = object;
        GLES20Util.setVertexBuffer(vertexBufferObject, vertex, GLES20.GL_STATIC_DRAW);
    }
    public void setIndexBufferObject(int object){
        indexBufferObject = object;
        GLES20Util.setIndexBuffer(indexBufferObject, v_indices, GLES20.GL_STATIC_DRAW);
    }
}
