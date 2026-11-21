package game.util;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.util.List;
import java.util.ArrayList;

public class Mesh {

    private int numVertices;
    private int vao;
    private List<Integer> vboList;

    public Mesh(float[] positions, float[] texts, int[] indices) {
        this.numVertices = indices.length;
        vboList = new ArrayList<>();

        vao = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(vao);

        // Positions VBO
        int vbo = GL46.glGenBuffers();
        vboList.add(vbo);
        FloatBuffer positionsBuffer = MemoryUtil.memCallocFloat(positions.length);
        positionsBuffer.put(0, positions);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, positionsBuffer, GL46.GL_STATIC_DRAW);
        GL46.glEnableVertexAttribArray(0);
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

        // Texture coordinates VBO
        vbo = GL46.glGenBuffers();
        vboList.add(vbo);
        FloatBuffer textsBuffer = MemoryUtil.memCallocFloat(texts.length);
        textsBuffer.put(0, texts);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textsBuffer, GL46.GL_STATIC_DRAW);
        GL46.glEnableVertexAttribArray(1);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);

        // Index VBO
        vbo = GL46.glGenBuffers();
        vboList.add(vbo);
        IntBuffer indicesBuffer = MemoryUtil.memCallocInt(indices.length);
        indicesBuffer.put(0, indices);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vbo);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
        
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);
        
        MemoryUtil.memFree(positionsBuffer);
        MemoryUtil.memFree(textsBuffer);
        MemoryUtil.memFree(indicesBuffer);
    }

    public void cleanup() {
        vboList.forEach(GL46::glDeleteBuffers);
        GL46.glDeleteVertexArrays(vao);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVao() {
        return vao;
    }
}
