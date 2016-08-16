package jp.ac.dendai.c.jtp.openglesutil.core.Shader;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;

import jp.ac.dendai.c.jtp.Graphics.Model.Matelial;
import jp.ac.dendai.c.jtp.Graphics.Model.Model;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/30.
 */
public abstract class Shader {
    protected static float[] modelMatrix = new float[16];
    protected int program;
    protected int ma_Position;			//頂点シェーダの頂点座標の格納場所
    protected int mu_ProjMatrix;				//頂点シェーダのワールド行列用格納変数の場所
    protected int mu_modelMatrix;				//モデル行列格納場所
    protected int[] textureObject;

    public Shader(String vertexShaderCode,String fragmentShaderCode){
        program = createProgram(vertexShaderCode, fragmentShaderCode);
        //プロジェクション行列の取得
        mu_ProjMatrix = GLES20.glGetUniformLocation(program, "u_ProjMatrix");
        if (mu_ProjMatrix == -1) {
            throw new RuntimeException("u_ProjMatrixの格納場所の取得に失敗");
        }
        // 頂点の格納場所を取得
        ma_Position = GLES20.glGetAttribLocation(program, "a_Position");
        if (ma_Position == -1) {
            throw new RuntimeException("a_Positionの格納場所の取得に失敗");
        }
        // モデル行列の格納場所を取得
        mu_modelMatrix = GLES20.glGetUniformLocation(program, "u_ModelMatrix");
        if (mu_modelMatrix == -1) {
            throw new RuntimeException("u_ModelMatrixの格納場所の取得に失敗");
        }
    }
    //ポイントライトの設定
    public abstract void setPointLight(float[] position,float[] color,float power);
    //平行光線の設定
    public abstract void setParallelLight(float[] direction,float[] color);
    public abstract void setMaterial(Matelial material,float alpha);
    public abstract void setMatrix(float x,float y,float z,float sx,float sy,float sz,float rx,float ry,float rz);
    public static int createProgram(String vertexShaderCode,String fragmentShaderCode){
        int program;
        Log.d("abstractGLES20Util", "initShader");
        //頂点シェーダの初期化
        int vertexShader = initVertexShader(vertexShaderCode);
        Log.d("abstractGLES20Util","initVertexShader finished");

        //フラグメントシェーダの初期化
        int fragmentShader = initFragmentShader(fragmentShaderCode);
        Log.d("abstractGLES20Util","initFragmentShader finished");

        //二つのシェーダオブジェクトを設定
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        Log.d("abstractGLES20Util","setOnProgram finished");

        // プログラムオブジェクトをリンクする
        GLES20.glLinkProgram(program);
        Log.d("abstractGLES20Util", "link Program finished");

        // リンク結果をチェックする
        int[] linked = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
        if (linked[0] != GLES20.GL_TRUE) {
            String error = GLES20.glGetProgramInfoLog(program);
            throw new RuntimeException("failed to link program: " + error);
        }

        Log.d("abstractGLES20Util","end of initShader");

        return program;
    }
    private static int initVertexShader(String vertexShaderString){
        //頂点シェーダオブジェクトを作成
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // シェーダコードを読み込む
        GLES20.glShaderSource( vertexShader,vertexShaderString);
        // シェーダコードをコンパイルする
        GLES20.glCompileShader( vertexShader );
        // コンパイル結果を検査する
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != GLES20.GL_TRUE) {
            String error = GLES20.glGetShaderInfoLog(vertexShader);
            throw new RuntimeException("failed to compile shader: " + error);
        }
        return vertexShader;
    }
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
            throw new RuntimeException("failed to compile shader: " + error);
        }
        return fragmentShader;
    }
}
