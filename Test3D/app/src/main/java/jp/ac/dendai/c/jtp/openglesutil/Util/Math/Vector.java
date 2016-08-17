package jp.ac.dendai.c.jtp.openglesutil.Util.Math;

/**
 * Created by Goto on 2016/06/28.
 */
public interface Vector {
    public void zeroReset();
    public void setX(float value);
    public void setY(float value);
    public void setZ(float value);
    public float getX();
    public float getY();
    public float getZ();
    public void add(Vector vec);
    public void add(float scalar);
    public void sub(Vector vec);
    public void sub(float scalar);
    public void scalarMult(float scalar);
    public void scalarDiv(float scalar);
    public float dot(Vector vec);
    public Vector getCross(Vector vec);
    public void cross(Vector vec);
    public float getCrossX(Vector vec);
    public float getCrossY(Vector vec);
    public float getCrossZ(Vector vec);
    public Vector getNormalize();
    public float getSqrMagnitude();
    public float getMagnitude();
    public void normalize();
    public Vector copy();
    public void copy(Vector vec);
}
