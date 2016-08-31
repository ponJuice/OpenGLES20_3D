package jp.ac.dendai.c.jtp.Physics.Physics;

import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;

/**
 * Created by Goto on 2016/08/31.
 */
public class Physics3D implements Physics {
    private String name;
    class PhysicsItem{
        public Physics3D owner;
        public PhysicsItem next,prev;
        public PhysicsObject object;
        public String getInfo(){
            return "owner name:"+owner.name+"\n"+
                    "next:"+next+" prev:"+prev+"\n"+
                    "object name:"+object.getName()+"\n";
        }
    }
    private PhysicsItem ite;
    private PhysicsItem[] objects;                          //あたり判定の総当たりをやりやすくするためのもの
    private int objectCount;
    private PhysicsInfo info;
    private long time,timeBuffer,startTime;
    private float deltaTime,deltaTimeSum,currentTime;
    private Vector3 buffer;
    public Physics3D(PhysicsInfo info){
        this.info = info;
        objectCount = 0;
        buffer = new Vector3();
        ite = createRingBuffer();
        objects = new PhysicsItem[info.maxObject];
        PhysicsItem temp = ite;
        for(int n = 0;n < info.maxObject;n++){
            objects[n] = temp;
            temp = temp.next;
        }
    }
    public void addObject(PhysicsObject object){
        if(objectCount >= info.maxObject) {
            ite.object.regist = null;
        }
        else {
            objectCount++;
        }

        ite.object = object;
        object.regist = ite;
        ite = ite.next;
    }
    public void removeObject(PhysicsObject object){
        if(object.regist == null)
            return;
        if(object.regist.owner != this)
            return;
        object.regist.prev.next = object.regist.next;
        object.regist.next.prev = object.regist.prev;
        object.regist.prev = ite;
        object.regist.next = ite.next;
        ite.next.prev = object.regist;
        ite.next = object.regist;
        object.regist.object = null;
        object.regist = null;
        objectCount--;
    }
    public void clearObjects(){
        PhysicsItem temp = ite;
        for(int n = 0;n < info.maxObject;n++){
            if(temp.object != null)
                temp.object.regist = null;
                temp.object = null;
            temp = temp.next;
        }
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public float getDeltaTime(){
        return deltaTime;
    }
    public float getCurrentTime(){
        return currentTime;
    }
    public float getDeltaTimeSum(){
        return deltaTimeSum;
    }
    private PhysicsItem createRingBuffer(){
        PhysicsItem start = new PhysicsItem();
        start.owner = this;
        PhysicsItem temp = start;
        for(int n = 1;n < info.maxObject;n++){
            temp.next = new PhysicsItem();
            temp.next.prev = temp;
            temp = temp.next;
            temp.owner = this;
        }
        start.prev = temp;
        temp.next = start;
        return start;
    }
    @Override
    public void preparation(){
        PhysicsItem temp = ite;
        do{
            if(!ite.object.freeze) {
                ite.object.bufferVelocity.zeroReset();
                ite.object.bufferPos.zeroReset();
                ite.object.bufferRot.zeroReset();
                ite.object.bufferScl.zeroReset();
            }
            ite = ite.next;
        }while(temp != ite);
    }
    @Override
    public void externalForceProc(float deltaTime) {
        //重力
        PhysicsItem temp = ite;
        do{
            if(ite.object.useGravity && !ite.object.freeze) {
                buffer.copy(info.gravity);
                buffer.scalarMult(deltaTime);
                ite.object.bufferVelocity.add(buffer);
            }
            ite = ite.next;
        }while(temp != ite);
    }

    @Override
    public void physicsProc(float deltaTime) {
        //あたり判定のみ今は実装
        for(int n = 0;n < info.maxObject;n++){
            if(objects[n].object == null)
                continue;
            for(int m = 1+n;m < info.maxObject;n++){
                if(objects[m].object == null)
                    continue;
                if(ACollider.isCollision(objects[n].object.gameObject.getCollider(),objects[m].object.gameObject.getCollider())){
                    objects[n].object.gameObject.collEnter(objects[m].object.gameObject.getCollider());
                    objects[m].object.gameObject.collEnter(objects[n].object.gameObject.getCollider());
                }
            }
        }
    }

    @Override
    public void updatePosProc(float deltaTime) {
        PhysicsItem temp = ite;
        do{
            if(!ite.object.freeze) {
                ite.object.velocity.add(ite.object.bufferVelocity);
                ite.object.bufferPos.copy(ite.object.velocity);
                ite.object.bufferPos.scalarMult(deltaTime);
                ite.object.gameObject.getPos().add(ite.object.bufferPos);
            }
            ite = ite.next;
        }while(temp != ite);
    }

    @Override
    public void simulate() {
        deltaTime = 0;
        timeBuffer = System.currentTimeMillis();
        if(time > 0)
            deltaTime = (timeBuffer - time)/1000f;
        else{
            startTime = timeBuffer;
        }
        currentTime = (timeBuffer - startTime)/1000f;
        time = timeBuffer;
        deltaTimeSum += deltaTime;
        preparation();
        externalForceProc(deltaTime);
        physicsProc(deltaTime);
        updatePosProc(deltaTime);
    }
}
