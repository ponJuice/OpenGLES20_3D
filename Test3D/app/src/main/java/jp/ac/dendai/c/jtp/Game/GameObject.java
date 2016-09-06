package jp.ac.dendai.c.jtp.Game;

import android.util.Log;

import jp.ac.dendai.c.jtp.Graphics.Renderer.RenderMediator;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;

/**
 * Created by Goto on 2016/08/31.
 */
public class GameObject{
    protected Vector pos,rot,scl;
    protected CircleCollider collider;
    protected PhysicsObject po;
    protected RenderMediator rm;
    public GameObject(){
        pos = new Vector3();
        rot = new Vector3();
        scl = new Vector3(1,1,1);

        //物理オブジェクト
        po = new PhysicsObject(this);
        po.mass = 1;
        po.freeze = false;
        po.useGravity = false;

        //レンダーメディエイターの設定
        rm = new RenderMediator();
        rm.gameObject = this;
        rm.alpha = 1.0f;
    }
    public RenderMediator getRenderMediator(){
        return rm;
    }
    public Vector getPos(){
        return pos;
    }
    public CircleCollider getCollider(){
        return collider;
    }
    public void setCollider(CircleCollider col){
        collider = col;
        collider.setGameObject(this);
    }
    public Vector getRot(){
        return rot;
    }
    public Vector getScl(){
        return scl;
    }
    public void collEnter(ACollider col){
        rm.alpha = 0.5f;
    };
    public void collExit(){
        rm.alpha = 1.0f;
    };
    public void collStay(){
        rm.alpha = 0.5f;
    };
}
