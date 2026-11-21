package game.util;

import java.util.Map;
import java.util.HashMap;

public class SceneBuilder {
    
    private Map<String, Sprite> sprites;
    private Projection projection;
    
    public SceneBuilder(int width, int height) {
        sprites = new HashMap<>();
        projection = new Projection(width, height);
    } // SceneBuilder
    
    public void addSprite(String spriteId, Sprite sprite) {
        sprites.put(spriteId, sprite);
    } // addMesh
    
    public Map<String, Sprite> getSprites() {
        return sprites;
    } // getMeshes
    
    public Projection getProjection() {
        return projection;
    } // getProjection
    
    public void resize(int width, int height) {
        projection.updateProjection(width, height);
    } //resize
    
    public void cleanup() {
        sprites.values().forEach(Sprite::cleanup);
    } // cleanup
    
} // SceneBuilder