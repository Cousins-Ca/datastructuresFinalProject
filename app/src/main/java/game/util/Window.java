package game.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL46;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private GLFWFramebufferSizeCallback sizeCallback = new GLFWFramebufferSizeCallback() {
        // callback updates viewport every time the window is resized   
        @Override
        public void invoke(long window, int width, int height) {
            GL46.glViewport(0, 0, width, height);
        }
    };
    
    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        // callback gives key pressed for InputHandler's polling
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            lastKeyPressed = key;
        }
    };
    
    private long window;
    private int height;
    private int width;
    private int lastKeyPressed;
    
    public Window(String title) {
        lastKeyPressed = 0;
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        // sets up window and window hints
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL46.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GL46.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL46.GL_TRUE);
        width = 800;
        height = 600;
        
        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFW.glfwSetKeyCallback(window, keyCallback);
        GLFW.glfwSetFramebufferSizeCallback(window, sizeCallback);
        GLFW.glfwMakeContextCurrent(window);
        // vsync active
        GLFW.glfwSwapInterval(1);
        
        GLFW.glfwShowWindow(window);
        
    } // Window
    
    public int getHeight() {
        return height;
    } // getHeight
    
    public int getWidth() {
        return width;
    } // getWidth
    
    public long getWindow() {
        return window;
    } // getWindow 
    
    public boolean keyIsPressed(int keyCode) {
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    } // isKeyPressed
     
    public int getKeyPressed() {
        return GLFW.glfwGetKey(window, 0);
    } // getKeyPressed
    
    public int getLastKeyPressed() {
        return lastKeyPressed;
    } // getLastKeyPressed

    public void setLastKeyPressed(int i) {
        lastKeyPressed = i;
    }
    
    public void pollEvents() {
        GLFW.glfwPollEvents();
    } // pollEvents
    
    public void update() {
        GLFW.glfwSwapBuffers(window);
    } // update

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    } // windowShouldClose
    
    public void cleanup() {
        glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    } // cleanup
    
} // Window
