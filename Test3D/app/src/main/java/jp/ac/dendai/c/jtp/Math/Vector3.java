package jp.ac.dendai.c.jtp.Math;

/**
 * Created by Goto on 2016/06/28.
 */
public class Vector3 extends Vector {
    private float x,y,z;

    public Vector3(){
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3(Vector vec) {
        x = vec.getX();
        y = vec.getY();
        z = vec.getZ();
    }

    public Vector3(float x,float y,float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void zeroReset() {
        x = 0;
        y = 0;
        z = 0;
    }

    @Override
    public void setX(float value) {
        x = value;
    }

    @Override
    public void setY(float value) {
        y = value;
    }

    @Override
    public void setZ(float value) {
        z = value;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }

    @Override
    public void add(Vector vec) {
        x += vec.getX();
        y += vec.getY();
        z += vec.getZ();
    }

    @Override
    public void add(float scalar) {
        x += scalar;
        y += scalar;
        z += scalar;
    }

    @Override
    public void sub(Vector vec) {
        x -= vec.getX();
        y -= vec.getY();
        z -= vec.getZ();
    }

    @Override
    public void sub(float scalar) {
        x -= scalar;
        y -= scalar;
        z -= scalar;
    }

    @Override
    public void scalarMult(float scalar) {
        x = x * scalar;
        y = y * scalar;
        z = z * scalar;
    }

    @Override
    public void scalarDiv(float scalar) {
        if(scalar != 0){
            x /= scalar;
            y /= scalar;
            z /= scalar;
        }
    }

    @Override
    public float dot(Vector vec) {
        return x*vec.getX() + y*vec.getY() + z*vec.getZ();
    }

    @Override
    public Vector getCross(Vector vec) {
        Vector3 vec3 = new Vector3();
        vec3.setX(getCrossX(vec));
        vec3.setY(getCrossY(vec));
        vec3.setZ(getCrossZ(vec));
        return vec3;
    }

    @Override
    public void cross(Vector vec) {
        float lx = getCrossX(vec);
        float ly = getCrossY(vec);
        float lz = getCrossZ(vec);
        x = lx;
        y = ly;
        x = lz;
    }

    @Override
    public float getCrossX(Vector vec) {
        return y*vec.getZ() - z*vec.getY();
    }

    @Override
    public float getCrossY(Vector vec) {
        return z*vec.getX() -x*vec.getZ();
    }

    @Override
    public float getCrossZ(Vector vec) {
        return x*vec.getY()-y*vec.getX();
    }

    @Override
    public Vector getNormalize() {
        Vector3 vec3 = new Vector3(this);
        vec3.normalize();
        return vec3;
    }

    @Override
    public float getSqrMagnitude() {
        return x*x + y*y + z*z;
    }

    @Override
    public float getMagnitude() {
        return (float)Math.sqrt(getSqrMagnitude());
    }

    @Override
    public void normalize() {
        //ゼロベクトルなら何もせずに終了
        if(x == 0 && y == 0 && z == 0)
            return;
        float dim = getMagnitude();
        x /= dim;
        y /= dim;
        z /= dim;
    }

    @Override
    public Vector copy() {
        return new Vector3(this);
    }

    @Override
    public void copy(Vector vec) {
        x = vec.getX();
        y = vec.getY();
        z = vec.getZ();
    }
    @Override
    public String toString(){
        return "("+x+","+y+","+z+")";
    }
}
