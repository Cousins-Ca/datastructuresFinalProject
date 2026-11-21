package game.util;

import java.util.Map;
import java.util.HashMap;

public class Sprite {
    
    Texture texture;
    private Map<String, Mesh> meshes;
    
    public Sprite(String textureName) {
        texture = new Texture(textureName);
        meshes = new HashMap<>();
    } // Sprite
    
    public Texture getTexture() {
        return texture;
    } // getTextures

    public void addMesh(String meshId, Mesh mesh) {
        meshes.put(meshId, mesh);
    } // addMesh
    
    public Map<String, Mesh> getMeshes() {
        return meshes;
    } // getMeshes 

    public void cleanup() {
        meshes.values().forEach(Mesh::cleanup);
        texture.cleanup();
    } // cleanup
    
    public Texture getTexture0() {
        return texture;
    }
    
} // Sprite

