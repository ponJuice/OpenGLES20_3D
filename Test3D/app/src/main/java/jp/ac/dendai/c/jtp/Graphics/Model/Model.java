package jp.ac.dendai.c.jtp.Graphics.Model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

public class Model {
	private enum Buffer_Mode{
		INDEX ,
		VERTEX
	}
	private ModelObject[] models;
	private int[] vertexBufferObject;
	private int[] indexBufferObject;

	public Model(ModelObject[] model) {
		models = model;
	}

	private void setBufferObject(){
		vertexBufferObject = GLES20Util.createBufferObject(models.length);
		indexBufferObject = GLES20Util.createBufferObject(models.length);
		for(int n = 0;n < models.length;n++){
			models[n].setVertexBufferObject(vertexBufferObject[n]);
			models[n].setIndexBufferObject(indexBufferObject[n]);
		}
	}

	public void draw(float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ) {
		for (int n = 0; n < models.length; n++) {
			models[n].draw(x, y, z, scaleX, scaleY, scaleZ, degreeX, degreeY, degreeZ);
		}
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

	public static Model createModel(ModelObject[] objects){
		Model model = new Model(objects);
		model.setBufferObject();
		return model;
	}
}
