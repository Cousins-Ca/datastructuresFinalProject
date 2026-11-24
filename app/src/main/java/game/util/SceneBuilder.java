package game.util;

import game.element.*;

public class SceneBuilder {

    public static Scene buildScene(String filename, Window window) {
        if (filename.equals("testLevel")) {
            float[] bounds = new float[]{-60, 25, -7, 30};
            Scene scene = new Scene(window.getWidth(), window.getHeight(), true, 0f, -2f, bounds);
            scene.addObject(new Box(-15f, -4f, 30f, 1f));
            scene.addObject(new Box(4f, 2f, 1f, 1f));
            scene.addObject(new Box(10f, 2f, 1f, 2f));
            scene.addObject(new Box(-10f, -3f, 0.5f, 0.35f));
            scene.addObject(new Box(-11f, -3f, 0.5f, 0.2f));
            scene.addObject(new Box(-12f, -3f, 0.5f, 0.3f));
            scene.addObject(new Box(-13f, -3f, 0.5f, 0.4f));
            scene.addObject(new Box(-14f, -3f, 0.5f, 0.5f));
            scene.addObject(new Box(-15f, -3f, 0.5f, 0.6f));
            scene.addObject(new Box(-16f, -3f, 0.5f, 0.7f));
            scene.addObject(new Box(1f, -3f, 2.5f, 1f));
            scene.addObject(new Box(-1f, 2f, 0.5f, 0.8f));
            scene.addObject(new Box(-4f, 1f, 0.5f, 0.1f));
            scene.addObject(new Box(4f, 8f, 1f, 1f));
            scene.addObject(new Box(6f, 8f, 1f, 1f));
            scene.addObject(new Box(8f, 12f, 1f, 1f));
            scene.addObject(new Box(10f, 16f, 1f, 1f));
            scene.addObject(new Box(12f, 20f, 1f, 1f));
            scene.addObject(new MovingBox(-25f, -3f, 1f, 1f, 10f, 0f, MovingBox.MovementType.SINUSOIDX, -30f, 0.3f));
            scene.addObject(new MovingBox(-35f, -3f, 1f, 1f, 10f, 0f, MovingBox.MovementType.SINUSOIDY, -30f, 0.3f));
            scene.addObject(new MovingBox(-30f, -3f, 1f, 1f, 10f, 0f, MovingBox.MovementType.SINUSOIDU, -30f, 0.3f));
            scene.addObject(new MovingBox(-40f, -3f, 1f, 1f, 10f, 0f, MovingBox.MovementType.SINUSOIDD, -30f, -0.3f));
            scene.addObject(new MovingBox(-20f, 3f, 0.6f, 0.1f, 10f, 0f, MovingBox.MovementType.SINUSOIDX, -30f, 0.1f));
            scene.addObject(new Box(-22f, 3f, 1f, 1f));
            scene.addObject(new Spring(-22f, 4.2f, 0.5f, 0.2f));
            scene.addObject(new Spring(12f, 21.2f, 0.5f, 0.2f));
            scene.addEnemy(new Mouse(5f, -2.7f, 0.5f, 0.3f));
            scene.addMenuBox(new MenuBox(0, 0, 2, 1, MenuBox.Behavior.CLOSE, ""), 0);
            scene.addMenuBox(new MenuBox(0, 0, 2, 1, MenuBox.Behavior.LOAD, "testLevel"), 0);
            scene.addMenuBox(new MenuBox(0, 0, 2, 1, MenuBox.Behavior.POP, ""), 0);
            scene.addSprite("cursor", new Sprite("Cursor-PLACEHOLDER.png"));
            float[] positions = new float[]{
                -60f, 4f, 0f,
                -60f, -4f, 0f,
                15f, -4f, 0f,
                15f, 4f, 0f,};
            float[] texture = new float[]{
                0f, 1f, 0f,
                0f, 0f, 0f,
                1f, 0f, 0f,
                1f, 1f, 0f,};
            int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,};
            Mesh mesh = new Mesh(positions, texture, indices);
            scene.addSprite("background", new Sprite("Background-PLACEHOLDER.png"));
            scene.getSprites().get("background").addMesh("background", mesh);
            scene.addSprite("player", new Sprite("Kila-PLACEHOLDER.png"));
            return scene;
        } else if (filename.equals("topMenu")) {
            Scene scene = new Scene(window.getWidth(), window.getHeight(), false);
            scene.addMenuBox(new MenuBox(3, 2.5f, 2, 1, MenuBox.Behavior.PUSH, "levelSelect"), 0);
            scene.addMenuBox(new MenuBox(3, 0f, 2, 1, MenuBox.Behavior.PUSH, "optionsMenu"), 0);
            scene.addMenuBox(new MenuBox(3, -2.5f, 2, 1, MenuBox.Behavior.POP, ""), 0);
            float[] positions = new float[]{
                -60f, 4f, 0f,
                -60f, -4f, 0f,
                15f, -4f, 0f,
                15f, 4f, 0f,};
            float[] texture = new float[]{
                0f, 1f, 0f,
                0f, 0f, 0f,
                1f, 0f, 0f,
                1f, 1f, 0f,};
            int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,};
            Mesh mesh = new Mesh(positions, texture, indices);
            scene.addSprite("background", new Sprite("Background-PLACEHOLDER.png"));
            scene.getSprites().get("background").addMesh("background", mesh);
            scene.addSprite("cursor", new Sprite("Cursor-PLACEHOLDER.png"));
            return scene;
        } else if (filename.equals("optionsMenu")) {
            Scene scene = new Scene(window.getWidth(), window.getHeight(), false);
            scene.addMenuBox(new MenuBox(4, 2.5f, 2, 1, MenuBox.Behavior.PUSH, "controlMenu"), 0);
            scene.addMenuBox(new MenuBox(4, -2.5f, 2, 1, MenuBox.Behavior.POP, ""), 0);
            float[] positions = new float[]{
                -60f, 4f, 0f,
                -60f, -4f, 0f,
                15f, -4f, 0f,
                15f, 4f, 0f,};
            float[] texture = new float[]{
                0f, 1f, 0f,
                0f, 0f, 0f,
                1f, 0f, 0f,
                1f, 1f, 0f,};
            int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,};
            Mesh mesh = new Mesh(positions, texture, indices);
            scene.addSprite("background", new Sprite("Background-PLACEHOLDER.png"));
            scene.getSprites().get("background").addMesh("background", mesh);
            scene.addSprite("cursor", new Sprite("Cursor-PLACEHOLDER.png"));
            return scene;
        } else if (filename.equals("levelSelect")) {
            Scene scene = new Scene(window.getWidth(), window.getHeight(), false);
            scene.addMenuBox(new MenuBox(0, 2.5f, 1, 1, MenuBox.Behavior.PUSH, "testLevel"), 0);
            scene.addMenuBox(new MenuBox(0, -2.5f, 2, 1, MenuBox.Behavior.POP, ""), 0);
            float[] positions = new float[]{
                -60f, 4f, 0f,
                -60f, -4f, 0f,
                15f, -4f, 0f,
                15f, 4f, 0f,};
            float[] texture = new float[]{
                0f, 1f, 0f,
                0f, 0f, 0f,
                1f, 0f, 0f,
                1f, 1f, 0f,};
            int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,};
            Mesh mesh = new Mesh(positions, texture, indices);
            scene.addSprite("background", new Sprite("Background-PLACEHOLDER.png"));
            scene.getSprites().get("background").addMesh("background", mesh);
            scene.addSprite("cursor", new Sprite("Cursor-PLACEHOLDER.png"));
            return scene;
        } else if (filename.equals("controlMenu")) {
            Scene scene = new Scene(window.getWidth(), window.getHeight(), false);
            scene.addMenuBox(new MenuBox(-2, 3f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "up"), 0);
            scene.addMenuBox(new MenuBox(-2, 1.5f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "down"), 0);
            scene.addMenuBox(new MenuBox(2, 3f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "left"), 1);
            scene.addMenuBox(new MenuBox(2, 1.5f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "right"), 1);
            scene.addMenuBox(new MenuBox(-2, 00f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "jump"), 0);
            scene.addMenuBox(new MenuBox(-2, -1.5f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "flow"), 0);
            scene.addMenuBox(new MenuBox(2, 0f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "attack"), 1);
            scene.addMenuBox(new MenuBox(2, -1.5f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "pause"), 1);
            scene.addMenuBox(new MenuBox(-2, -3f, 0.75f, 0.5f, MenuBox.Behavior.UPDATE, "reset"), 0);
            scene.addMenuBox(new MenuBox(2, -3f, 0.75f, 0.5f, MenuBox.Behavior.POP, ""), 1);
            float[] positions = new float[]{
                -60f, 4f, 0f,
                -60f, -4f, 0f,
                15f, -4f, 0f,
                15f, 4f, 0f,};
            float[] texture = new float[]{
                0f, 1f, 0f,
                0f, 0f, 0f,
                1f, 0f, 0f,
                1f, 1f, 0f,};
            int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,};
            Mesh mesh = new Mesh(positions, texture, indices);
            scene.addSprite("background", new Sprite("Background-PLACEHOLDER.png"));
            scene.getSprites().get("background").addMesh("background", mesh);
            scene.addSprite("cursor", new Sprite("Cursor-PLACEHOLDER.png"));
            return scene;
        }
        return null;
    }

} // SceneBuilder
