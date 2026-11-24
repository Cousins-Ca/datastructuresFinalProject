package game.interfaces;

import game.util.Sprite;

public interface GameObject {

    public enum ObjectType {
        SOLID,
        SPRING;
    }
    
    float getX();
    
    float getY();
    
    float getSizeX();
    
    float getSizeY();
    
    ObjectType getType();
    
    Sprite getSprite();
    
    void cleanup();
    
    void update();
    
    float getForceY();
    
    float getForceX();
    
} // Collidable