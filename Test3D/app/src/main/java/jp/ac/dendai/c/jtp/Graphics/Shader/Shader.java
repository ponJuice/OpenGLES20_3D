package jp.ac.dendai.c.jtp.Graphics.Shader;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.Buffer;

import jp.ac.dendai.c.jtp.Graphics.Model.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.Util.BufferCreater;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.Math.Vector3;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by テツヤ on 2016/08/26.
 */
public abstract class Shader {
    /**
     * テクスチャ座標
     */
    private static final float[] texPosition={		//テクスチャ座標
            0.0f,1.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            1.0f,0.0f
    };
    protected static float[] modelMatrix = new float[16];
    protected static int useProgram;
    protected int program = -1;
    protected int ma_Position = -1;			        //頂点シェーダの頂点座標の格納場所
    protected int mu_ProjMatrix = -1;				//頂点シェーダのワールド行列用格納変数の場所
    protected int mu_modelMatrix = -1;               //モデルマトリックスの格納場所
    protected int u_Sampler = -1;
    protected int ma_texCoord = -1;
    protected int[] texture;
    protected String vs_name,fs_name;
    public Shader(String vertex,String fragment){
        vs_name = vertex;
        fs_name = fragment;
    }
    public void loadShader(){
        //シェーダ―ファイルの中身を読み込む
        String vertex = FileManager.readTextFile(vs_name);
        String fragment = FileManager.readTextFile(fs_name);
        //プログラムオブジェクトを作成
        program = initShader(vertex,fragment);

        //シェーダ変数の格納場所を取得
        //プロジェクション行列の取得
        mu_ProjMatrix = Shader.getUniformLocation(program, "u_ProjMatrix");

        // 頂点の格納場所を取得
        ma_Position = Shader.getAttributeLocation(program, "a_Position");

        // モデル行列の格納場所を取得
        mu_modelMatrix = Shader.getUniformLocation(program, "u_ModelMatrix");

        //テクスチャサンプラの格納場所を取得
        u_Sampler = Shader.getUniformLocation(program,"u_Sampler");

        //テクスチャ座標の格納場所を取得
        ma_texCoord = Shader.getAttributeLocation(program,"a_TexCoord");

        // バッファオブジェクトを作成する
        int[] vertexTexCoord = new int[1];
        GLES20.glGenBuffers(1, vertexTexCoord, 0);
        //テクスチャ座標のバッファを作成
        Buffer texBuffer = BufferCreater.createFloatBuffer(texPosition);

        // テクスチャ座標をバッファオブジェクトに書き込む
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexTexCoord[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, GLES20Util.FSIZE * texBuffer.limit(), texBuffer, GLES20.GL_DYNAMIC_DRAW);

        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        //テクスチャユニットは一つだけ使用
        // テクスチャオブジェクトを作成する
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        //テクスチャユニットに関する設定
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);   // テクスチャユニット0を有効にする
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]); // テクスチャオブジェクトをバインドする
        // テクスチャパラメータを設定する
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        //シェーダ変数の取得
        loadShaderVariable();

        //プログラムの使用開始
        GLES20.glUseProgram(program);
        Log.d("abstractGLES20Util", "use stert Program finished");
    }
    public void useShader(){
        if(useProgram == program)
            return;
        _useShader();
    }
    protected void _useShader(){
        GLES20.glUseProgram(program);
        useProgram = program;
    }
    public abstract void draw(Mesh mesh, Vector3 pos, Vector3 rotate, Vector3 scale);

    //シェーダ―変数の取得
    abstract void loadShaderVariable();

    //マテリアルの設定
    abstract void setMaterial(Face face);

    //シェーダ―変数の取得用ユーティリティ関数
    //エラーの処理もする
    protected static int getUniformLocation(int program,String name){
        int variable = GLES20.glGetUniformLocation(program, name);
        if (variable == -1) {
            throw new RuntimeException(name+"の格納場所の取得に失敗");
        }
        return variable;
    }
    protected static int getAttributeLocation(int program,String name){
        int variable = GLES20.glGetAttribLocation(program,name);
        if(variable == -1){
            throw new RuntimeException(name+"の格納場所の取得に失敗");
        }
        return variable;
    }

    /**
     * シェーダ―を初期化します
     */
    private static int initShader(String vertexShaderString,String fragmentShaderString){
        Log.d("abstractGLES20Util","initShader");
        //頂点シェーダの初期化
        int vertexShader = initVertexShader(vertexShaderString);
        Log.d("abstractGLES20Util","initVertexShader finished");

        //フラグメントシェーダの初期化
        int fragmentShader = initFragmentShader(fragmentShaderString);
        Log.d("abstractGLES20Util","initFragmentShader finished");

        //二つのシェーダオブジェクトを設定
        int program = GLES20.glCreateProgram();
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
    /**
     * 頂点シェーダの初期化
     */
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
            throw new RuntimeException("failed to compile shader: " + error);
        }
        return fragmentShader;
    }

    /**
     * テクスチャ画像を設定する
     * @param 使用する画像のbitmapデータ
     */
    //テクスチャ画像を設定する
    protected static void setOnTexture(Bitmap image,int u_Sampler){
        // テクスチャ画像を設定する
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0,image, 0);
        GLES20.glUniform1i(u_Sampler,0);     // サンプラにテクスチャユニットを設定する
    }
}
