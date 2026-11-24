package game.util;

import java.util.Stack;

import game.element.*;
import game.interfaces.*;

public class GameHandler {

    public enum GameState {
        MENU,
        LEVEL;
    }

    private final Window window;
    private RenderHandler render;
    private Stack<Scene> scenes;
    private InputHandler input;
    private GameState gameState;
    private SaveManager saveManager;
    private boolean popStack;
    private int counter = 0;

    public GameHandler(String title) {
        window = new Window(title);
        render = new RenderHandler();
        scenes = new Stack<>();
        saveManager = new SaveManager();
        input = new InputHandler(saveManager);
        gameState = GameState.MENU;
        popStack = false;
    } // GameHandler

    public void init() {
        scenes.push(SceneBuilder.buildScene("topMenu", window));
    } // init

    public GameState getState() {
        return gameState;
    } // getState
    
    public void cleanup() {
        render.cleanup();
        window.cleanup();
    } // cleanup

    public void run() {
        int prevHeight = window.getHeight();
        int prevWidth = window.getWidth();
        while (!scenes.isEmpty() && !window.windowShouldClose()) {
            window.pollEvents();
            // resizes ortho matrix if the height or width of window has changed
            if (window.getHeight() != prevHeight || window.getWidth() != prevWidth) {
                prevHeight = window.getHeight();
                prevWidth = window.getWidth();
                scenes.peek().resize(window.getHeight(), window.getWidth());
            }
            if (gameState.equals(GameState.LEVEL)) {
                input.inputLevel(window, scenes.peek().getPlayer());
                updateLevel();
            } else if (gameState.equals(GameState.MENU)) {
                input.inputMenu(window, scenes.peek().getCursor(), saveManager);
                updateMenu();
            }
            render.render(window, scenes.peek(), this);
            if (input.getOpenMenu()) {
                gameState = GameState.MENU;
                float f = 2.5f;
                for (MenuBox mb : scenes.peek().getMenuBoxes().get(0)) {
                    mb.update(scenes.peek().getCamera().getPosition().x, scenes.peek().getCamera().getPosition().y + f);
                    f -= 2.5;
                }
            }
            if (popStack) {
                scenes.peek().cleanup();
                scenes.pop();
                if (!scenes.isEmpty()) {
                scenes.peek().getCursor().setX(0);
                scenes.peek().getCursor().setY(0);
                }
                popStack = false;
            }
            window.update();
        }
        cleanup();
    } // loop

