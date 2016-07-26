package jp.ac.dendai.c.jtp.ModelConverter.Wavefront;

import java.util.HashMap;
import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Graphics.Face;
import jp.ac.dendai.c.jtp.Graphics.Matelial;
import jp.ac.dendai.c.jtp.TouchUtil.Input;

/**
 * Created by Goto on 2016/07/26.
 */
public class ObjIndexReader {
    private HashMap<String,Integer> buffer = new HashMap<>();
    private LinkedList<Integer> index = new LinkedList<>();
    private LinkedList<Float> convertVertex = new LinkedList<>();
    private LinkedList<Face> face = new LinkedList<>();
    private int offset = 0,end = 0;
    private String mtlname;
    public int read(String[] lines,int offset,LinkedList<Float> ver,LinkedList<Float> normal,LinkedList<Float> uv,HashMap<String,Matelial> matelials){
        int serialNumber = 0;
        int n = offset;
        for(;n < lines.length;n++){
            String[] charas  = lines[n].split(" ");
            if(charas[0].equals("usemtl")) {
                if (end != 0) {
                    //2回目以降はリストに入れて情報を更新
                    face.add(new Face(matelials.get(mtlname), this.offset, end));
                }
                //最初なら情報を溜めるだけ
                mtlname = charas[1];
                this.offset = end;
            }else if(charas[0].equals("f")) {
                for(int m = 1;m < 4;m++) {
                    //面の情報の項目
                    if (buffer.containsKey(charas[m])) {
                        // すでに登録されているインデックス
                        index.add(buffer.get(charas[m]));
                    } else {
                        //まだ登録されていないインデックス
                        //インデックスバッファに登録
                        index.add(serialNumber);
                        //ハッシュマップに登録し、重複をなくす
                        buffer.put(charas[m], serialNumber);
                        //頂点バッファを更新する
                        String[] element = charas[m].split("/");
                        int elem0 = Integer.valueOf(element[0])-1;
                        int elem1 = Integer.valueOf(element[1])-1;
                        int elem2 = Integer.valueOf(element[2])-1;
                        convertVertex.add(ver.get(elem0*3));
                        convertVertex.add(ver.get(elem0*3+1));
                        convertVertex.add(ver.get(elem0*3+2));
                        convertVertex.add(normal.get(elem2*3));
                        convertVertex.add(normal.get(elem2*3+1));
                        convertVertex.add(normal.get(elem2*3+2));
                        convertVertex.add(uv.get(elem1*2));
                        convertVertex.add(uv.get(elem1*2+1));
                        serialNumber++;
                    }
                    end++;
                }
            }else if(charas[0].equals("o")){
                break;
            }
        }
        face.add(new Face(matelials.get(mtlname), this.offset, end));
        return n;
    }
    public LinkedList<Integer> getIndex(){
        return index;
    }
    public LinkedList<Float> getConvertVertex(){
        return convertVertex;
    }
    public LinkedList<Face> getFace(){
        return face;
    }
    public void clear(){
        buffer.clear();
        index.clear();
        convertVertex.clear();
        face.clear();
        offset = 0;
        end = 0;
        mtlname = null;
    }
}
