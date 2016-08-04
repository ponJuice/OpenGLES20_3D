package jp.ac.dendai.c.jtp.Graphics.Model;

import jp.ac.dendai.c.jtp.openglesutil.core.Shader.Shader;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/26.
 */
public class Face {
    public Matelial material;
    public Shader shader;
    public int offset,end;
    public Face(Matelial m,Shader shader,int offset,int end){
        material = m;
        this.offset = offset;
        this.end = end;
        this.shader = shader;
    }
    public void draw(float x,float y,float z,float sx,float sy,float sz,float degx,float degy,float degz,GLES20COMPOSITIONMODE mode,int verBuffObj,int indBuffObj,int size){
        shader.setMaterial(material);
        mode.setBlendMode();
        shader.draw(x,y,z,sx,sy,sz,degx,degy,degz,verBuffObj,indBuffObj,size);
    }
}
