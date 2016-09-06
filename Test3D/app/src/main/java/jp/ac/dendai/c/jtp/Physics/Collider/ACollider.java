package jp.ac.dendai.c.jtp.Physics.Collider;


import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Math.Vector;

/**
 * Created by Goto on 2016/08/31.
 */
public abstract class ACollider {
    protected GameObject gameObject;
    public GameObject getGameObject(){
        return gameObject;
    }
    public void setGameObject(GameObject gameObject){
        this.gameObject = gameObject;
    }
    public abstract Vector[] getDirect();

    public static boolean isCollision(CircleCollider A,CircleCollider B){
        if(A.radius + B.radius <= Vector.distanceAtoB(A.gameObject.getPos(),B.gameObject.getPos())){
            return false;
        }else{
            return true;
        }
    }
}
