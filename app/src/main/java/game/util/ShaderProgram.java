package game.util;

import org.lwjgl.opengl.GL46;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ShaderProgram {

    private final int program;
    
    public ShaderProgram(String vertShader, String fragShader) {
        program = GL46.glCreateProgram();
        // creating, compiling, and attaching the vertex/fragment shaders to the program
        int id = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);
        GL46.glShaderSource(id, shader(vertShader));
        GL46.glCompileShader(id);
        GL46.glAttachShader(program, id);
        GL46.glDeleteShader(id);
        id = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);
        GL46.glShaderSource(id, shader(fragShader));
        GL46.glCompileShader(id);
        GL46.glAttachShader(program, id);
        GL46.glDeleteShader(id);
        GL46.glLinkProgram(program);
    }   
    
    public void bind() {
        GL46.glUseProgram(program);
    } // bind
    
    public void unbind() {
        GL46.glUseProgram(0);
    } // unbind
    
    public void cleanup() {
        unbind();
        GL46.glDeleteProgram(program);
    }
    
    public int getProgram() {
        return program;
    }   
    
    private static String shader(String name) {
        // shaders stored in one place, so path start is always the same
        String path = "src/main/java/game/shaders/";
        path += name;
        try {
        BufferedReader br = new BufferedReader(new FileReader(path));      
	String read;
        String src = "";
	while ((read = br.readLine()) != null) {
            src += read;
            src += "\n";
	}
        final String end = src;
        return end;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read shader");
        }
    }
    
} // ShaderBuilder
