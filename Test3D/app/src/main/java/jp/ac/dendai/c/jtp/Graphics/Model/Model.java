package jp.ac.dendai.c.jtp.Graphics.Model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model extends Mesh{
	public Model(Float[] vertex,Integer[] v_indices,Face[] faces){
		this.vertex = Model.makeFloatBuffer(vertex);
		this.index = Model.makeIntBuffer(v_indices);
		this.faces = faces;
		int[] bufferObjects = createBufferObject(2);
		vertexBufferObject = bufferObjects[0];
		indexBufferObject = bufferObjects[1];
		setVertexBufferObject();
		setIndexBufferObject();
	}

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
