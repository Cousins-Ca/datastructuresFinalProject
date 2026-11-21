package game.util;

import org.joml.Matrix4f;

public class Projection {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_FAR = 1000.f;
    private static final float Z_NEAR = 0.01f;

    private Matrix4f projection;

    public Projection(int width, int height) {
        projection = new Matrix4f();
        updateProjection(width, height);
    }

    public Matrix4f getProjection() {
        return projection;
    }

    
    public void updateProjection(int width, int height) {
        projection.setPerspective(FOV, (float) width / height, Z_NEAR, Z_FAR);
    }
}