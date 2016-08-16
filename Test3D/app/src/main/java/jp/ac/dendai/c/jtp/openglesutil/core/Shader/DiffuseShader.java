package jp.ac.dendai.c.jtp.openglesutil.core.Shader;

import android.opengl.Matrix;
import android.view.SurfaceHolder;

import jp.ac.dendai.c.jtp.Graphics.Model.Matelial;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/08/16.
 * DiffuseShaderは光の計算を行わず、Diffuse色を最終的な色として扱います
 */
public class DiffuseShader extends Shader {

    public DiffuseShader(){
        super(FileManager.readTextFile("DiffuseShaderVertex"), FileManager.readTextFile("DiffuseShaderFragment"));
    }

    @Override
    public void setPointLight(float[] position, float[] color, float power) {

    }

    @Override
    public void setParallelLight(float[] direction, float[] color) {

    }

    @Override
    public void setMaterial(Matelial material,float alpha) {
        GLES20Util.setOnTexture(material.tex_diffuse,material.d*alpha);
    }

    @Override
    public void setMatrix(float x, float y, float z, float sx, float sy, float sz, float rx, float ry, float rz) {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, x, y, z);
        if(sx != 0 || sy != 0 || sz != 0)
            Matrix.scaleM(modelMatrix, 0, sx, sy, sz);
        if(rz != 0)
            Matrix.rotateM(modelMatrix, 0, rz, 0, 0, 1);
        if(ry != 0)
            Matrix.rotateM(modelMatrix, 0, ry, 0, 1, 0);
        if(rx != 0)
            Matrix.rotateM(modelMatrix, 0, rx, 1, 0, 0);

        GLES20Util.setShaderModelMatrix(modelMatrix);
    }
}
