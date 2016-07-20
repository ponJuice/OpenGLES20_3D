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

import jp.ac.dendai.c.jtp.openglesutil.graphic.Image;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;


public class GLES20Util extends abstractGLES20Util {
	private static Paint paint;
	private static Canvas canvas;
	private static Rect rect = new Rect(0,0,0,0);
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
		Matrix.translateM(modelMatrix,0,startX,startY,0.0f);
		Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, 1.0f);
		setShaderModelMatrix(modelMatrix);

		setOnTexture(image, alpha);

		mode.setBlendMode();
		GLES20.glBindBuffer(ma_Position, planeBufferObject);
		GLES20.glEnableVertexAttribArray(ma_Position);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);	//描画
	}

	public static void DrawGraph(float startX,float startY,float lengthX,float lengthY,float degree,Bitmap image,float alpha,GLES20COMPOSITIONMODE mode){
		float scaleX = lengthX;
		float scaleY = lengthY;

		//float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
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
								 Bitmap texture,
								 int vertexBufferObject,int normalBufferObject,int indexBufferObject,int indexCount){
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, y, z);
		Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, scaleZ);
		Matrix.rotateM(modelMatrix, 0, degreeZ, 0, 0, 1);
		setShaderModelMatrix(modelMatrix);

		setOnTexture(texture, 1.0f);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
		GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, normalBufferObject);
		GLES20.glVertexAttribPointer(va_Normal, 3, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glEnableVertexAttribArray(va_Normal);

		GLES20.glLineWidth(4.0f);
		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 8, GLES20.GL_UNSIGNED_INT, 0);
		//GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,8);
		GLES20.glDisableVertexAttribArray(ma_Position);
		GLES20.glDisableVertexAttribArray(va_Normal);
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

