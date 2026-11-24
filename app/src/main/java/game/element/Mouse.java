package game.element;

import game.util.Sprite;
import game.util.Mesh;
import game.interfaces.Enemy;

public class Mouse implements Enemy {
    
    private float x;
    private float y;
    private float sizeX;
    private float sizeY;
    private Sprite sprite;
    private boolean cleared;
    private int cooldown;
    
    
    public Mouse(float x, float y, float sizeX, float sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        sprite = new Sprite("Mouse-PLACEHOLDER.png");
        cleared = false;
        cooldown = 0;
        createSpriteMesh();
    } // Mouse
    
    @Override
    public float getX() {
        return x;
    } // getX

    @Override
    public float getY() {
        return y;
    } // getY

    @Override
    public float getSizeX() {
        return sizeX;
    } // getSizeX
    
    @Override
    public float getSizeY() {
        return sizeY;
    } // getSizeY

    @Override
    public Sprite getSprite() {
        return sprite;
    } // getSprite
    
    @Override
    public void cleanup() {
        sprite.cleanup();
    } // cleanup
    
    @Override
    public void update() {
        if (cooldown > 0) {
            cooldown--;
        }
    } // update

    @Override
    public void hit(boolean hit) {
        if (hit) {
            cleared = true;
        }
        cooldown = 15;
    } // clear
    
    @Override
    public boolean isCleared() {
        return cleared;
    } // isCleared
    
    @Override
    public boolean onCooldown() {
        return cooldown == 0;
    } // onCooldown
    
    protected void createSpriteMesh() {
        float[] positions = new float[]{
            x - sizeX, y + sizeY, 0f,
            x - sizeX, y - sizeY, 0f,
            x + sizeX, y - sizeY, 0f,
            x + sizeX, y + sizeY, 0f,};
        float[] texture = new float[]{
            1f, 0f, 0f,
            1f, 1f, 0f,
            0f, 1f, 0f,
            0f, 0f, 0f,};
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,};
        Mesh mesh = new Mesh(positions, texture, indices);
        if (sprite.getMeshes().get("mouse") != null)
            sprite.getMeshes().get("mouse").cleanup();
        sprite.addMesh("mouse", mesh);
    } // createSpriteMesh
    
} // Mouse