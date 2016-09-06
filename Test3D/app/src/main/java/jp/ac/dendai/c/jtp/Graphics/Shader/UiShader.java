package jp.ac.dendai.c.jtp.Graphics.Shader;

import android.opengl.GLES20;
import android.opengl.Matrix;

import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.Model;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/08/30.
 */
public class UiShader extends Shader{
    public UiShader(){
        super(FileManager.readTextFile("DiffuseShaderVertex.txt")
                ,FileManager.readTextFile("DiffuseShaderFragment.txt"));

    }

    @Override
    protected void _useShader(){
        super._useShader();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    void loadShaderVariable() {
        u_Sampler = GLES20Util.getUniformLocation(program, "u_Sampler");
    }

    @Override
    void setMaterial(Face face) {
        setOnTexture(face.matelial.tex_diffuse,u_Sampler);
    }

    @Override
    public void draw(Model model, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ) {

    }

    @Override
    public void draw(Mesh mesh, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ,float alpha) {

    }

    @Override
    public void draw(Texture tex, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ, float alpha) {
        draw(tex,x,y,scaleX,scaleY,degreeZ,alpha);
    }

    public void draw(Texture tex,float x, float y, float lengthX, float lengthY,float degree,float alpha){
        //裏面を表示しない
        //GLES20.glCullFace(GLES20.GL_FRONT_AND_BACK);

        //float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, -10f);
        Matrix.scaleM(modelMatrix, 0, lengthX, lengthY, 1.0f);
        Matrix.rotateM(modelMatrix, 0, degree, 0, 0, 1);
        setShaderModelMatrix(modelMatrix);

        setOnTexture(tex.getTexture(), u_Sampler);

        tex.getBlendMode().setBlendMode();
        GLES20.glUniform1f(u_alpha,alpha);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tex.getVertexBufferObject());
        GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

        //テクスチャの有効化
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tex.getTextureBufferObject());
        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);	//描画

        GLES20.glDisableVertexAttribArray(ma_Position);
        GLES20.glDisableVertexAttribArray(ma_texCoord);
    }
}
