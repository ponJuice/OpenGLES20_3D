package jp.ac.dendai.c.jtp.openglesutil.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Face;
import jp.ac.dendai.c.jtp.openglesutil.graphic.Image;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;


public class GLES20Util extends abstractGLES20Util {
	private static Paint paint;
	private static Canvas canvas;
	private static Rect rect = new Rect(0,0,0,0);
	private static float[] line = new float[]{
			0,0,0,0,0,1,0,0,
			1,1,1,0,0,1,0,0
	};
	public enum GLES20UTIL_MODE{
		POSX,
		POSY
	}
	public GLES20Util(){
		Log.d("GLES20Util","Constract");
	}

	public static float screenToInnerPosition(float value,GLES20UTIL_MODE mode){
		if(value == 0)
			return 0;
		if(mode == GLES20UTIL_MODE.POSX){
			return  GLES20Util.getWidth_gl()/GLES20Util.getWidth()*value;
		}
		else if(mode == GLES20UTIL_MODE.POSY){
			return GLES20Util.getHeight_gl()/GLES20Util.getHight()*(GLES20Util.getHight()-value);
		}
		return 0;
	}
	//文字列描画
	public static Bitmap stringToBitmap(String text,float size,int r,int g,int b){
		//描画するテキスト
		paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(r, g, b));
		paint.setTextSize(size*100);
		paint.getTextBounds(text, 0, text.length(), new Rect());
		FontMetrics fm = paint.getFontMetrics();
		//テキストの表示範囲を設定

