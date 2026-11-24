package game.interfaces;

import game.util.Sprite;

public interface Enemy {
    
    float getX();
    
    float getY();
    
    float getSizeX();
    
    float getSizeY();
    
    Sprite getSprite();
    
    void cleanup();
    
    void update();
    
    void hit(boolean b);
    
    boolean isCleared();
    
    boolean onCooldown();
    
} // Enemy