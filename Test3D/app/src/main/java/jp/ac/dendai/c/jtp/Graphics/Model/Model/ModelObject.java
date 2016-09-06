package jp.ac.dendai.c.jtp.Graphics.Model.Model;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/21.
 */
public class ModelObject extends Mesh {
    protected Face[] face;
    protected int vertexBufferObject = -1,indexBufferObject = -1;
    public ModelObject(FloatBuffer vertex,IntBuffer v_indices,Face[] faces){
        this.vertex = vertex;
        this.index = v_indices;
        this.face = faces;
    }
    public ModelObject(Float[] vertex,Integer[] v_indices,Face[] faces){
        this.vertex = Model.makeFloatBuffer(vertex);
        this.index = Model.makeIntBuffer(v_indices);
        this.face = faces;
    }
    public void useBufferObject(){
        int object[] = GLES20Util.createBufferObject(2);
        vertexBufferObject = object[0];
        indexBufferObject = object[1];
        GLES20Util.setVertexBuffer(vertexBufferObject,vertex,GLES20.GL_STATIC_DRAW);
        GLES20Util.setIndexBuffer(indexBufferObject,index,GLES20.GL_STATIC_DRAW);
    }

    @Override
    public int getVBO() {
        return vertexBufferObject;
    }

    @Override
    public int getIBO() {
        return indexBufferObject;
    }

    @Override
    public Face[] getFaces() {
        return face;
    }
}
