package game.element;

import game.util.Sprite;
import game.util.Mesh;

public class MenuBox {
    
    public enum Behavior {
        PUSH,
        POP,
        LOAD,
        UPDATE,
        CLOSE;
    }
    
    private float x;
    private float y;
    private float sizeX;
    private float sizeY;
    private Sprite sprite;
    private Behavior behavior;
    private String string;
    
    public MenuBox(float x, float y, float sizeX, float sizeY, Behavior behavior, String string) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.behavior = behavior;
        this.string = string;
        sprite = new Sprite("MenuBox-PLACEHOLDER.png");
        createSpriteMesh();
    } // Box
    
    public float getX() {
        return x;
    } // getX

    public float getY() {
        return y;
    } // getY
    
    public float getSizeX() {
        return sizeX;
    } // getSizeX
    
    public float getSizeY() {
        return sizeY;
    } // getSizeY
    
    public Sprite getSprite() {
        return sprite;
    } // getSprite 
    
    public Behavior getBehavior() {
        return behavior;
    } // getBehavior
    
    public String getString() {
        return string;
    } // getFilename
    
    public void cleanup() {
        sprite.cleanup();
    } // cleanup
    
    public void update(float x, float y) {
        this.x = x;
        this.y = y;
        createSpriteMesh();
    } // update
    
    private void createSpriteMesh() {
        float[] positions = new float[]{
            x - sizeX, y + sizeY, 0f,
            x - sizeX, y - sizeY, 0f,
            x + sizeX, y - sizeY, 0f,
            x + sizeX, y + sizeY, 0f,};
        float[] texture = new float[]{
            0f, 0f, 0f,
            0f, 1f, 0f,
            1f, 1f, 0f,
            1f, 0f, 0f,};
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,};
        Mesh mesh = new Mesh(positions, texture, indices);
        if (sprite.getMeshes().get("menubox") != null)
            sprite.getMeshes().get("menubox").cleanup();
        sprite.addMesh("menubox", mesh);
    } // createSpriteMesh
    
} // Box
