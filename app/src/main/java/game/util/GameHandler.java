package game.util;

import game.element.Player;

public class GameHandler {

    public static final int TARGET_UPS = 30;
    private final Window window;
    private RenderHandler render;
    private SceneBuilder scene;
    private InputHandler input;
    private boolean running;
    private final int ups = 60;
    private Player player;

    public GameHandler(String title) {
        window = new Window(title);
        render = new RenderHandler();
        scene = new SceneBuilder(window.getWidth(), window.getHeight());
        input = new InputHandler();
        running = true;
        player = new Player(0f, -3.0f, false);
    } // GameHandler

    public void init() {
        // mesh parameters for background
        float[] positions = new float[]{
            -4f, 3f, -6.0f,
            -4f, -3f, -6.0f,
            4f, -3f, -6.0f,
            4f, 3f, -6.0f,};
        float[] texture = new float[]{
            -4f, 3f, -6.0f,
            -4f, -3f, -6.0f,
            4f, -3f, -6.0f,
            4f, 3f, -6.0f,};
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,};
        Mesh mesh = new Mesh(positions, texture, indices);
        scene.addSprite("background", new Sprite("Background-PLACEHOLDER.png"));
        scene.getSprites().get("background").addMesh("background", mesh);
    } // init

    public void cleanup() {
        render.cleanup();
        scene.cleanup();
        window.cleanup();
    } // cleanup

    public void stop() {
        running = false;
    } // run

    public void run() {
        float delta;
        float accumulator = 0f;
        float interval = 1f / ups;
        float alpha;
        int prevHeight = window.getHeight();
        int prevWidth = window.getWidth();
        while (running && !window.windowShouldClose()) {
            window.pollEvents();
            // resizes projection matrix if the height or width of window has changed
            if (window.getHeight() != prevHeight || window.getWidth() != prevWidth) {
                prevHeight = window.getHeight();
                prevWidth = window.getWidth();
                scene.resize(window.getHeight(), window.getWidth());
            }
            delta = window.getDelta();
            accumulator += delta;
            input.input(window, player);
            while (accumulator >= interval) {
                update();
                accumulator -= interval;
            }

            alpha = accumulator / interval;
            render.render(window, scene);
            window.update();

        }
        cleanup();
    } // loop

    private void update() {
        float x = input.getX();
        float y = input.getY();
        // temporary code to prevent player from getting stuck oob
        if (x <= -4f) {
            x = -4f;
        }
        if (x >= 4f) {
            x = 4f;
        }
        if (y <= -3f) {
            y = -3f;
        }
        if (y >= 3f) {
            y = 3f;
        }
        player.updatePosition(x, y, (x <= -4f || x >= 4f), y >= 3f, y <= -3f);
        // mesh parameters for player
        float[] positions = new float[]{
            player.getX() + 0.05f, player.getY() + 0.2f, -6.0f,
            player.getX() + 0.05f, player.getY(), -6.0f,
            player.getX() - 0.05f, player.getY(), -6.0f,
            player.getX() - 0.05f, player.getY() + 0.2f, -6.0f,};
        float[] colors = new float[]{
            player.getX() + 0.05f, player.getY() + 0.2f, -6.0f,
            player.getX() + 0.05f, player.getY(), -6.0f,
            player.getX() - 0.05f, player.getY(), -6.0f,
            player.getX() - 0.05f, player.getY() + 0.2f, -6.0f,};
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,};
        Mesh mesh = new Mesh(positions, colors, indices);
        // cleans up previous player sprite to prevent memory leak
        if (scene.getSprites().get("player") != null) {
            scene.getSprites().get("player").cleanup();
        }
        scene.addSprite("player", new Sprite("Kila-PLACEHOLDER.png"));
        scene.getSprites().get("player").addMesh("player", mesh);
    } // update

} // GameHandler
