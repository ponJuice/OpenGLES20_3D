package jp.ac.dendai.c.jtp.ModelConverter.Wavefront;

/**
 * Created by Goto on 2016/07/26.
 */
public class ObjUVReader extends ObjReader {
    public ObjUVReader(){
        super();
        identifier = "vt";
    }
    @Override
    public int read(String[] lines,int offset){
        int n = offset;
        for(;n < lines.length;n++){
            String[] charas = lines[n].split(" ");
            if(charas[0].equals(identifier)){
                buffer.add(Float.valueOf(charas[1]));
                buffer.add(1f - Float.valueOf(charas[2]));
            }else{
                break;
            }
        }
        return n;
    }
}
