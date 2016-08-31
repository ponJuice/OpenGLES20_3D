package jp.ac.dendai.c.jtp.Graphics.Shader;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Model;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by テツヤ on 2016/08/29.
 */
public abstract class Shader {
    protected float[] modelMatrix = new float[16];
    protected float[] invertMatrix = new float[16];
    protected float[] normalMatrix = new float[16];
    protected static int useProgram = -1;
    /**
     * プログラムオブジェクト
     */
    protected int program;						//プログラムオブジェクト
    /**
     * 頂点シェーダの頂点座標の格納場所
     */
    protected int ma_Position;			//頂点シェーダの頂点座標の格納場所
    /**
     * 頂点シェーダのワールド行列用格納変数の場所
     */
    protected int mu_ProjMatrix;				//頂点シェーダのワールド行列用格納変数の場所
    /**
     * テクスチャオブジェクトの格納場所
     */
    protected int ma_texCoord;				//テクスチャオブジェクトの格納場所
    /**
     * モデル行列格納場所
     */
    protected int mu_modelMatrix;				//モデル行列格納場所
    protected int u_Sampler;
    protected int u_alpha;
    protected Camera camera;
    public Shader(){}
    public Shader(String v,String f){
        Log.d("abstractGLES20Util","initShader");

        program = createShaderProgram(initVertexShader(v),initFragmentShader(f));

        //プログラムの使用開始
        //GLES20.glUseProgram(program);
        Log.d("abstractGLES20Util","use stert Program finished");

        Log.d("abstractGLES20Util","end of initShader");
        // u_ProjMatrix変数の格納場所を取得する
        mu_ProjMatrix = GLES20Util.getUniformLocation(program, "u_ProjMatrix");
        // a_Positionの格納場所を取得
        ma_Position = GLES20Util.getAttributeLocation(program, "a_Position");
        // モデル行列の格納場所を取得
        mu_modelMatrix = GLES20Util.getUniformLocation(program, "u_ModelMatrix");
        //テクスチャの格納場所を取得
        ma_texCoord = GLES20Util.getAttributeLocation(program, "a_TexCoord");
        //アルファ値
        u_alpha = GLES20Util.getUniformLocation(program,"u_alpha");
        loadShaderVariable();
    }

    /**
     * テクスチャの使用を可能にします
     * @param num テクスチャオブジェクトの数
     */
    public static void useTexture(int num){
        // テクスチャオブジェクトを作成する
        int[] textures = new int[num];
        GLES20.glGenTextures(num, textures, 0);
        if(num >= 1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);   // テクスチャユニット0を有効にする

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]); // テクスチャオブジェクトをバインドする

            // テクスチャパラメータを設定する
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        }
        if(num >= 2){
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);   // テクスチャユニット0を有効にする

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]); // テクスチャオブジェクトをバインドする

            // テクスチャパラメータを設定する
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        }
        if(num >= 3){
            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);   // テクスチャユニット0を有効にする

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[2]); // テクスチャオブジェクトをバインドする

            // テクスチャパラメータを設定する
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        }
    }

    public void useShader(){
        if(useProgram == program)
            return;
        _useShader();
    }
    protected void _useShader(){
        GLES20.glUseProgram(program);
        useProgram = program;
        if(!GLES20.glIsEnabled(GLES20.GL_DEPTH_TEST)){
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }
    }

    //シェーダ―変数の取得
    abstract void loadShaderVariable();

    //マテリアルの設定
    abstract void setMaterial(Face face);

    public void setCamera(Camera camera){
        this.camera = camera;
    }
    public void updateCamera(){
        camera.updateCamera(this);
    }
    public int getProjMatrixPosition(){
        return mu_ProjMatrix;
    }
    /**
     * シェーダをリンクし、プログラムオブジェクトを作成
     */
    private static int createShaderProgram(int vertexShaderObject,int fragmentShaderObject){
        //二つのシェーダオブジェクトを設定
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShaderObject);
        GLES20.glAttachShader(program, fragmentShaderObject);
        Log.d("abstractGLES20Util","setOnProgram finished");

        // プログラムオブジェクトをリンクする
        GLES20.glLinkProgram(program);
        Log.d("abstractGLES20Util","link Program finished");

        // リンク結果をチェックする
        int[] linked = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
        if (linked[0] != GLES20.GL_TRUE) {
            String error = GLES20.glGetProgramInfoLog(program);
            throw new RuntimeException("failed to link program: " + error);
        }
        return program;
    }
    /**
     * 頂点シェーダの初期化
     */
    private static int initVertexShader(String vertexShaderString){
        //頂点シェーダオブジェクトを作成
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // シェーダコードを読み込む
        GLES20.glShaderSource(vertexShader, vertexShaderString);
        // シェーダコードをコンパイルする
        GLES20.glCompileShader(vertexShader);
        // コンパイル結果を検査する
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != GLES20.GL_TRUE) {
            String error = GLES20.glGetShaderInfoLog(vertexShader);
            Log.d("Vertex String [ERROE]",vertexShaderString);
            throw new RuntimeException("failed to compile shader: " + error + "\n" + vertexShaderString);
        }
        return vertexShader;
    }

    /**
     * フラグメントシェーダの初期化
     */
    private static int initFragmentShader(String fragmentShaderString){
        // ピクセルシェーダオブジェクトを作成する
        int fragmentShader = GLES20.glCreateShader( GLES20.GL_FRAGMENT_SHADER );
        // シェーダコードを読み込む
        GLES20.glShaderSource( fragmentShader,fragmentShaderString);
        // シェーダコードをコンパイルする
        GLES20.glCompileShader( fragmentShader );
        // コンパイル結果を検査する
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != GLES20.GL_TRUE) {
            String error = GLES20.glGetShaderInfoLog(fragmentShader);
            Log.d("Fragment String [ERROE]",fragmentShaderString);
            throw new RuntimeException("failed to compile shader: " + error +"\n"+fragmentShaderString);
        }
        return fragmentShader;
    }
    public abstract void draw(Model model, float x, float y, float z,
                       float scaleX, float scaleY, float scaleZ,
                       float degreeX, float degreeY, float degreeZ);
    public abstract void draw(Mesh mesh, float x, float y, float z,
                              float scaleX, float scaleY, float scaleZ,
                              float degreeX, float degreeY, float degreeZ,float alpha);
    protected static void setShaderModelMatrix(float[] modelMatrix){
        GLES20.glUniformMatrix4fv(GLES20Util.mu_modelMatrix, 1, false, modelMatrix, 0);
    }

    protected static void setShaderNormalMatrix(float[] normalMatrix){
        GLES20.glUniformMatrix4fv(GLES20Util.mu_NormalMatrix, 1, false, normalMatrix, 0);
    }

    /**
     * テクスチャ画像を設定する
     * @param 使用する画像のbitmapデータ
     */
    //テクスチャ画像を設定する
    protected static void setOnTexture(Bitmap image, int u_Sampler){
        // テクスチャ画像を設定する
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
        GLES20.glUniform1i(u_Sampler, 0);     // サンプラにテクスチャユニットを設定する
    }
}
