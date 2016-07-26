package jp.ac.dendai.c.jtp.Graphics.Model;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Graphics.Face;
import jp.ac.dendai.c.jtp.Graphics.Matelial;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/21.
 */
public class ModelObject {
    private LinkedList<ModelObject> children;
    private TouchListener touchListener;
    private Face[] faces;
    private int vertexBufferObject = -1,indexBufferObject = -1;
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
        children = new LinkedList<>();
    }
    public ModelObject(Float[] vertex,Integer[] v_indices,Face[] faces){
        this.vertex = Model.makeFloatBuffer(vertex);
        this.v_indices = Model.makeIntBuffer(v_indices);

        this.faces = faces;
        children = new LinkedList<>();
    }
    public void addChildren(ModelObject model){
        children.add(model);
    }
    public void removeChildren(ModelObject model){
        children.remove(model);
    }
    public void setVertexBufferObject(int object){
        vertexBufferObject = object;
        GLES20Util.setVertexBuffer(vertexBufferObject, vertex, GLES20.GL_STATIC_DRAW);
    }
    public void setIndexBufferObject(int object){
        indexBufferObject = object;
        GLES20Util.setIndexBuffer(indexBufferObject, v_indices, GLES20.GL_STATIC_DRAW);
    }

    public void draw(float x,float y,float z,float scaleX,float scaleY,float scaleZ,float degreeX,float degreeY,float degreeZ){
        //GLES20Util.setEmmision(matelial.emmision);
        GLES20Util.DrawModel(x, y, z, scaleX, scaleY, scaleZ, degreeX, degreeY, degreeZ, faces, vertexBufferObject,indexBufferObject, v_indices.limit());
    }
}
