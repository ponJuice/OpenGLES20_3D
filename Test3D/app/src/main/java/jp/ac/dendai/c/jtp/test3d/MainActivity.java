package jp.ac.dendai.c.jtp.test3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.ImageReader;
import jp.ac.dendai.c.jtp.Graphics.Line.Line;
import jp.ac.dendai.c.jtp.Graphics.Model.Model;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

public class MainActivity extends Activity implements GLSurfaceView.Renderer{
    private FpsController fpsController = new FpsController((short)60);
    public static Bitmap[] fpsImage = new Bitmap[10];
    private float rotateX = 0 ,rotateY = 0;
    private Model mode;
    private Line line_x,line_y,line_z;
    private Camera camera;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int eventAction = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int ptrIndex = event.findPointerIndex(pointerId);
        Touch temp;

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                Input.addTouchCount();
                (Input.getTouch()).setTouch(event.getX(ptrIndex), event.getY(ptrIndex), pointerId);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Input.addTouchCount();
                if(Input.getTouchCount() <= Input.getMaxTouch()){
                    (Input.getTouch()).setTouch(event.getX(ptrIndex), event.getY(ptrIndex), pointerId);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                Input.subTouchCount();
                if((temp = Input.getTouch(pointerId)) != null){
                    temp.removeTouch();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Input.subTouchCount();
                if((temp = Input.getTouch(pointerId)) != null){
                    temp.removeTouch();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                //どれか一つでも移動された場合、全てのタッチ位置を更新する
                for(int n=0;n < Input.getMaxTouch();n++){
                    if((temp = Input.getTouchArray()[n]).getTouchID() != -1){
                        temp.updatePosition(event.getX(event.findPointerIndex(temp.getTouchID())),event.getY(event.findPointerIndex(temp.getTouchID())));
                    }
                }
                break;
        }
        //pointerInfo.setText("pointerID:"+pointerId+" pointerIndex:"+pointerIndex+" ptrIndex:"+ptrIndex);
        //count.setText("count : " + Input.getTouchCount());
        //text1.setText(Input.getTouchArray()[0].toString());
        //text2.setText(Input.getTouchArray()[1].toString());
        return true;
    }

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

        //タッチマネージャーを使えるようにする
        Input.setMaxTouch(1);
        Input.getTouchArray()[0].addTouchListener(new TouchListener() {
            @Override
            public void execute(Touch t) {
                //rotateY += t.getDelta(Touch.Pos_Flag.X) * 0.1f;
                //rotateX += t.getDelta(Touch.Pos_Flag.Y) * 0.1f;
                camera.setAngleOfView(camera.getAngleOfView()+(t.getDelta(Touch.Pos_Flag.Y) * 0.01f));
                camera.addPosition(-t.getDelta(Touch.Pos_Flag.X) * 0.1f,t.getDelta(Touch.Pos_Flag.X) * 0.1f,t.getDelta(Touch.Pos_Flag.X) * 0.1f);
            }
        });

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

        mode = Model.createModel(WavefrontObjConverter.createModel("houdai.obj"));
        line_x = new Line(1f,0,0);
        line_y = new Line(0,1f,0);
        line_z = new Line(0,0,1f);
        camera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,-5f,5f,5f);

        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 画面をクリアする色を設定する
    }
    private void process(){
        fpsController.updateFps();
    }
    private int count = 0;
    private void draw(){
        // 描画領域をクリアする
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        camera.updateCamera();
        if(count % 60 == 0) {
            Log.d("FPS",String.valueOf(fpsController.getFps()));
        }
        count++;
        //Log.d("Touch",Input.getTouchArray()[0].toString());
        line_x.draw(0,0,0,50f,0,0);
        line_y.draw(0, 0, 0, 0, 50f, 0);
        line_z.draw(0,0,0,0,0,50f);
        mode.draw(0, 0, 0, 1f, 1f, 1f, rotateX, rotateY, 0);

        /*
        //文字の描画
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0, 0, GLES20COMPOSITIONMODE.ALPHA);
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0.05f, 0,GLES20COMPOSITIONMODE.ALPHA);
        */
        //FPSの表示
        //GLES20Util.DrawFPS(0, 1.0f, fpsController.getFps(), fpsImage, 0.5f);
    }
}
