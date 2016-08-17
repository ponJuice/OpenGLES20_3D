package jp.ac.dendai.c.jtp.openglesutil.core.Shader;

import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.Environment;
import jp.ac.dendai.c.jtp.openglesutil.GameObject;
import jp.ac.dendai.c.jtp.openglesutil.core.Shader.Shader;

/**
 * Created by Goto on 2016/08/17.
 */
public class Renderer {
    Renderer next,prev;
    Environment environment;
    GameObject gameObject;
    Mesh mesh;
    Shader shader;
    boolean alive;
    private float alpha;
    void remove(Shader s){
        if(shader != s)
            return;
        if(next.getClass() != null && prev.getClass() != null) {
            next.prev = prev;
            prev.next = next;
        }else if(next.getClass() == null && prev.getClass() == null){
            s.first = null;
            s.end = null;
        }
        else if(s.first == this){
            next.prev = null;
            s.first = next;
        }else if(s.end == this){
            prev.next = null;
            s.end = prev;
        }
    }
}
