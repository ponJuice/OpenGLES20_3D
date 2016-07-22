package jp.ac.dendai.c.jtp.Graphics;

import android.graphics.Bitmap;

/**
 * Created by Goto on 2016/07/21.
 */
public class Matelial {
    public final static int r = 0;
    public final static int g = 1;
    public final static int b = 2;
    public float[] emmision;
    public float[] diffuse;
    public float[] specular;
    public float[] ambient;
    public Bitmap texture;
    public float Ns,d;
    public Matelial(){
        emmision = new float[3];
        diffuse = new float[3];
        specular = new float[3];
        ambient = new float[3];
        texture = null;
    }
}
