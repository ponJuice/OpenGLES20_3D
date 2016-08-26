package jp.ac.dendai.c.jtp.Graphics.Shader;

import android.opengl.GLES20;
import android.opengl.Matrix;

import jp.ac.dendai.c.jtp.Graphics.Model.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.Util.Math.Vector3;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by テツヤ on 2016/08/26.
 */
public class DiffuseShader extends Shader{
    public DiffuseShader(){
        super("diff_vertex","diff_fragment");
    }
    @Override
    void loadShaderVariable() {
    }

    @Override
    void setMaterial(Face face) {
        setOnTexture(face.material.tex_diffuse,u_Sampler);
    }

    @Override
    protected void _useShader() {
        super._useShader();
    }

    @Override
    public void draw(Mesh mesh, Vector3 pos, Vector3 rotate, Vector3 scale) {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, pos.getX(), pos.getY(), pos.getZ());
        if(scale.getX() != 0 || scale.getY() != 0 || scale.getZ() != 0)
            Matrix.scaleM(modelMatrix, 0, scale.getX(), scale.getY(), scale.getZ());
        if(rotate.getZ() != 0)
            Matrix.rotateM(modelMatrix, 0, rotate.getZ(), 0, 0, 1);
        if(rotate.getY() != 0)
            Matrix.rotateM(modelMatrix, 0, rotate.getY(), 0, 1, 0);
        if(rotate.getX() != 0)
            Matrix.rotateM(modelMatrix, 0, rotate.getX(), 1, 0, 0);

        GLES20.glUniformMatrix4fv(mu_modelMatrix, 1, false, modelMatrix, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.getVBO());
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
        GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, 0);
        GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

        //テクスチャの有効化
        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, GLES20Util.FSIZE * 6);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        for(int n = 0;n < mesh.getFaces().length;n++) {
            setMaterial(mesh.getFaces()[n]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getFaces()[n].end-mesh.getFaces()[n].offset+1, GLES20.GL_UNSIGNED_INT, GLES20Util.ISIZE*mesh.getFaces()[n].offset);
        }
        //GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,8);
        GLES20.glDisableVertexAttribArray(ma_Position);
        GLES20.glDisableVertexAttribArray(ma_texCoord);
    }
}
