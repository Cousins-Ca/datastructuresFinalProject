package game.util;

import org.joml.Vector3f;
import org.joml.Matrix4f;

public class Camera {
    
    private Vector3f position;
    private Matrix4f viewMatrix;
    private Vector3f prevPos;

    public Camera(float x, float y) {
        position = new Vector3f();
        viewMatrix = new Matrix4f();
        prevPos = new Vector3f();
        setPosition(x, y, 0);
    } // Camera

    public Vector3f getPosition() {
        return position;
    } // getPosition

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    } // getViewMatrix

    private void recalculate() {
        viewMatrix.identity()
                .translate(-position.x, -position.y, -position.z);
    } // recalculate

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    } // setPosition
    
    public void updatePosition(float x, float y) {
        prevPos.x = x;
        prevPos.y = y;
        position.smoothStep(prevPos, 1 / 10f, position);
        recalculate();
    } // updatePosition

}