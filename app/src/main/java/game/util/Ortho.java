package game.util;

import org.joml.Matrix4f;

public class Ortho {

    private Matrix4f ortho;

    public Ortho(int width, int height) {
        ortho = new Matrix4f();
        updateOrtho(width, height);
    }

    public Matrix4f getOrtho() {
        return ortho;
    }
    
    public void updateOrtho(int width, int height) {
        ortho.setOrtho2D(-8f, 8f, -4.5f, 4.5f);
    }
}