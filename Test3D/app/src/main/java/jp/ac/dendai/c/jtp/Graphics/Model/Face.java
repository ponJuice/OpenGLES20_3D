package jp.ac.dendai.c.jtp.Graphics.Model;

/**
 * Created by Goto on 2016/07/26.
 */
public class Face {
    public Matelial material;
    public int offset,end;
    public Face(Matelial m,int offset,int end){
        material = m;
        this.offset = offset;
        this.end = end;
    }
}
