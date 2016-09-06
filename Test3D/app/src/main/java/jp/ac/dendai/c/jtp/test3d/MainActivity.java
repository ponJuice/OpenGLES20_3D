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
import android.view.View;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsInfo;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.openglesutil.Util.ImageReader;
import jp.ac.dendai.c.jtp.Graphics.Line.Line;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.Model;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.ModelObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class MainActivity extends Activity implements GLSurfaceView.Renderer{
    private FpsController fpsController = new FpsController((short)60);
    public static Bitmap[] fpsImage = new Bitmap[10];
    private float rotateX = 0 ,rotateY = 0;
    private Model mode;
    private Line line_x,line_y,line_z;
    private Renderer renderer;
    private Renderer testRenderer;
    private Plane plane;
    private Camera camera;
    private Camera uiCamera;
    private Camera testCamera;
    private GameObject[] gameObjects;
    private Button button;
    private Player player;
    private Physics3D physics;
    private Shader shader;
    private UiShader uiShader;
    private DiffuseShader testShader;
    private Texture tex;
    private ModelObject[] objects;

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

        //フルスクリーン、ステータスバー非表示、ナビゲーションバー非表示
        /*View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

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
        Input.setOrientation(getResources().getConfiguration().orientation);

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
        //GLES20Util.initTextures();
        GLES20Util.initFpsBitmap(fpsImage, true, R.drawable.degital2);
        uiCamera.setPosition(GLES20Util.getAspect() / 2f, 0.5f, 0);
        Log.d("onSurfaceCreated", "initShader");
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        String vertexShader = new String(FileManager.readShaderFile(this, "DiffuseShaderVertex.txt"));
        String fragmentShader = new String(FileManager.readShaderFile(this,"DiffuseShaderFragment.txt"));
        //テクスチャを１枚使えるようにする
        Shader.useTexture(1);
        //シェーダの作成
        testShader = new DiffuseShader();
        shader = new DiffuseShader();
        uiShader = new UiShader();
        //OpenGLES20のもろもろを使えるようにする
        GLES20Util.initGLES20Util(vertexShader, fragmentShader, false);

        //オブジェクトファイルの読み込み
        objects = WavefrontObjConverter.createModel("untitled.obj");
        //バッファオブジェクトを使用する
        objects[0].useBufferObject();

        //プリミティブ型
        plane = new Plane();
        plane.useBufferObject();
        plane.setImage(GLES20Util.loadBitmap(R.drawable.block));

        //UI用のテクスチャ読み込み
        tex = new Texture(GLES20Util.loadBitmap(R.drawable.block),GLES20COMPOSITIONMODE.ALPHA);
        //バッファオブジェクトを使用する
        tex.setBufferObject();

        //ゲームオブジェクトを作成
        gameObjects = new GameObject[2];
        gameObjects[0] = new GameObject();
        gameObjects[1] = new GameObject();
        //メッシュをゲームオブジェクトに登録
        gameObjects[0].getRenderMediator().mesh = objects[0];
        gameObjects[1].getRenderMediator().mesh = plane;
        //コライダ―の設定
        gameObjects[0].setCollider(new CircleCollider(1));
        gameObjects[1].setCollider(new CircleCollider(1));
        //ゲームオブジェクトの位置を変更
        gameObjects[0].getPos().setX(-2.0f);
        gameObjects[1].getPos().setY(-1f);
        gameObjects[1].getScl().setX(50f);
        gameObjects[1].getScl().setY(50f);
        gameObjects[1].getScl().setZ(50f);

        //プレイヤー
        player = new Player();
        player.getRenderMediator().mesh = objects[0];
        //タッチリスナで動かせるように
        Input.getTouchArray()[0].addTouchListener(new TouchListener() {
            @Override
            public void execute(Touch t) {
                rotateX += t.getDelta(Touch.Pos_Flag.X) * 0.1f;
                rotateY += t.getDelta(Touch.Pos_Flag.Y) * 0.1f;

                if(rotateX <= -90f)
                    rotateX = -89f;
                else if(rotateX >= 90f)
                    rotateX = 89f;

                if(rotateY < -90f)
                    rotateY = -90f;
                else if(rotateY > 90f)
                    rotateY = 90f;

                player.getRot().setY(rotateY);
                player.getRot().setX(-rotateX);
            }
        });

        //物理計算用クラス作成
        PhysicsInfo info = new PhysicsInfo();
        info.enabled = true;
        info.gravity = new Vector3(0,0,0);
        info.maxObject = 3;
        physics = new Physics3D(info);
        PhysicsObject po = new PhysicsObject(gameObjects[0]);
        po.useGravity = false;
        po.freeze = false;
        physics.addObject(po);
        po = new PhysicsObject(gameObjects[1]);
        po.useGravity = false;
        po.freeze = false;
        physics.addObject(po);

        //レンダラを作成
        renderer = new Renderer();
        testRenderer = new Renderer();
        //レンダラにシェーダ―を登録
        renderer.setShader(shader);
        testRenderer.setShader(testShader);
        //レンダラに表示したいオブジェクトを登録
        renderer.addItem(gameObjects[0]);
        renderer.addItem(gameObjects[1]);

        testRenderer.addItem(gameObjects[0]);
        testRenderer.addItem(gameObjects[1]);
        testRenderer.addItem(player);


        //カメラを作成
        camera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,-10f,10f,10f);
        camera.setFar(1000f);
        uiCamera = new Camera(Camera.CAMERA_MODE.ORTHO,0,0,10f);
        uiCamera.setNear(0.1f);
        testCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,0);

        //カメラを登録
        player.setCamera(testCamera);

        //シェーダ―に使用するカメラを登録
        shader.setCamera(camera);
        uiShader.setCamera(uiCamera);
        testShader.setCamera(testCamera);

        //ボタンを作成
        button = new Button(0,0.125f,0.125f,0);
        button.setCamera(uiCamera);
        button.setBackground(tex);

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // 画面をクリアする色を設定する
    }
    private void tempTouchProcess(Touch t){
        if(t.getTouchID() == -1)
            return;
        rotateX += t.getDelta(Touch.Pos_Flag.X) * 0.1f;
        rotateY += t.getDelta(Touch.Pos_Flag.Y) * 0.1f;

        if(rotateX <= -90f)
            rotateX = -89f;
        else if(rotateX >= 90f)
            rotateX = 89f;

        if(rotateY < -90f)
            rotateY = -90f;
        else if(rotateY > 90f)
            rotateY = 90f;

        player.getRot().setY(rotateY);
        player.getRot().setX(-rotateX);
    }
    private void process(){
        fpsController.updateFps();
        player.proc();
        //tempTouchProcess(Input.getTouchArray()[0]);
        button.touch(Input.getTouchArray()[0]);
        button.proc();
        //physics.simulate();
    }
    private int count = 0;
    private void draw(){
        // 描画領域をクリアする
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if(count % 60 == 0) {
            Log.d("FPS",String.valueOf(fpsController.getFps()));
        }
        count++;
        //Log.d("Touch",Input.getTouchArray()[0].toString());
        testRenderer.drawAll();
        //renderer.drawAll();
        //shader.useShader();
        //shader.updateCamera();
        //shader.draw(gameObjects[0].getRenderMediator().mesh,0,0,0,1,1,1,0,0,0,1);
        //mode.draw(0, 0, 0, 1f, 1f, 1f, rotateX, rotateY, 0);

        uiShader.useShader();
        uiShader.updateCamera();
        button.draw(uiShader);
        //uiShader.draw(tex,0.125f,0.125f,0.25f,0.25f,0,1f);
        //uiShader.draw(tex,0.25f,0.25f,0.5f,0.5f,0,1f);

        /*
        //文字の描画
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0, 0, GLES20COMPOSITIONMODE.ALPHA);
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0.05f, 0,GLES20COMPOSITIONMODE.ALPHA);
        */
        //FPSの表示
        //GLES20Util.DrawFPS(0, 1.0f, fpsController.getFps(), fpsImage, 0.5f);
    }
}
