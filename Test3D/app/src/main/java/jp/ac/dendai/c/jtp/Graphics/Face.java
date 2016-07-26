package jp.ac.dendai.c.jtp.Graphics;

/**
 * Created by Goto on 2016/07/26.
 */
public class Face {
    public Matelial matelial;
    public int offset,end;
    public Face(Matelial m,int offset,int end){
        matelial = m;
        this.offset = offset;
        this.end = end;
    }
}
