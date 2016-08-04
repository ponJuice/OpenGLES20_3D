package jp.ac.dendai.c.jtp.openglesutil.core.Shader;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import jp.ac.dendai.c.jtp.Graphics.Model.Matelial;
import jp.ac.dendai.c.jtp.Graphics.Model.Model;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/30.
 * 2D表示用シェーダー
 * 【内容】
 * 　光による陰影は一切計算しません
 * 　Materialのtex_diffuse(tex_diffuseがnullの場合はデフォルトのテクスチャ)をテクスチャに使用します
 */
public class Shader2D extends Shader{
    protected int ma_texCoord;				//テクスチャオブジェクトの格納場所
    protected int u_Sampler;                //テクスチャのサンプラ
    protected int u_alpha;                  //透明度

    public Shader2D(String vertexShaderCode,String fragmentShaderCode)
    {
        //シェーダプログラムの作成（親クラスが担当）
        super(vertexShaderCode, fragmentShaderCode);

        //テクスチャオブジェクトの作成
        // バッファオブジェクトを作成する
        int[] vertexTexCoord = new int[1];
        GLES20.glGenBuffers(1, vertexTexCoord, 0);
        // テクスチャオブジェクトを作成する
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        //Uniform変数の格納場所取得
        //テクスチャの格納場所を取得
        ma_texCoord = GLES20.glGetAttribLocation(program, "a_TexCoord");
        if (ma_texCoord == -1) {
            throw new RuntimeException("a_texCoordの格納場所の取得に失敗");
        }
        // u_Samplerの格納場所を取得する
        u_Sampler = GLES20.glGetUniformLocation(program, "u_Sampler");
        if (u_Sampler == -1) {
            throw new RuntimeException("u_Samplerの格納場所の取得に失敗");
        }
        u_alpha = GLES20.glGetUniformLocation(program, "u_alpha");
        if(u_alpha == -1){
            throw new RuntimeException("u_alphaの格納場所の取得に失敗");
        }
    }

    @Override
    public void setMaterial(Matelial material) {
        //テクスチャの設定
        if(material.tex_diffuse == null){
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20Util.white, 0);
        }else {
            // テクスチャ画像を設定する
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, material.tex_diffuse, 0);
        }

        GLES20.glUniform1f(u_alpha, material.d);		//サンプラにアルファを設定する
        GLES20.glUniform1i(u_Sampler, 0);     // サンプラにテクスチャユニットを設定する
    }

    @Override
    public void draw(float x, float y, float z, float sx, float sy, float sz, float degx, float degy, float degz, int verBuffObj, int indBuffObj, int size) {
        Matrix.setIdentityM(modelMatrix, 0);

        if(sx != 0 || sy != 0 || sz != 0)
            Matrix.scaleM(modelMatrix, 0, sx, sy, sz);
        if(degz != 0)
            Matrix.rotateM(modelMatrix, 0, degz, 0, 0, 1);
        if(degy != 0)
            Matrix.rotateM(modelMatrix, 0, degy, 0, 1, 0);
        if(degx != 0)
            Matrix.rotateM(modelMatrix, 0, degx, 1, 0, 0);

        GLES20Util.setShaderModelMatrix(modelMatrix);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, verBuffObj);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indBuffObj);
        GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, 0);
        GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

        //テクスチャの有効化
        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, GLES20Util.FSIZE * 6);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        for(int n = 0;n < face.length;n++) {
            face[n].matelial.setMatelial();
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, face[n].end-face[n].offset+1, GLES20.GL_UNSIGNED_INT, ISIZE*face[n].offset);
        }
        GLES20.glDisableVertexAttribArray(ma_Position);
        GLES20.glDisableVertexAttribArray(ma_texCoord);
    }

    //テクスチャ画像を設定する
    public void setOnTexture(Bitmap image,float alpha){
        if(image == null){
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, white, 0);
        }else {
            // テクスチャ画像を設定する
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
        }

        GLES20.glUniform1f(u_alpha, alpha);		//サンプラにアルファを設定する
        GLES20.glUniform1i(u_Sampler, 0);     // サンプラにテクスチャユニットを設定する
    }

    @Override
    public void drawModel(float x, float y, float z, float sx, float sy, float sz, float degreeX, float degreeY, float degreeZ,Matelial material, float alpha, GLES20COMPOSITIONMODE mode) {

    }
}
