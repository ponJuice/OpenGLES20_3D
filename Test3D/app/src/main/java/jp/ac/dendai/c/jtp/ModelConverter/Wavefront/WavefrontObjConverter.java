package jp.ac.dendai.c.jtp.ModelConverter.Wavefront;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Material.Matelial;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.Model;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.ModelObject;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;

public class WavefrontObjConverter {
	public static ModelObject[] createModel(String modelFileName){
		String code = FileManager.readTextFile(modelFileName);
		Log.d("model fila",code);
		LinkedList<ModelObject> models = new LinkedList<>();
		ObjVertexReader vr = new ObjVertexReader();
		ObjNormalReader nr = new ObjNormalReader();
		ObjUVReader ur = new ObjUVReader();
		ObjIndexReader ir = new ObjIndexReader();
		HashMap<String,Matelial> matelials = null;

		//行で分割
        String[] lines = code.split("\n");
		int index = 0;
        for(int n=0;n < lines.length;n++) {
			String[] charas = lines[n].split(" ");
			if (charas[0].equals("#")) {
				//コメント行は処理をしない]
			} else if (charas[0].equals("mtllib")) {
				//マテリアルを読み込む
				matelials = WavefrontMtlReader.createMaterial(FileManager.readTextFile(charas[1]));
			} else if (charas[0].equals("o")) {
				vr.clear();
				nr.clear();
				ur.clear();
				ir.clear();
				n = vr.read(lines, n + 1);
				n = ur.read(lines, n);
				n = nr.read(lines, n);
				ir.read(lines, n, vr.getBuffer(), nr.getBuffer(), ur.getBuffer(), matelials);
				models.add(new ModelObject((Float[]) ir.getConvertVertex().toArray(new Float[0]),
						(Integer[]) ir.getIndex().toArray(new Integer[0]),
						ir.getFace().toArray(new Face[0])));

				for(int a = 0;a < ir.getConvertVertex().size();a += 8){
					String str = String.format("%+.1f %+.1f %+.1f , %+.1f %+.1f %+.1f , %+.3f %+.3f"
							,ir.getConvertVertex().get(a)
							,ir.getConvertVertex().get(a+1)
							,ir.getConvertVertex().get(a+2)
							,ir.getConvertVertex().get(a+3)
							,ir.getConvertVertex().get(a+4)
							,ir.getConvertVertex().get(a+5)
							,ir.getConvertVertex().get(a+6)
							,ir.getConvertVertex().get(a+7));
					String str2 = String.format("vertex %2d",(a/8+1));
					Log.d(str2,str);

				}
				for(int a = 0;a < ir.getIndex().size();a += 3){
					String str = String.format("%2d %2d %2d"
							,ir.getIndex().get(a)
							,ir.getIndex().get(a+1)
							,ir.getIndex().get(a+2));
					Log.d("index",str);
				}
			}
		}
		return models.toArray(new ModelObject[0]);
	}
	public static String createModelFile(Model model){
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
}
