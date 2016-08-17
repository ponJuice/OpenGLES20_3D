package jp.ac.dendai.c.jtp.openglesutil.Util.Math;

/**
 * Created by Goto on 2016/06/28.
 */
public class Vector2 implements Vector{
    float x,y;
    public Vector2(Vector vec){
        x = vec.getX();
        y = vec.getY();
    }
    public Vector2(){
        zeroReset();
    }
    public Vector2(float x,float y){
        this.x = x;
        this.y = y;
    }
    @Override
    public void zeroReset(){
        x = 0;
        y = 0;
    }

    @Override
    public void setX(float value){
        x = value;
    }
    @Override
    public void setY(float value){
        y = value;
    }
    @Override
    public void setZ(float value){
    }
    @Override
    public float getX(){
        return x;
    }
    @Override
    public float getY(){
        return y;
    }
    @Override
    public float getZ(){
        return 0;
    }

    @Override
    public void add(Vector vec) {
        x += vec.getX();
        y += vec.getY();
    }

    @Override
    public void add(float scalar) {
        x += scalar;
        y += scalar;
    }

    @Override
    public void sub(Vector vec) {
        x -= vec.getX();
        y -= vec.getY();
    }

    @Override
    public void sub(float scalar) {
        x -= scalar;
        y -= scalar;
    }

    @Override
    public void scalarMult(float scalar) {
        x *= scalar;
        y *= scalar;
    }

    @Override
    public void scalarDiv(float scalar) {
        if(scalar != 0){
            x /= scalar;
            y /= scalar;
        }
    }

    @Override
    public float dot(Vector vec){
        return vec.getX()*x+vec.getY()*y;
    }
    @Override
    public Vector getCross(Vector vec){
        Vector3 vec3 = new Vector3();
        vec3.setX(getCrossX(vec));
        vec3.setY(getCrossY(vec));
        vec3.setZ(getCrossZ(vec));
        return vec3;
    }
    @Override
    public void cross(Vector vec){
        float lx = getCrossX(vec);
        float ly = getCrossY(vec);
        float lz = getCrossZ(vec);
        x = lx;
        y = ly;
        x = lz;
    }
    @Override
    public float getCrossX(Vector vec){
        return y*vec.getZ();
    }
    @Override
    public float getCrossY(Vector vec){
        return -x*vec.getZ();
    }
    @Override
    public float getCrossZ(Vector vec){
        return x*vec.getY()-y*vec.getX();
    }
    @Override
    public Vector getNormalize(){
        Vector2 vec2 = new Vector2(this);
        vec2.normalize();
        return vec2;
    }

    @Override
    public float getSqrMagnitude() {
        return x*x + y*y;
    }

    @Override
    public float getMagnitude() {
        return (float)Math.sqrt(getSqrMagnitude());
    }

    @Override
    public void normalize() {
        //ゼロベクトルなら何もせずに終了
        if(x == 0 && y == 0)
            return;
        float dim = (float)Math.sqrt(x*x + y*y);
        x /= dim;
        y /= dim;
    }
    @Override
    public Vector copy(){
        return new Vector2(this);
    }
    @Override
    public void copy(Vector vec){
        x = vec.getX();
        y = vec.getY();
    }
    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
}
