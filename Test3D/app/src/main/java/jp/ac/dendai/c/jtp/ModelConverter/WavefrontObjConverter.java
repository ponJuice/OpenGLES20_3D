package jp.ac.dendai.c.jtp.ModelConverter;

import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Graphics.Model;

public class WavefrontObjConverter {
	public static Model createModel(String code){
		LinkedList<Float> vertex = new LinkedList<>();
		LinkedList<Float> normal= new LinkedList<>();
		LinkedList<Float> texture = new LinkedList<>();
		LinkedList<Integer> v_indices = new LinkedList<>();
		LinkedList<Integer> n_indices = new LinkedList<>();
		LinkedList<Integer> t_indices = new LinkedList<>();
		String modelName = "";
		String matelialName = "";
        String[] lines = code.split("\n");
        for(int n=0;n < lines.length;n++) {
            String[] charas = lines[n].split(" ");
            if(charas[0].equals("#")){
            	//コメント行は処理をしない]
            }
            else if(charas[0].equals("g")){
            	modelName = charas[1];
            }
            else if(charas[0].equals("usemtl")){
            	matelialName = charas[1];
            }
            else if(charas[0].equals("v")){
            	vertex.add(Float.valueOf(charas[1]));
            	vertex.add(Float.valueOf(charas[2]));
            	vertex.add(Float.valueOf(charas[3]));
            }else if(charas[0].equals("vn")){
            	normal.add(Float.valueOf(charas[1]));
            	normal.add(Float.valueOf(charas[2]));
            	normal.add(Float.valueOf(charas[3]));
            }else if(charas[0].equals("vt")){
            	texture.add(Float.valueOf(charas[1]));
            	texture.add(Float.valueOf(charas[2]));
            	texture.add(Float.valueOf(charas[3]));
            }else if(charas[0].equals("f")){
            	for(int m = 1;m < charas.length ;m++){
            		String[] t = charas[m].split("/");
            		if(!t[0].equals("")){
            			v_indices.add(Integer.valueOf(t[0]));
            		}
            		if(!t[1].equals("")){
            			t_indices.add(Integer.valueOf(t[1]));
            		}
            		if(!t[2].equals("")){
            			n_indices.add(Integer.valueOf(t[2]));
            		}
            	}
            }
        }
        return new Model(modelName,matelialName,
        		(Float[])vertex.toArray(new Float[0]),
        		(Float[])normal.toArray(new Float[0]),
        		(Float[])texture.toArray(new Float[0]),
        		(Integer[])v_indices.toArray(new Integer[0]),
        		(Integer[])n_indices.toArray(new Integer[0]),
        		(Integer[])t_indices.toArray(new Integer[0]));
	}
	public static String createModelFile(Model model){
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
}
