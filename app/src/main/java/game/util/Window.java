package game.util;

import org.lwjgl.glfw.GLFW;
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
    
    private long window;
    private int height;
    private int width;
    private float prevTime;
    private float currTime = 0f;

    public Window(String title) {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        // sets up window and window hints
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL46.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL46.GL_TRUE);
        width = 800;
        height = 600;
        
        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

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
    
    public float getDelta() {
        prevTime = currTime;
        currTime = (float) GLFW.glfwGetTime();
        float delta = currTime - prevTime;
        return delta;
    }
    
     public boolean keyIsPressed(int keyCode) {
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    } // isKeyPressed

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
