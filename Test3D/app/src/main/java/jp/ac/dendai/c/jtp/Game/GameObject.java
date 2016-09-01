package jp.ac.dendai.c.jtp.Game;

import android.util.Log;

import jp.ac.dendai.c.jtp.Graphics.Renderer.MeshRenderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;

/**
 * Created by Goto on 2016/08/31.
 */
public class GameObject{
    protected Vector pos,rot,scl;
    protected CircleCollider collider;
    protected MeshRenderer meshRenderer;
    public Renderer.RenderItem item;
    public Vector getPos(){
        return pos;
    }
    public CircleCollider getCollider(){
        return collider;
    }
    public Vector getRot(){
        return rot;
    }
    public Vector getScl(){
        return scl;
    }
    public void setMeshRenderer(MeshRenderer renderer){
        if(meshRenderer != null && meshRenderer != renderer)
            meshRenderer.removeItem(this);
        meshRenderer = renderer;
        meshRenderer.addItem(this);
    }
    public MeshRenderer getMeshRenderer(){
        return meshRenderer;
    }
    public void collEnter(ACollider col){Log.d("Collision", col.toString());};
    public void collExit(ACollider col){};
    public void collStay(ACollider col){};
}
