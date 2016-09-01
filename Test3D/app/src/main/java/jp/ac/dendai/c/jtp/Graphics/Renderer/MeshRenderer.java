package jp.ac.dendai.c.jtp.Graphics.Renderer;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;

/**
 * Created by Goto on 2016/09/01.
 */
public class MeshRenderer extends Renderer{
    private Shader shader;
    private Mesh mesh;
    private float alpha;
    private GameObject gameObject;
    public MeshRenderer(GameObject gameObject,Shader shader,Mesh mesh){
        super();
        this.shader = shader;
        this.mesh = mesh;
        this.gameObject = gameObject;
        alpha = 1f;
    }
    public Shader getShader(){
        return shader;
    }
    public void setShader(Shader shader){
        this.shader = shader;
    }
    public Mesh getMesh(){
        return mesh;
    }
    public void setMesh(Mesh mesh){
        this.mesh = mesh;
    }
    public float getAlpha(){
        return alpha;
    }
    public void setAlpha(float alpha){
        this.alpha = alpha;
    }
    @Override
    public void draw(){
        shader.draw(mesh,gameObject.getPos().getX(),gameObject.getPos().getY(),gameObject.getPos().getZ()
        ,gameObject.getScl().getX(),gameObject.getScl().getY(),gameObject.getScl().getZ()
        ,gameObject.getRot().getX(),gameObject.getRot().getY(),gameObject.getScl().getZ()
        ,alpha);
    }
}
