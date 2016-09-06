package jp.ac.dendai.c.jtp.Graphics.Model;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/08/04.
 */
public abstract class Mesh {
    protected FloatBuffer vertex;
    protected IntBuffer index;
    public abstract int getVBO();
    public abstract int getIBO();
    public FloatBuffer getVertexBuffer(){return vertex;}
    public IntBuffer getIndexBuffer(){return index;}
    protected void setVertexBufferObject(){
        GLES20Util.setVertexBuffer(getVBO(), vertex, GLES20.GL_STATIC_DRAW);
    }
    protected void setIndexBufferObject(){
        GLES20Util.setIndexBuffer(getIBO(), index, GLES20.GL_STATIC_DRAW);
    }
    protected int[] createBufferObject(int num){
        // バッファオブジェクトを作成する
        int[] bufferObjects = new int[num];
        GLES20.glGenBuffers(num,bufferObjects, 0);
        return bufferObjects;
    }
    public abstract Face[] getFaces();
    public static FloatBuffer makeFloatBuffer(float[] array) {
        if (array == null) throw new IllegalArgumentException();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * array.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static FloatBuffer makeFloatBuffer(Float[] array) {
        if (array == null) throw new IllegalArgumentException();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * array.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        for (Float f : array) {
            floatBuffer.put((float) f);
        }
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static IntBuffer makeIntBuffer(int[] array) {
        if (array == null) throw new IllegalArgumentException();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.SIZE / 8 * array.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(array).position(0);
        return intBuffer;
    }

    public static IntBuffer makeIntBuffer(Integer[] array) {
        if (array == null) throw new IllegalArgumentException();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.SIZE / 8 * array.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        for (Integer f : array) {
            intBuffer.put((int) f);
        }
        intBuffer.position(0);
        return intBuffer;
    }
}
