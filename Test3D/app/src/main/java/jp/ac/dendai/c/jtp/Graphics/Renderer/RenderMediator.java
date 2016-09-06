package jp.ac.dendai.c.jtp.Graphics.Renderer;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;

/**
 * Created by テツヤ on 2016/09/03.
 */
public class RenderMediator {
    public float alpha;
    public Mesh mesh;
    public GameObject gameObject;
    public Renderer renderer;
    public Renderer.RenderItem item;
    public void draw(){
        if(mesh == null || renderer == null)
            return;
        renderer.shader.draw(mesh
                ,gameObject.getPos().getX(),gameObject.getPos().getY(),gameObject.getPos().getZ()
                ,gameObject.getScl().getX(),gameObject.getScl().getY(),gameObject.getScl().getZ()
                ,gameObject.getRot().getX(),gameObject.getRot().getY(),gameObject.getRot().getZ()
                ,alpha);
    }
}