		int textWidth = (int) paint.measureText(text);
		int textHeight = (int) (Math.abs(fm.top) + fm.bottom);
		Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);

		//キャンバスからビットマップを取得
		canvas = new Canvas(bitmap);
		canvas.drawText(text, 0, Math.abs(fm.top), paint);

		return bitmap;
	}

	public static void DrawString(String string,int size,int r,int g,int b,float alpha,float x,float y,GLES20COMPOSITIONMODE mode){
		Bitmap bitmap = stringToBitmap(string,size,r,g,b);
		//Log.d("DrawString",String.valueOf(bitmap.getWidth()));
		DrawGraph(x, y, bitmap.getWidth() / 1000f, bitmap.getHeight() / 1000f, bitmap, alpha, mode);
	}

	/**
	 * 画像表示
	 */
	//画像表示
	public static void DrawGraph(float startX,float startY,float lengthX,float lengthY,Bitmap image,float alpha,GLES20COMPOSITIONMODE mode){

		float scaleX = lengthX;
		float scaleY = lengthY;

		//float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, startX, startY, 0.0f);
		Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, 1.0f);
		setShaderModelMatrix(modelMatrix);

		setOnTexture(image, alpha);

		mode.setBlendMode();
		GLES20.glBindBuffer(ma_Position, planeBufferObject);
		GLES20.glVertexAttribPointer(ma_Position,2,GLES20.GL_FLOAT,false,0,0);
		GLES20.glEnableVertexAttribArray(ma_Position);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);	//描画
		GLES20.glDisableVertexAttribArray(ma_Position);
	}

	public static void setCamera(Camera camera){
		if(camera.getCameraMode() == Camera.CAMERA_MODE.PERSPECTIVE){
			if(camera.getPersUpdate())
				setPerspectiveM(u_ProjMatrix,0,camera.getAngleOfView(),(double)Width/Height,camera.getNear(),camera.getmFar());
			if(camera.getPosUpdate())
				Matrix.setLookAtM(camera.getMatrix(), 0, camera.getPosition(Camera.POSITION.X),
														camera.getPosition(Camera.POSITION.Y),
														camera.getPosition(Camera.POSITION.Z),
														camera.getLookPosition(Camera.POSITION.X),
														camera.getLookPosition(Camera.POSITION.Y),
														camera.getLookPosition(Camera.POSITION.Z),
														0.0f, 1.0f, 0.0f);
			Matrix.multiplyMM(viewProjMatrix, 0, u_ProjMatrix, 0, camera.getMatrix(), 0);
		}
		else{
			if(camera.getPosUpdate()) {
				Matrix.setIdentityM(camera.getMatrix(), 0);
				Matrix.translateM(camera.getMatrix(), 0, -width_gl / 2f, -height_gl / 2f, 0);
			}
			if(camera.getPersUpdate())
				Matrix.orthoM(u_ProjMatrix,0,-aspect,aspect,-1.0f,1.0f,camera.getNear()/100,camera.getmFar()/100);
			Matrix.multiplyMM(viewProjMatrix,0,u_ProjMatrix,0,camera.getMatrix(),0);
			//viewProjMatrix = u_ProjMatrix;
		}
		//シェーダにワールド行列を設定
		setShaderProjMatrix();
	}

	public static void DrawLine(FloatBuffer vertex,float x,float y,float z,float lengthX,float lengthY,float lengthZ,float[] color,float a,float width,GLES20COMPOSITIONMODE mode){
		Matrix.setIdentityM(normalMatrix,0);
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, y, z);
		Matrix.scaleM(modelMatrix, 0, lengthX, lengthY, lengthZ);
		setShaderModelMatrix(modelMatrix);
		setShaderNormalMatrix(normalMatrix);
		setEmmision(color);
		setOnTexture(null, a);
		mode.setBlendMode();
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
		GLES20.glLineWidth(width);
		GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, FSIZE * 8, vertex.position(0));
		GLES20.glVertexAttribPointer(va_Normal, 3, GLES20.GL_FLOAT, true, FSIZE * 8, vertex.position(3));
		vertex.position(6);
		GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, true, FSIZE * 8, vertex.position(6));
		GLES20.glEnableVertexAttribArray(ma_Position);
		GLES20.glEnableVertexAttribArray(va_Normal);
		GLES20.glEnableVertexAttribArray(ma_texCoord);
		GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, 2);
		GLES20.glDisableVertexAttribArray(ma_Position);
		GLES20.glDisableVertexAttribArray(va_Normal);
		GLES20.glDisableVertexAttribArray(ma_texCoord);
	}

	public static void DrawGraph(float startX,float startY,float lengthX,float lengthY,float degree,Bitmap image,float alpha,GLES20COMPOSITIONMODE mode){
		float scaleX = lengthX;
		float scaleY = lengthY;

		//float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.setIdentityM(normalMatrix, 0);
		Matrix.translateM(modelMatrix, 0, startX, startY, 0.0f);
		Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, 1.0f);
		Matrix.rotateM(modelMatrix, 0, degree, 0, 0, 1);
		setShaderModelMatrix(modelMatrix);

		setOnTexture(image, alpha);

		mode.setBlendMode();
		GLES20.glBindBuffer(ma_Position, planeBufferObject);
		GLES20.glEnableVertexAttribArray(ma_Position);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);	//描画
	}

	public static void DrawModel(float x,float y,float z,
								 float scaleX,float scaleY, float scaleZ,
								 float degreeX,float degreeY,float degreeZ,
								 Face[] face,
								 int vertexBufferObject,int indexBufferObject,int indexCount){
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.setIdentityM(invertMatrix, 0);
		Matrix.setIdentityM(normalMatrix, 0);

		Matrix.translateM(modelMatrix, 0, x, y, z);
		if(scaleX != 0 || scaleY != 0 || scaleZ != 0)
			Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, scaleZ);
		if(degreeZ != 0)
			Matrix.rotateM(modelMatrix, 0, degreeZ, 0, 0, 1);
		if(degreeY != 0)
			Matrix.rotateM(modelMatrix, 0, degreeY, 0, 1, 0);
		if(degreeX != 0)
			Matrix.rotateM(modelMatrix, 0, degreeX, 1, 0, 0);

		Matrix.invertM(invertMatrix, 0, modelMatrix, 0);
		Matrix.transposeM(normalMatrix, 0, invertMatrix, 0);

		setShaderModelMatrix(modelMatrix);
		setShaderNormalMatrix(normalMatrix);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
		GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, FSIZE * 8, 0);
		GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

		GLES20.glVertexAttribPointer(va_Normal, 3, GLES20.GL_FLOAT, true, FSIZE * 8, FSIZE * 3);
		GLES20.glEnableVertexAttribArray(va_Normal);

		//テクスチャの有効化
		GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, FSIZE * 8, FSIZE * 6);
		GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

		for(int n = 0;n < face.length;n++) {
			face[n].matelial.setMatelial();
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, face[n].end-face[n].offset, GLES20.GL_UNSIGNED_INT, face[n].offset);
		}
		//GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,8);
		GLES20.glDisableVertexAttribArray(ma_Position);
		GLES20.glDisableVertexAttribArray(va_Normal);
		GLES20.glDisableVertexAttribArray(ma_texCoord);

	}

	public static void DrawGraph(float startX,float startY,float lengthX,float lengthY,Image img){
		float scaleX = lengthX;
		float scaleY = lengthY;

		//float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix,0,startX,startY,0.0f);
		Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, 1.0f);
		setShaderModelMatrix(modelMatrix);
		img.getBlend().setBlendMode();
		setOnTexture(img.getImage(), 1.0f);

		img.getBlend().setBlendMode();
		GLES20.glBindBuffer(ma_Position, planeBufferObject);
		GLES20.glEnableVertexAttribArray(ma_Position);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);	//描画
	}

	public static void DrawString(String text,int textSize,Color color,float x,float y){

	}

	/**
	 * FPS表示。三桁。FPSに限らず数値であれば表示できる
	 */
	//FPS表示	三桁表示
	public static void DrawFPS(float x,float y,int FPS,Bitmap[] digitBitmap,float alpha){
		int place100,place10,place1;
		FPS %= 1000;
		place100 = FPS/100;
		place10 = (FPS-place100*100)/10;
		place1 = (FPS - place100*100-place10*10);
		DrawGraph(x,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place100],alpha, GLES20COMPOSITIONMODE.ALPHA);
		DrawGraph(x+62.0f/1000.0f,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place10],alpha,GLES20COMPOSITIONMODE.ALPHA);
		DrawGraph(x+62.0f/1000.0f*2.0f,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place1],alpha,GLES20COMPOSITIONMODE.ALPHA);

	}

	/**
	 * FPS用の十進数画像の作成。現在は用意されたdegital2.pngのみを対象。
	 */
	//flagはtrueで黒を透過色、falseで白を透過色
	public static void initFpsBitmap(Bitmap[] bitmap,boolean flag,int resource){
		int count=0;
		int rgb = 0;
		for(int n=0;n<2;n++){
			for(int m=0;m<5;m++){
				bitmap[count] = loadBitmap(62*m,110*n,62*(m+1),110*(n+1),resource);
				if(flag){
					for(int a =0;a<62;a++){
						for(int b=0;b<110;b++){
							rgb = bitmap[count].getPixel(a, b);
							bitmap[count].setPixel(a, b,Color.argb(
									(Color.red(rgb)+Color.red(rgb)+Color.blue(rgb))/3
									,Color.red(rgb)
									,Color.red(rgb)
									,Color.blue(rgb)
							));
						}
					}
				}
				else{
					for(int a =0;a<62;a++){
						for(int b=0;b<110;b++){
							rgb = bitmap[count].getPixel(a, b);

							bitmap[count].setPixel(a, b,Color.argb(
									(255-Color.red(rgb)+255-Color.red(rgb)+255-Color.blue(rgb))/3
									,Color.red(rgb)
									,Color.red(rgb)
									,Color.blue(rgb)
							));
						}
					}
				}
				count++;
			}
		}
	}
}

