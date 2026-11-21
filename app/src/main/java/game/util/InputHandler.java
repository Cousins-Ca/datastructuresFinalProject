package game.util;

import game.element.Player;

import org.lwjgl.glfw.GLFW;

public class InputHandler {
    
    private boolean[] inputs;
    private float x;
    private float y;
    private int[] counters;
    private float[] prevXs;
    
    public InputHandler() {
        inputs = new boolean[2];
        x = 0f;
        y = 0f;
        counters = new int[3];
        prevXs = new float[10];
    } // InputHandler
    
    public void input(Window window, Player player) {
        if (window.keyIsPressed(GLFW.GLFW_KEY_LEFT)) {
            // player moves left
            x = player.intendedXPos(true, false, 0);
        } else if (window.keyIsPressed(GLFW.GLFW_KEY_RIGHT)) {
            // player moves right
            x = player.intendedXPos(true, true, 0);
        } else {
            // x position update without left/right movement
            x = player.intendedXPos(false, false, 0);
        }
        if (window.keyIsPressed(GLFW.GLFW_KEY_Z) && !inputs[0]) {
            // player jumps if z wasnt held on the previous frame
            y = player.intendedYPos(Player.Boost.JUMP, 0);
            inputs[0] = true;
        } else {
            // y position update without vertical boost
            y = player.intendedYPos(0); 
            if (!window.keyIsPressed(GLFW.GLFW_KEY_Z)) {
                // unpresses z key
                inputs[0] = false;
            }   
        }
        if (window.keyIsPressed(GLFW.GLFW_KEY_X)) {
            // counter[0] tracks how long x (flow button) is held
            // if <= 20, flow move is active and player is in antigrav
            // if > 20, flow move is inactive and player is charging
            counters[0]++;
            if (counters[0] <= 20) {
                player.setFlow(true);
                player.updateFlow(0);
            } else {
                player.setFlow(false);
                player.disableAntigrav();
                player.setCharge(true);
            }       
        } else {
            player.setFlow(false);
            player.setCharge(false);
            // if counters[0] >= 40, player goes into kilo flow
            // else, player goes into byte flow
            if (counters[0] != 0) {
                player.disableAntigrav();
                player.updateFlow(counters[0]);
                prevXs[counters[2]] -= x;
            }
            counters[0] = 0;
        }
        if (window.keyIsPressed(GLFW.GLFW_KEY_C) && !inputs[1]) {
            // player slides / dives if c wasnt held on previous frame
            // dives if airborne, slides if grounded
            inputs[1] = true;
            if (player.getState().equals(Player.State.AIRBORNE)) {
                player.setState(Player.State.DIVE);
            } else if (player.getState().equals(Player.State.GROUNDED)) {
                // slide sets byte flow
                player.setState(Player.State.SLIDE);
                player.updateFlow(0);
            }
        } else if (!window.keyIsPressed(GLFW.GLFW_KEY_C)) {
            // releases c key
            inputs[1] = false;
        }
        if (player.getState().equals(Player.State.SLIDE)) {
            // slide counter, once counters[1] reaches 20 slide stops
            counters[1]++;
            counters[0] = 0;
            if (counters[1] == 20) {
                counters[1] = 0;
                player.setState(Player.State.GROUNDED);
            }
        }
        checkLeaveFlow(player);
    } // input
    
    public float getX() {
        return x;
    } // getX
    
    public float getY() {
        return y;
    } // getY
    
    private void checkLeaveFlow(Player player) {
        // if the player has been in the same x position, they enter bit flow
        // allows leniency in flow not being exited instantly on stopping
        int check = 0;
        for (int i = 0; i < prevXs.length; i++) {
            if (x == prevXs[i]) {
                check++;
            }
        }
        if (check == 10) {
            player.updateFlow(-1);
        }
        prevXs[counters[2]] = x;
        counters[2]++;
        if (counters[2] == 10) {
            counters[2] = 0;
        }
    } // checkLeaveFlow
    
} // InputHandler