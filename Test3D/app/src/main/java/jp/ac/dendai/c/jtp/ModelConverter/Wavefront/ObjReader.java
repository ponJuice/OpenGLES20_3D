package jp.ac.dendai.c.jtp.ModelConverter.Wavefront;

import java.util.LinkedList;

/**
 * Created by Goto on 2016/07/26.
 */
public class ObjReader {
    protected LinkedList<Float> buffer;
    protected String identifier;
    public ObjReader(){
        buffer = new LinkedList<>();
    }
    public LinkedList<Float> getBuffer(){
        return buffer;
    }
    public int read(String[] lines,int offset){
        int n = offset;
        for(;n < lines.length;n++){
            String[] charas = lines[n].split(" ");
            //そうでない場合はバッファにため込む
            if(charas[0].equals(identifier)){
                buffer.add(Float.valueOf(charas[1]));
                buffer.add(Float.valueOf(charas[2]));
                buffer.add(Float.valueOf(charas[3]));
            }else{
                break;
            }
        }
        return n;
    }
    public void clear(){
        buffer.clear();
    }
}
