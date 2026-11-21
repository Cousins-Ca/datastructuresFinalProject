package game.util;

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
        uniforms.createUniform("projection");
        uniforms.createUniform("txtSampler");
    } // createUniforms
    
    public void cleanup() {
        program.cleanup();
    } // cleanup

    public void render(Window window, SceneBuilder scene) {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
        uniforms.setUniform("projection", scene.getProjection().getProjection());
        uniforms.setUniform("txtSampler", 0);
        renderScene(scene);
    } // render
    
    private void renderScene(SceneBuilder scene) {
        program.bind();
        // draws each sprite, taking their texture and binding it to the mesh for the sprite
        scene.getSprites().values().forEach(sprite -> {
            GL46.glActiveTexture(GL46.GL_TEXTURE0);
            sprite.getTexture().bind();
            sprite.getMeshes().values().forEach(mesh -> {
                GL46.glBindVertexArray(mesh.getVao());
                GL46.glDrawElements(GL46.GL_TRIANGLES, mesh.getNumVertices(), GL46.GL_UNSIGNED_INT, 0);
            });
        });
    } // renderScene
    
} // RenderHandler                                                          