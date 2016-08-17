package jp.ac.dendai.c.jtp.Graphics.Model;

/**
 * Created by Goto on 2016/08/04.
 */
public class Mesh {
    protected static float[] modelMatrix = new float[16];
    protected int vertexBufferObject = -1,indexBufferObject = -1;
    protected Face[] faces;
    public int getVBO(){return vertexBufferObject;}
    public int getIBO(){return indexBufferObject;}
    public Face[] getFaces(){return faces;}
}
