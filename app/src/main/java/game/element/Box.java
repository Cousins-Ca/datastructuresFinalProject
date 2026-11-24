package game.element;

import game.util.Sprite;
import game.util.Mesh;
import game.interfaces.GameObject;

public class Box implements GameObject {
    
    private float x;
    private float y;
    private float sizeX;
    private float sizeY;
    private ObjectType type;
    private Sprite sprite;
    private float movementX = 0f;
    private float movementY = 0f;
    
    public Box(float x, float y, float sizeX, float sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        type = ObjectType.SOLID;
        sprite = new Sprite("Box-PLACEHOLDER.png");
        createSpriteMesh();
    } // Box
    
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
    public ObjectType getType() {
        return type;
    } // getType
    
    @Override
    public Sprite getSprite() {
        return sprite;
    } // getSprite
    
    @Override
    public void cleanup() {
        sprite.cleanup();
    } // cleanup
    
    @Override
    public void update() {} // update

    @Override
    public float getForceX() {
        return movementX;
    } // getForceX
    
    @Override
    public float getForceY() {
        return movementY;
    } // getForceY
    
    protected void setX(float f) {
        x = f;
    } // setX
    
    protected void setY(float f) {
        y = f;
    } // setY
    
    protected void createSpriteMesh() {
        float[] positions = new float[]{
            x - sizeX, y + sizeY, 0f,
            x - sizeX, y - sizeY, 0f,
            x + sizeX, y - sizeY, 0f,
            x + sizeX, y + sizeY, 0f,};
        float[] texture = new float[]{
            0f, 1f, 0f,
            0f, 0f, 0f,
            1f, 0f, 0f,
            1f, 1f, 0f,};
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,};
        Mesh mesh = new Mesh(positions, texture, indices);
        if (sprite.getMeshes().get("box") != null)
            sprite.getMeshes().get("box").cleanup();
        sprite.addMesh("box", mesh);
    } // createSpriteMesh
    
} // Box
