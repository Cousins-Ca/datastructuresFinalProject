package game.util;

import java.util.Map;
import java.util.HashMap;

import org.joml.Matrix4f;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL46;

public class Uniforms {
 
    private int program;
    private Map<String, Integer> uniforms;
    
    public Uniforms(int program) {
        this.program = program;
        uniforms = new HashMap<>();
    } // Uniforms
    
    public void createUniform(String name) {
        int uniformLocation = GL46.glGetUniformLocation(program, name);
        if (uniformLocation < 0) {
            throw new RuntimeException("Could not find uniform");
        }
        uniforms.put(name, uniformLocation);
    } // createUniform
    
    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL46.glUniformMatrix4fv(getUniformLocation(name), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String name, int value) {
        GL46.glUniform1i(getUniformLocation(name), value);
    }

    private int getUniformLocation(String uniformName) {
        Integer location = uniforms.get(uniformName);
        if (location == null) {
            throw new RuntimeException("Could not find uniform [" + uniformName + "]");
        }
        return location;
    }
    
} // Uniforms