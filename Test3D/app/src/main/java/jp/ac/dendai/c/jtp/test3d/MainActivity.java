package jp.ac.dendai.c.jtp.test3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import jp.ac.dendai.c.jtp.Graphics.ImageReader;
import jp.ac.dendai.c.jtp.Graphics.Model;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

public class MainActivity extends Activity implements GLSurfaceView.Renderer{
    private FpsController fpsController = new FpsController((short)60);
    public static Bitmap[] fpsImage = new Bitmap[10];
    private int[] v_obj = new int[1],i_obj = new int[1];
    private Model mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // OpenGL ES 2.0が使用できるように初期化する
        GLSurfaceView glSurfaceView = GLES20Util.initGLES20(this, this);

        // GLSurfaceViewをこのアプリケーションの画面として使用する
        setContentView(glSurfaceView);

        //ファイルマネージャを使えるようにする
        FileManager.initFileManager(this);

        //イメージリーダーを使えるようにする
        ImageReader.initImageReader(this);

        Log.d("onCreate", "onCreate finished");}

    @Override
    public void onDrawFrame(GL10 arg0) {
        // TODO 自動生成されたメソッド・スタブ
        process();
        draw();
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        Log.d("MainActivity", "onSurfaceChanged");
        // 表示領域を設定する
        GLES20Util.initDrawErea(width, height, true);
        //テクスチャの再読み込み
        GLES20Util.initTextures();
        GLES20Util.initFpsBitmap(fpsImage, true, R.drawable.degital2);
        Log.d("onSurfaceCreated", "initShader");
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        String vertexShader = new String(FileManager.readShaderFile(this, "VSHADER.txt"));
        String fragmentShader = new String(FileManager.readShaderFile(this,"FSHADER.txt"));
        GLES20Util.initGLES20Util(vertexShader,fragmentShader);

        mode = Model.createModel(WavefrontObjConverter.createModel("monky.obj"));
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 画面をクリアする色を設定する
    }
    private void process(){
        fpsController.updateFps();
    }
    private int count = 0;
    private void draw(){
        // 描画領域をクリアする
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if(count % 60 == 0) {
            Log.d("FPS",String.valueOf(fpsController.getFps()));
        }
        count++;
        mode.draw(0,0,0,1f,1f,1f,0,0,count%360);
        /*
        //文字の描画
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0, 0, GLES20COMPOSITIONMODE.ALPHA);
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0.05f, 0,GLES20COMPOSITIONMODE.ALPHA);
        */
        //FPSの表示
        //GLES20Util.DrawFPS(0, 1.0f, fpsController.getFps(), fpsImage, 0.5f);
    }
}
