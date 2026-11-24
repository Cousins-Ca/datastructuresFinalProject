package game.util;

import game.interfaces.*;
import game.element.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Scene {

    private Map<String, Sprite> sprites;
    private Ortho ortho;
    private Camera camera;
    private List<GameObject> objects;
    private List<Enemy> enemies;
    private List<List<MenuBox>> menuBoxes;
    private Player player;
    private Cursor cursor;
    private float[] bounds;
    private boolean isLevel;

    public Scene(int width, int height, boolean isLevel, float x, float y, float[] bounds) {
        sprites = new HashMap<>();
        ortho = new Ortho(width, height);
        camera = new Camera(x, y);
        objects = new ArrayList<>();
        enemies = new ArrayList<>();
        menuBoxes = new ArrayList<>();
        player = new Player(x, y);
        cursor = new Cursor();
        this.bounds = bounds;
        this.isLevel = isLevel;
    } // Scene

    public Scene(int width, int height, boolean isLevel) {
        sprites = new HashMap<>();
        ortho = new Ortho(width, height);
        camera = new Camera(0, 0);
        objects = new ArrayList<>();
        enemies = new ArrayList<>();
        menuBoxes = new ArrayList<>();
        player = new Player(0, 0);
        cursor = new Cursor();
        this.bounds = new float[]{-10, 10, -10, 10};
        this.isLevel = isLevel;
    } // Scene

    public Player getPlayer() {
        return player;
    } // getPlayer

    public Cursor getCursor() {
        return cursor;
    } // getCursor

    public float[] getBounds() {
        return bounds;
    } // getBounds

    public void addSprite(String spriteId, Sprite sprite) {
        sprites.put(spriteId, sprite);
    } // addMesh

    public Map<String, Sprite> getSprites() {
        return sprites;
    } // getMeshes

    public Ortho getOrtho() {
        return ortho;
    } // getOrtho

    public void resize(int width, int height) {
        ortho.updateOrtho(width, height);
    } //resize

    public void cleanup() {
        sprites.values().forEach(Sprite::cleanup);
        for (GameObject o : objects) {
            o.cleanup();
        }
        for (Enemy e : enemies) {
            e.cleanup();
        }
        for (List<MenuBox> lmb : menuBoxes) {
            for (MenuBox mb : lmb) {
                mb.cleanup();
            }
        }
        // add in cleanup for other instance variables
    } // cleanup

    public Camera getCamera() {
        return camera;
    } // Camera 

    public List<GameObject> getObjects() {
        return objects;
    } // getObjects

    public void addObject(GameObject object) {
        objects.add(object);
    } // addObject

    public List<Enemy> getEnemies() {
        return enemies;
    } // getEnemies

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    } // addEnemy

    public boolean isLevel() {
        return isLevel;
    } // isLevel

    public void addMenuBox(MenuBox box, int x) {
        if (x < menuBoxes.size()) {
            menuBoxes.get(x).add(box);
        } else if (x == menuBoxes.size()) {
            menuBoxes.add(new ArrayList<>());
            menuBoxes.get(x).add(box);
        }
    } // addMenuBox

    public List<List<MenuBox>> getMenuBoxes() {
        return menuBoxes;
    } // getMenuBoxes

} // Scene
