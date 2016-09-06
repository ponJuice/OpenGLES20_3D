package jp.ac.dendai.c.jtp.Physics.Collider;

import jp.ac.dendai.c.jtp.Math.Vector;

/**
 * Created by Goto on 2016/08/31.
 */
public class CircleCollider extends ACollider{
    protected float radius;
    public CircleCollider(float radius){
        this.radius  = radius;
    }
    @Override
    public Vector[] getDirect() {
        return new Vector[0];
    }
}
