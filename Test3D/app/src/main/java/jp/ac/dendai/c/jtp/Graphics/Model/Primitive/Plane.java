package jp.ac.dendai.c.jtp.Graphics.Model.Primitive;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Material.Matelial;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by テツヤ on 2016/09/03.
 */
public class Plane extends Mesh {
    protected static final float[] f_plane = {
             0.5f, 0.0f, 0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f,
            -0.5f, 0.0f,-0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,
            -0.5f, 0.0f, 0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,
             0.5f, 0.0f,-0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,
    };
    protected static final int[] f_index = {
            0,1,2,0,3,1
    };
    protected static int vertexBufferObject = -1;
    protected static int indexBufferObject = -1;
    protected static Face[] face = new Face[1];

    public Plane(){
        face[0] = new Face(new Matelial(),0,6);
    }

    @Override
    public Face[] getFaces(){return face;};

    public void useBufferObject(){
        if(vertexBufferObject != -1 && indexBufferObject != -1)
            return;
        int object[] = GLES20Util.createBufferObject(2);
        vertexBufferObject = object[0];
        indexBufferObject = object[1];
        vertex = makeFloatBuffer(f_plane);
        index = makeIntBuffer(f_index);
        setIndexBufferObject();
        setVertexBufferObject();
        GLES20Util.setVertexBuffer(vertexBufferObject,vertex, GLES20.GL_STATIC_DRAW);
        GLES20Util.setIndexBuffer(indexBufferObject,index,GLES20.GL_STATIC_DRAW);
    }

    public void setImage(Bitmap bitmap){
        face[0].matelial.tex_diffuse = bitmap;
    }

    @Override
    public int getVBO() {
        return vertexBufferObject;
    }

    @Override
    public int getIBO() {
        return indexBufferObject;
    }
}
