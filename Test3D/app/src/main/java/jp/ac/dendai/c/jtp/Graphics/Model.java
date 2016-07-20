package jp.ac.dendai.c.jtp.Graphics;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

public class Model {
	private Bitmap bitmap;
	private int vertexBufferObject = -1,indexBufferObject = -1,normalBufferObject = -1;
	private String modelName;
	private String useMatelialName;
	private FloatBuffer vertex;
	private FloatBuffer normal;
	private FloatBuffer texture;
	private IntBuffer v_indices;
	private IntBuffer n_indices;
	private IntBuffer t_indices;
	public Model(String modelName,String useMatelialName,FloatBuffer vertex,FloatBuffer normal,FloatBuffer texture,
				 IntBuffer v_indices,IntBuffer  n_indices,IntBuffer  t_indices){
		this.modelName = modelName;
		this.useMatelialName = useMatelialName;
		this.vertex = vertex;
		this.normal = normal;
		this.texture = texture;
		this.v_indices = v_indices;
		this.n_indices = n_indices;
		this.t_indices = t_indices;

		bitmap = GLES20Util.createBitmap(255,0,0,255);
	}
	public Model(String modelName,String useMatelialName,Float[] vertex,Float[] normal,Float[] texture,
			Integer[] v_indices,Integer[]  n_indices, Integer[]  t_indices){
		this.modelName = modelName;
		this.useMatelialName = useMatelialName;
		this.vertex = makeFloatBuffer(vertex);
		this.normal = makeFloatBuffer(normal);
		this.texture = makeFloatBuffer(texture);
		this.v_indices = makeIntBuffer(v_indices);
		this.n_indices = makeIntBuffer(n_indices);
		this.t_indices = makeIntBuffer(t_indices);

		bitmap = GLES20Util.createBitmap(255,0,0,255);
	}

	public void setVertexBufferObject(int object){
		vertexBufferObject = object;
		GLES20Util.setVertexBuffer(vertexBufferObject,vertex,GLES20.GL_STATIC_DRAW);
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
		GLES20Util.DrawModel(x, y, z, scaleX, scaleY, scaleZ, degreeX, degreeY, degreeZ, bitmap, vertexBufferObject,normalBufferObject, indexBufferObject, v_indices.limit());
	}
	public static FloatBuffer makeFloatBuffer(float[] array){
    	if (array == null) throw new IllegalArgumentException();

    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * array.length);
    	byteBuffer.order(ByteOrder.nativeOrder());
    	FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
    	floatBuffer.put(array);
    	floatBuffer.position(0);
    	return floatBuffer;
	}
	public static FloatBuffer makeFloatBuffer(Float[] array){
    	if (array == null) throw new IllegalArgumentException();

    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * array.length);
    	byteBuffer.order(ByteOrder.nativeOrder());
    	FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
    	for(Float f : array){
    		floatBuffer.put((float)f);
    	}
    	floatBuffer.position(0);
    	return floatBuffer;
	}
	public static IntBuffer makeIntBuffer(int[] array){
		if (array == null) throw new IllegalArgumentException();

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.SIZE/8 * array.length);
		byteBuffer.order(ByteOrder.nativeOrder());
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(array).position(0);
		return intBuffer;
	}
	public static IntBuffer makeIntBuffer(Integer[] array){
		if (array == null) throw new IllegalArgumentException();

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.SIZE/8 * array.length);
		byteBuffer.order(ByteOrder.nativeOrder());
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		for(Integer f : array){
			intBuffer.put((int)f);
		}
		intBuffer.position(0);
		return intBuffer;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ModelName : ");
		sb.append(modelName);
		sb.append("\nMatelialName : ");
		sb.append(useMatelialName);
		sb.append("\n[vertex]\n");
		sb.append(vertex.toString());
		sb.append("\n[normal]\n");
		sb.append(normal.toString());
		sb.append("\n[texture]\n");
		sb.append(texture.toString());
		sb.append("\n[v_indices]\n");
		sb.append(v_indices.toString());
		sb.append("\n[n_indices]\n");
		sb.append(n_indices.toString());
		sb.append("\n[t_indices]\n");
		sb.append(t_indices.toString());
		
		return sb.toString();
	}
}