    private void updateLevel() {
        float prevX = scenes.peek().getPlayer().getX();
        float prevY = scenes.peek().getPlayer().getY();
        float x = input.getPlayerX();
        float y = input.getPlayerY();
        float forceX = 0f;
        float forceY = 0f;
        boolean hitWall = false;
        boolean hitCeil = false;
        boolean hitFloor = false;
        boolean grounded = false;
        boolean moved = false;
        for (Enemy e : scenes.peek().getEnemies()) {
            e.update();
            if (scenes.peek().getPlayer().checkCollision(e, x, y) && e.onCooldown()) {
                if (scenes.peek().getPlayer().attacking()) {
                    y = scenes.peek().getPlayer().intendedYPos(Player.Boost.ENEMY, 0);
                    e.hit(true);
                } else if (!scenes.peek().getPlayer().isInvincible() && !e.isCleared()) {
                    scenes.peek().getPlayer().hit();
                    e.hit(false);
                    y = scenes.peek().getPlayer().intendedYPos(Player.Boost.HIT, 0);
                    System.out.println(scenes.peek().getPlayer().getHealth());
                }
            }
        }
        for (GameObject o : scenes.peek().getObjects()) {
            o.update();
            if (o.getType().equals(GameObject.ObjectType.SOLID)) {
                if (scenes.peek().getPlayer().checkCollision(o, x, y)) {
                    if (!moved || Math.abs(x) + o.getForceX() < Math.abs(x)) {
                        x += o.getForceX();
                        prevX += o.getForceX();
                        forceX = o.getForceX();
                    }
                    if (!moved || Math.abs(y) + o.getForceY() < Math.abs(y)) {
                        y += o.getForceY();
                        prevY += o.getForceY();
                        forceY = o.getForceY();
                    }
                    if (y < (o.getY() + o.getSizeY()) && Math.abs(prevY - o.getY() - o.getSizeY()) < 0.25f) {
                        y = o.getY() + o.getSizeY();
                        hitFloor = scenes.peek().getPlayer().getVSpd() <= 0;
                    }
                    if (y + scenes.peek().getPlayer().getSizeY() > (o.getY() - o.getSizeY()) && prevY + scenes.peek().getPlayer().getSizeY() <= (o.getY() - o.getSizeY())) {
                        y = o.getY() - o.getSizeY() - scenes.peek().getPlayer().getSizeY();
                        hitCeil = scenes.peek().getPlayer().getVSpd() >= 0;
                    }
                    if (x - scenes.peek().getPlayer().getSizeX() < (o.getX() + o.getSizeX()) && Math.abs(prevX - scenes.peek().getPlayer().getSizeX() - o.getX() - o.getSizeX()) < 0.2f) {
                        if (Math.abs((y - (o.getY() + o.getSizeY()))) < 0.2f && scenes.peek().getPlayer().getVSpd() <= 0) {
                            y = o.getY() + o.getSizeY();
                            hitFloor = true;
                            grounded = true;
                        } else {
                            x = o.getX() + o.getSizeX() + scenes.peek().getPlayer().getSizeX();
                            hitWall = true;
                        }
                    }
                    if (x + scenes.peek().getPlayer().getSizeX() > (o.getX() - o.getSizeX()) && Math.abs(prevX + scenes.peek().getPlayer().getSizeX() - o.getX() + o.getSizeX()) < 0.2f) {
                        if (Math.abs((y - (o.getY() + o.getSizeY()))) < 0.2f && scenes.peek().getPlayer().getVSpd() <= 0) {
                            y = o.getY() + o.getSizeY();
                            hitFloor = true;
                            grounded = true;
                        } else {
                            x = o.getX() - o.getSizeX() - scenes.peek().getPlayer().getSizeX();
                            hitWall = true;
                        }
                    }
                }
                if (Math.abs(y - o.getY() - o.getSizeY()) < 0.11
                        && (x - scenes.peek().getPlayer().getSizeX()) < (o.getX() + o.getSizeX())
                        && (x + scenes.peek().getPlayer().getSizeX()) > (o.getX() - o.getSizeX())
                        && scenes.peek().getPlayer().getVSpd() <= 0) {
                    if (o.getForceY() <= 0) {
                        y = o.getY() + o.getSizeY();
                        x += o.getForceX();
                    }
                    grounded = true;
                    if (!moved || Math.abs(x) + o.getForceX() < Math.abs(x)) {
                        forceX = o.getForceX();
                    }
                    if (!moved || Math.abs(y) + o.getForceY() < Math.abs(y)) {
                        forceY = o.getForceY();
                    }
                    moved = true;
                }
            } else if (o.getType().equals(GameObject.ObjectType.SPRING)) {
                if (scenes.peek().getPlayer().checkCollision(o, x, y)) {
                    y = scenes.peek().getPlayer().intendedYPos(Player.Boost.SPRING, 0);
                }
            }
        }
        if (!grounded && scenes.peek().getPlayer().getState().equals(Player.State.GROUNDED)) {
            scenes.peek().getPlayer().setState(Player.State.AIRBORNE);
            scenes.peek().getPlayer().activateMidAirJump();
        }
        if (hitWall) {
            counter += 1;
        } else {
            counter = 0;
        }
        if (counter >= 10) {
            scenes.peek().getPlayer().updateFlow(-1);
        }
        scenes.peek().getPlayer().updatePosition(x, y, hitWall, hitCeil, hitFloor);
        if ((scenes.peek().getPlayer().getState().equals(Player.State.GROUNDED)
                || scenes.peek().getPlayer().getState().equals(Player.State.BOUNCE))
                && forceY >= 0) {
            scenes.peek().getPlayer().setVSpd(scenes.peek().getPlayer().getVSpd() + forceY);
        }
        scenes.peek().getCamera().updatePosition(scenes.peek().getPlayer().getX() + (scenes.peek().getPlayer().getHSpd() + forceX) * 50, scenes.peek().getPlayer().getY() + (scenes.peek().getPlayer().getVSpd() + forceY) * 8);
        // mesh parameters for scenes.peek().getPlayer()
        float[] positions = new float[]{
            scenes.peek().getPlayer().getX() + 0.25f, scenes.peek().getPlayer().getY() + 1.0f, 0f,
            scenes.peek().getPlayer().getX() + 0.25f, scenes.peek().getPlayer().getY(), 0f,
            scenes.peek().getPlayer().getX() - 0.25f, scenes.peek().getPlayer().getY(), 0f,
            scenes.peek().getPlayer().getX() - 0.25f, scenes.peek().getPlayer().getY() + 1.0f, 0f,};
        float[] texture = new float[]{
            1f, 0f, 0f,
            1f, 1f, 0f,
            0f, 1f, 0f,
            0f, 0f, 0f};
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,};
        Mesh mesh = new Mesh(positions, texture, indices);
        // cleans up previous scenes.peek().getPlayer() sprite to prevent memory leak
        if (scenes.peek().getSprites().get("player").getMeshes().get("player") != null) {
            scenes.peek().getSprites().get("player").getMeshes().get("player").cleanup();
        }
        scenes.peek().getSprites().get("player").addMesh("player", mesh);
        if (scenes.peek().getPlayer().getHealth() == 0 || scenes.peek().getPlayer().outOfBounds(scenes.peek().getBounds())) {
            String name = scenes.peek().getMenuBoxes().get(0).get(1).getString();
            scenes.peek().cleanup();
            scenes.pop();
            scenes.push(SceneBuilder.buildScene(name, window));
        }
    } // updateLevel

    private void updateMenu() {
        int x = input.getCursorX();
        int y = input.getCursorY();
        int xSize = scenes.peek().getMenuBoxes().size();
        int[] ySizes = new int[xSize];
        for (int i = 0; i < xSize; i++) {
            ySizes[i] = scenes.peek().getMenuBoxes().get(i).size();
        }
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x < xSize) {
            scenes.peek().getCursor().setX(x);
        } else {
            x = xSize - 1;
        }
        if (x < xSize && y < ySizes[x]) {
            scenes.peek().getCursor().setY(y);
        } else {
            y = ySizes[x] - 1;
        }
        if (input.getCancel()) {
            x = xSize - 1;
            y = ySizes[x] - 1;
            scenes.peek().getCursor().setX(x);
            scenes.peek().getCursor().setY(y);
            input.setHeldCursorPos(x, y);
        }
        MenuBox currBox = scenes.peek().getMenuBoxes().get(x).get(y);
        float[] positions = new float[]{
            currBox.getX() + currBox.getSizeX() + 0.15f, currBox.getY() - currBox.getSizeY() + 0.1f, 0,
            currBox.getX() + currBox.getSizeX() + 0.15f, currBox.getY() - currBox.getSizeY() - 0.15f, 0,
            currBox.getX() + currBox.getSizeX() - 0.1f, currBox.getY() - currBox.getSizeY() - 0.15f, 0,
            currBox.getX() + currBox.getSizeX() - 0.1f, currBox.getY() - currBox.getSizeY() + 0.1f, 0,
        };
        float[] texture = new float[]{
            1f, 0f, 0f,
            1f, 1f, 0f,
            0f, 1f, 0f,
            0f, 0f, 0f};
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2};
        Mesh mesh = new Mesh(positions, texture, indices);
        // cleans up previous cursor sprite to prevent memory leak
        if (scenes.peek().getSprites().get("cursor").getMeshes().get("cursor") != null) {
            scenes.peek().getSprites().get("cursor").getMeshes().get("cursor").cleanup();
        }
        scenes.peek().getSprites().get("cursor").addMesh("cursor", mesh);
        int newControl = input.getNewControl();
        if (newControl != -1) {
            input.updateControl(currBox.getString(), newControl, saveManager);
        }
        if (input.getSelect()) {
            switch (currBox.getBehavior()) {
                case POP -> {
                    popStack = true;
                }
                case PUSH, LOAD -> {
                    if (currBox.getBehavior().equals(MenuBox.Behavior.LOAD)) {
                        scenes.peek().cleanup();
                        scenes.pop();
                    }
                    scenes.push(SceneBuilder.buildScene(currBox.getString(), window));
                    gameState = (scenes.peek().isLevel() ? GameState.LEVEL : GameState.MENU);
                }
                case CLOSE -> gameState = GameState.LEVEL;
                case UPDATE -> {
                    if (currBox.getString().equals("reset")) {
                        saveManager.resetControls();
                        input.resetControls(saveManager);
                    } else {
                        input.setPolling(true);   
                    }
                }
            }
        }
    } // updateMenu

} // GameHandler
