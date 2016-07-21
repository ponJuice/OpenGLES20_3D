package jp.ac.dendai.c.jtp.ModelConverter.Wavefront;

import android.util.Log;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import jp.ac.dendai.c.jtp.Graphics.Matelial;
import jp.ac.dendai.c.jtp.Graphics.Model;
import jp.ac.dendai.c.jtp.Graphics.ModelObject;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;

public class WavefrontObjConverter {
	public static ModelObject[] createModel(String modelFileName){
		String code = FileManager.readTextFile(modelFileName);
		LinkedList<Float> vertex = new LinkedList<>();
		LinkedList<Float> normal= new LinkedList<>();
		LinkedList<Float> texture = new LinkedList<>();
		LinkedList<Integer> v_indices = new LinkedList<>();
		LinkedList<Integer> result_v_indices = new LinkedList<>();
		LinkedList<Integer> n_indices = new LinkedList<>();
		LinkedList<Integer> t_indices = new LinkedList<>();
		LinkedList<Float[]> normal_result = new LinkedList<>();
		LinkedList<ModelObject> models = new LinkedList<>();
		HashMap<String,Matelial> matelials = null;
		String modelName = "";
		String matelialName = "";
        String[] lines = code.split("\n");
		int index = 0;
        for(int n=0;n < lines.length;n++) {
			String[] charas = lines[n].split(" ");
			if (charas[0].equals("#")) {
				//コメント行は処理をしない]
			}else if(charas[0].equals("mtllib")){
				//マテリアルを読み込む
				matelials = WavefrontMtlReader.createMaterial(FileManager.readTextFile(charas[1]));
			}
			else if (charas[0].equals("o")) {
				if(vertex.size() != 0){
					vertex.clear();
					for(Float[] f : normal_result){
						for(Float lf : f){
							vertex.add(lf);
						}
					}
					models.add(new ModelObject(modelName,matelialName,
							(Float[])vertex.toArray(new Float[0]),
							(Float[])normal.toArray(new Float[0]),
							(Float[])texture.toArray(new Float[0]),
							(Integer[])result_v_indices.toArray(new Integer[0]),
							(Integer[])n_indices.toArray(new Integer[0]),
							(Integer[])t_indices.toArray(new Integer[0]),
							matelials.get(matelialName)));
				}
				modelName = charas[1];
			} else if (charas[0].equals("usemtl")) {
				matelialName = charas[1];
			} else if (charas[0].equals("v")) {
				vertex.add(Float.valueOf(charas[1]));
				vertex.add(Float.valueOf(charas[2]));
				vertex.add(Float.valueOf(charas[3]));
			} else if (charas[0].equals("vn")) {
				normal.add(Float.valueOf(charas[1]));
				normal.add(Float.valueOf(charas[2]));
				normal.add(Float.valueOf(charas[3]));
			} else if (charas[0].equals("vt")) {
				texture.add(Float.valueOf(charas[1]));
				texture.add(1f- Float.valueOf(charas[2]));
			} else if (charas[0].equals("f")) {
				int indexs=0,indexe=0;
				for (int m = 1; m < charas.length; m++) {
					String[] t = charas[m].split("/");
					if(charas.length >= 5 && m == 1){
						indexs = index;
					}else if(charas.length >= 5 && m%3 == 0){
						indexe = index;
					}
					//頂点データ読み込み
					if (!t[0].equals("")) {
						v_indices.add(Integer.valueOf(t[0]) - 1);
						result_v_indices.add(index);
					}
					//法線データ読み込み
					if (!t[2].equals("")) {
						n_indices.add(Integer.valueOf(t[2]) - 1);
					}
					//UVデータ読み込み
					if (!t[1].equals("")) {
						t_indices.add(Integer.valueOf(t[1]) - 1);
					}else{
						t_indices.add(0);
					}
					Float[] buff = new Float[8];
					buff[0] = vertex.get(v_indices.getLast() * 3);
					buff[1] = vertex.get(v_indices.getLast() * 3 + 1);
					buff[2] = vertex.get(v_indices.getLast() * 3 + 2);
					buff[3] = normal.get(n_indices.getLast() * 3);
					buff[4] = normal.get(n_indices.getLast() * 3 + 1);
					buff[5] = normal.get(n_indices.getLast() * 3 + 2);
					buff[6] = texture.get(t_indices.getLast() * 2);
					buff[7] = texture.get(t_indices.getLast() * 2+1);
					normal_result.add(buff);

					if (charas.length == 5 && m == 4) {
						if (result_v_indices.size() != 0) {
							result_v_indices.add(indexs);
							result_v_indices.add(indexe);
						}
					}
					index++;
				}
			}
		}
		if(vertex.size() != 0){
			vertex.clear();
			for(Float[] f : normal_result){
				for(Float lf : f){
					vertex.add(lf);
				}
			}
		}
		vertex.clear();
		for(Float[] f : normal_result){
			for(Float lf : f){
				vertex.add(lf);
			}
		}
		models.add(new ModelObject(modelName,matelialName,
				(Float[])vertex.toArray(new Float[0]),
				(Float[])normal.toArray(new Float[0]),
				(Float[])texture.toArray(new Float[0]),
				(Integer[])result_v_indices.toArray(new Integer[0]),
				(Integer[])n_indices.toArray(new Integer[0]),
				(Integer[])t_indices.toArray(new Integer[0]),
				matelials.get(matelialName)));
       return models.toArray(new ModelObject[0]);
	}
	public static String createModelFile(Model model){
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
}
