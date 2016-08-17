package jp.ac.dendai.c.jtp.openglesutil.core.Shader;

import android.opengl.GLES20;
import android.util.Log;

import jp.ac.dendai.c.jtp.Graphics.Model.Matelial;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.Environment;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/30.
 */
public abstract class Shader{
    protected static float[] modelMatrix = new float[16];
    protected Renderer first;
    protected Renderer end;
    protected int size;
    protected int program;
    protected int ma_Position;			           //頂点シェーダの頂点座標の格納場所
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
    public void addLast(Renderer r){
        if(size == 0){
            first = r;
            end = r;
            r.next = null;
            r.prev = null;
        }else{
            r.prev = end;
            end.next = r;
            end = r;
        }
        r.shader = this;
        size++;
    }
    public void remove(Renderer r){
        r.remove(this);
        size--;
    }
    public abstract void setEnvironmentParam(Environment e);
    public abstract void setRenderParam(Renderer r);
    public abstract void setMaterial(Matelial material,float alpha);
    public abstract void setMatrix(float x,float y,float z,float sx,float sy,float sz,float rx,float ry,float rz);
    public void draw(){
        useShader();
        Renderer r = first;
        setEnvironmentParam(r.environment);
        while(r != null){
            setMatrix(r.gameObject.position.getX(),r.gameObject.position.getY(),r.gameObject.position.getZ(),
                    r.gameObject.scale.getX(),r.gameObject.scale.getY(),r.gameObject.scale.getZ(),
                    r.gameObject.rotation.getX(),r.gameObject.rotation.getY(),r.gameObject.rotation.getZ());
            setRenderParam(r);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, r.mesh.getVBO());
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, r.mesh.getIBO());
            GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, 0);
            GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

            GLES20.glVertexAttribPointer(va_Normal, 3, GLES20.GL_FLOAT, true, GLES20Util.FSIZE * 8, GLES20Util.FSIZE * 3);
            GLES20.glEnableVertexAttribArray(va_Normal);

            //テクスチャの有効化
            GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, FSIZE * 8, FSIZE * 6);
            GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

            for(int n = 0;n < face.length;n++) {
                shader.setMaterial(face[n].material,alpha);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, face[n].end-face[n].offset+1, GLES20.GL_UNSIGNED_INT, ISIZE*face[n].offset);
            }
            //GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,8);
            GLES20.glDisableVertexAttribArray(ma_Position);
            GLES20.glDisableVertexAttribArray(va_Normal);
            GLES20.glDisableVertexAttribArray(ma_texCoord);

            r = r.next;
        }
    }
    public void useShader(){
        GLES20.glUseProgram(program);
    }
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
