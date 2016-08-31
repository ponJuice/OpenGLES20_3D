package jp.ac.dendai.c.jtp.Physics.Physics;

/**
 * Created by Goto on 2016/08/31.
 */
public interface Physics {
    void preparation();
    void externalForceProc(float deltaTime);
    void physicsProc(float deltaTime);
    void updatePosProc(float deltaTime);
    void simulate();
}
