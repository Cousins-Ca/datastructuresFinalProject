package game.util;

import game.interfaces.*;
import game.element.MenuBox;

import java.util.List;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

public class RenderHandler {

    private ShaderProgram program;
    private Uniforms uniforms;

    public RenderHandler() {
        GL.createCapabilities();
        String vertexShader = "spriteVertex.vert";
        String fragmentShader = "spriteFragment.frag";
        program = new ShaderProgram(vertexShader, fragmentShader);
        createUniforms();
    } // RenderHandler

    private void createUniforms() {
        uniforms = new Uniforms(program.getProgram());
        uniforms.createUniform("ortho");
        uniforms.createUniform("txtSampler");
        uniforms.createUniform("view");
    } // createUniforms

    public void cleanup() {
        program.cleanup();
    } // cleanup

    public void render(Window window, Scene scene, GameHandler game) {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
        uniforms.setUniform("ortho", scene.getOrtho().getOrtho());
        uniforms.setUniform("txtSampler", 0);
        uniforms.setUniform("view", scene.getCamera().getViewMatrix());
        renderScene(scene, game);
    } // render

    private void renderScene(Scene scene, GameHandler game) {
        program.bind();
        // draws each sprite, taking their texture and binding it to the mesh for the sprite
        renderSprite(scene.getSprites().get("background"));
        for (GameObject o : scene.getObjects()) {
            renderSprite(o.getSprite());
        }
        for (Enemy e : scene.getEnemies()) {
            renderSprite(e.getSprite());
        }
        if (scene.isLevel()) {
            renderSprite(scene.getSprites().get("player"));

        }
        if (game.getState().equals(GameHandler.GameState.MENU)) {
            for (List<MenuBox> lmb : scene.getMenuBoxes()) {
                for (MenuBox mb : lmb) {
                    renderSprite(mb.getSprite());
                }
            }
            renderSprite(scene.getSprites().get("cursor"));

        }
    } // renderScene

    private void renderSprite(Sprite sprite) {
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        sprite.getTexture().bind();
        sprite.getMeshes().values().forEach(mesh -> {
            GL46.glBindVertexArray(mesh.getVao());
            GL46.glDrawElements(GL46.GL_TRIANGLES, mesh.getNumVertices(), GL46.GL_UNSIGNED_INT, 0);
        });
    } // renderSprites

} // RenderHandler                                                          
