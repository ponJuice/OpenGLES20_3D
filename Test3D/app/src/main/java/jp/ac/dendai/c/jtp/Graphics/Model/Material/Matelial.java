package jp.ac.dendai.c.jtp.Graphics.Model.Material;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/21.
 */
public class Matelial {
    public int startFace,endFace;
    public final static int r = 0;
    public final static int g = 1;
    public final static int b = 2;
    public float[] emmision;
    public float[] diffuse;
    public float[] specular;
    public float[] ambient;
    public Bitmap tex_emmision;
    public Bitmap tex_diffuse;
    public Bitmap tex_specular;
    public Bitmap tex_ambient;
    public float Ns,d;
    public Matelial(){
        emmision = new float[3];
        diffuse = new float[3];
        specular = new float[3];
        ambient = new float[3];
        tex_ambient = null;
        tex_diffuse = null;
        tex_emmision = null;
        tex_specular = null;
    }
}
