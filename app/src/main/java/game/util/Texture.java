package game.util;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public class Texture {

    private int texture;
    private String textureName;

    public Texture(String textureName) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // beginning of path is always same and can start with same handle
            this.textureName = "resources/" + textureName;
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // loads and generates texture
            ByteBuffer buf = STBImage.stbi_load(this.textureName, w, h, channels, 4);
            if (buf == null) {
                throw new RuntimeException("Image file not loaded");
            }

            int width = w.get();
            int height = h.get();

            generateTexture(width, height, buf);

            STBImage.stbi_image_free(buf);
        }
    }

    public void bind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, texture);
    }

    public void cleanup() {
        GL46.glDeleteTextures(texture);
    }

    private void generateTexture(int width, int height, ByteBuffer buf) {
        texture = GL46.glGenTextures();

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, texture);
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0,
                GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buf);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
    }

    public String getTexturePath() {
        return textureName;
    }
}