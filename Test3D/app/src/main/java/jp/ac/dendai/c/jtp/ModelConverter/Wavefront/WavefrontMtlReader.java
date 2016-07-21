package jp.ac.dendai.c.jtp.ModelConverter.Wavefront;

import java.util.HashMap;
import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Graphics.ImageReader;
import jp.ac.dendai.c.jtp.Graphics.Matelial;

/**
 * Created by Goto on 2016/07/21.
 */
public class WavefrontMtlReader {
    public static HashMap createMaterial(String code){
        HashMap<String,Matelial> matelials = new HashMap<>();
        String targetMatelial = "None";
        String[] lines = code.split("\n");
        for(String l : lines){
            String[] content = l.split(" ");
            if(content[0].equals("newmtl")){
                targetMatelial = content[1];
                matelials.put(targetMatelial,new Matelial());
            }else if(content[0].equals("Ns")){
                //スペキュラー係数
                matelials.get(targetMatelial).Ns = Float.valueOf(content[1]);
            }else if(content[0].equals("Ka")){
                //アンビエント
                matelials.get(targetMatelial).a_r = Float.valueOf(content[1]);
                matelials.get(targetMatelial).a_g = Float.valueOf(content[2]);
                matelials.get(targetMatelial).a_b = Float.valueOf(content[3]);
            }else if(content[0].equals("Kb")){
                //デフューズ
                matelials.get(targetMatelial).d_r = Float.valueOf(content[1]);
                matelials.get(targetMatelial).d_g = Float.valueOf(content[2]);
                matelials.get(targetMatelial).d_b = Float.valueOf(content[3]);
            }else if(content[0].equals("Ks")){
                //スペキュラー
                matelials.get(targetMatelial).s_r = Float.valueOf(content[1]);
                matelials.get(targetMatelial).s_g = Float.valueOf(content[2]);
                matelials.get(targetMatelial).s_b = Float.valueOf(content[3]);
            }else if(content[0].equals("Ke")){
                //スペキュラー
                matelials.get(targetMatelial).e_r = Float.valueOf(content[1]);
                matelials.get(targetMatelial).e_g = Float.valueOf(content[2]);
                matelials.get(targetMatelial).e_b = Float.valueOf(content[3]);
            }else if(content[0].equals("d")) {
                //ディゾルブ
                matelials.get(targetMatelial).d = Float.valueOf(content[1]);
            }else if(content[0].equals("map_Kd")){
                matelials.get(targetMatelial).texture = ImageReader.readImageToAssets(content[1]);
            }
        }
        return matelials;
    }
}
