package jp.ac.dendai.c.jtp.Game;

import android.util.Log;

import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;

/**
 * Created by Goto on 2016/08/31.
 */
public class GameObject {
    protected Vector pos,rot,scl;
    protected CircleCollider collider;
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
    public void collEnter(ACollider col){Log.d("Collision", col.toString());};
    public void collExit(ACollider col){};
    public void collStay(ACollider col){};
}
