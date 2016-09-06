package jp.ac.dendai.c.jtp.Graphics.Line;

import java.nio.FloatBuffer;

import jp.ac.dendai.c.jtp.Graphics.Model.Material.Matelial;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.Model;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/22.
 */
public class Line {
    private static FloatBuffer lineVertex = Model.makeFloatBuffer(new float[]{
            0,0,0,0,0,1,0,0,
            1,1,1,0,0,1,0,0
    });
    private Matelial matelial;
    public Line(float r,float g,float b){
        matelial = new Matelial();
        matelial.emmision = new float[]{r,g,b};
        matelial.specular = new float[]{0,0,0};
        matelial.diffuse = new float[]{0,0,0};
        matelial.d = 1;
        matelial.Ns = 0;
    }
    public void draw(float x,float y,float z,float lengthX,float lengthY,float lengthZ){
        GLES20Util.DrawLine(lineVertex,x,y,z,lengthX,lengthY,lengthZ,matelial.emmision,1.0f, 10,GLES20COMPOSITIONMODE.ALPHA);
    }
}
