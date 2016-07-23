package jp.ac.dendai.c.jtp.Graphics.Model;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jp.ac.dendai.c.jtp.Graphics.Matelial;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/21.
 */
public class ModelObject {
    private Matelial matelial;
    private int vertexBufferObject = -1,indexBufferObject = -1,normalBufferObject = -1;
    private String modelName;
    private String useMatelialName;
    private FloatBuffer vertex;
    private FloatBuffer normal;
    private FloatBuffer texture;
    private IntBuffer v_indices;
    private IntBuffer n_indices;
    private IntBuffer t_indices;
    public ModelObject(String modelName,String useMatelialName,FloatBuffer vertex,FloatBuffer normal,FloatBuffer texture,
                 IntBuffer v_indices,IntBuffer  n_indices,IntBuffer  t_indices,Matelial matelial){
        this.modelName = modelName;
        this.useMatelialName = useMatelialName;
        this.vertex = vertex;
        this.normal = normal;
        this.texture = texture;
        this.v_indices = v_indices;
        this.n_indices = n_indices;
        this.t_indices = t_indices;

        this.matelial = matelial;
        if(matelial.texture == null)
            matelial.texture = GLES20Util.createBitmap((int)(matelial.diffuse[0]*255),(int)(matelial.diffuse[0]*255),(int)(matelial.diffuse[0]*255),(int)(matelial.d*255));
    }
    public ModelObject(String modelName,String useMatelialName,Float[] vertex,Float[] normal,Float[] texture,
                 Integer[] v_indices,Integer[]  n_indices, Integer[]  t_indices,Matelial matelial){
        this.modelName = modelName;
        this.useMatelialName = useMatelialName;
        this.vertex = Model.makeFloatBuffer(vertex);
        this.normal = Model.makeFloatBuffer(normal);
        this.texture = Model.makeFloatBuffer(texture);
        this.v_indices = Model.makeIntBuffer(v_indices);
        this.n_indices = Model.makeIntBuffer(n_indices);
        this.t_indices = Model.makeIntBuffer(t_indices);

        this.matelial = matelial;
        if(matelial.texture == null)
            matelial.texture = GLES20Util.createBitmap((int)(matelial.diffuse[0]*255),(int)(matelial.diffuse[0]*255),(int)(matelial.diffuse[0]*255),(int)(matelial.d*255));
    }

    public void setVertexBufferObject(int object){
        vertexBufferObject = object;
        GLES20Util.setVertexBuffer(vertexBufferObject,vertex, GLES20.GL_STATIC_DRAW);
    }
    public void setIndexBufferObject(int object){
        indexBufferObject = object;
        GLES20Util.setIndexBuffer(indexBufferObject, v_indices, GLES20.GL_STATIC_DRAW);
    }
    public void setNormalBufferObject(int object){
        normalBufferObject = object;
        GLES20Util.setVertexBuffer(normalBufferObject,normal,GLES20.GL_STATIC_DRAW);
    }

    public void draw(float x,float y,float z,float scaleX,float scaleY,float scaleZ,float degreeX,float degreeY,float degreeZ){
        GLES20Util.setEmmision(matelial.emmision);
        GLES20Util.DrawModel(x, y, z, scaleX, scaleY, scaleZ, degreeX, degreeY, degreeZ, matelial.texture, vertexBufferObject,normalBufferObject, indexBufferObject, v_indices.limit());
    }
}
