package jp.ac.dendai.c.jtp.Graphics.Model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
    public Face[] getFaces(){return faces;}
}
