package game.util;

import java.util.Map;
import java.util.HashMap;

import game.element.Player;
import game.element.Cursor;

import org.lwjgl.glfw.GLFW;

public class InputHandler {

    private boolean[] inputs;
    private float x;
    private float y;
    private int cursorX;
    private int cursorY;
    private int newControl;
    private boolean select;
    private boolean cancel;
    private boolean openMenu;
    private boolean polling;
    private int[] counters;
    private boolean[] moved;
    private float[] prevXs;
    private Map<String, Integer> keybinds;

    public InputHandler(SaveManager sm) {
        inputs = new boolean[7];
        x = 0f;
        y = 0f;
        cursorX = 0;
        cursorY = 0;
        select = false;
        cancel = true;
        openMenu = false;
        polling = false;
        counters = new int[4];
        moved = new boolean[10];
        prevXs = new float[10];
        newControl = -1;
        keybinds = sm.getControls();
    } // InputHandler

    public void inputLevel(Window window, Player player) {
        moved[counters[2]] = false;
        if (window.keyIsPressed(keybinds.get("left"))) {
            // player moves left
            moved[counters[2]] = true;
            x = player.intendedXPos(true, false, 0);
        } else if (window.keyIsPressed(keybinds.get("right"))) {
            // player moves right
            moved[counters[2]] = true;
            x = player.intendedXPos(true, true, 0);
        } else {
            // x position update without left/right movement
            x = player.intendedXPos(false, false, 0);
        }
        if (window.keyIsPressed(keybinds.get("jump")) && !inputs[0] && !player.getState().equals(Player.State.DIVE)) {
            // player jumps if z wasnt held on the previous frame
            y = player.intendedYPos(Player.Boost.JUMP, 0);
            inputs[0] = true;
        } else {
            // y position update without vertical boost
            y = player.intendedYPos(0);
            if (!window.keyIsPressed(keybinds.get("jump"))) {
                // unpresses z key
                inputs[0] = false;
            }
        }
        if (window.keyIsPressed(keybinds.get("flow"))) {
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
        if (window.keyIsPressed(keybinds.get("attack")) && !inputs[1]) {
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
        } else if (!window.keyIsPressed(keybinds.get("attack"))) {
            // releases c key
            inputs[1] = false;
        }
        if (player.getState().equals(Player.State.DIVE)) {
            counters[0] = 0;
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
        if (window.keyIsPressed(keybinds.get("pause"))) {
            openMenu = true;
        } else {
            openMenu = false;
        }
        checkLeaveFlow(player, moved);
    } // inputLevel

    public void inputMenu(Window window, Cursor cursor, SaveManager sm) {
        if (polling) {
            if ((window.getLastKeyPressed() != keybinds.get("jump") && inputs[0]) || (!inputs[0] && window.getLastKeyPressed() != 0)) {
                newControl = window.getLastKeyPressed();
                polling = false;
                counters[3] = 6;
            } else if (!window.keyIsPressed(keybinds.get("jump"))) {
                inputs[0] = false;
                window.setLastKeyPressed(0);
            }
        } else if (counters[3] != 0) {
            counters[3]--;
        } else {
            if (window.keyIsPressed(keybinds.get("left")) && !inputs[2]) {
                cursorX = cursor.intendedX(-1);
                inputs[2] = true;
            } else if (window.keyIsPressed(keybinds.get("right")) && !inputs[2]) {
                cursorX = cursor.intendedX(1);
                inputs[2] = true;
            } else if (!window.keyIsPressed(keybinds.get("right")) && !window.keyIsPressed(keybinds.get("left"))) {
                cursorX = cursor.getX();
                inputs[2] = false;
            }
            if (window.keyIsPressed(keybinds.get("up")) && !inputs[3]) {
                cursorY = cursor.intendedY(-1);
                inputs[3] = true;
            } else if (window.keyIsPressed(keybinds.get("down")) && !inputs[3]) {
                cursorY = cursor.intendedY(1);
                inputs[3] = true;
            } else if (!window.keyIsPressed(keybinds.get("down")) && !window.keyIsPressed(keybinds.get("up"))) {
                cursorY = cursor.getY();
                inputs[3] = false;
            }
            if (window.keyIsPressed(keybinds.get("jump")) && !inputs[0]) {
                select = true;
                inputs[0] = true;
            } else if (!window.keyIsPressed(keybinds.get("jump"))) {
                select = false;
                inputs[0] = false;
            } else {
                select = false;
            }
            if (window.keyIsPressed(keybinds.get("flow")) && !inputs[5]) {
                cancel = true;
                inputs[5] = true;
            } else if (!window.keyIsPressed(keybinds.get("flow"))) {
                cancel = false;
                inputs[5] = false;
            } else {
                cancel = false;
            }
        }
    } // inputMenu

    public float getPlayerX() {
        return x;
    } // getX

    public float getPlayerY() {
        return y;
    } // getY

    public int getCursorX() {
        return cursorX;
    } // getCursorX;

    public int getCursorY() {
        return cursorY;
    } // getCursorY

    public void setHeldCursorPos(int x, int y) {
        cursorX = x;
        cursorY = y;
    } // setHeldCursorPos

    public void setPolling(boolean b) {
        polling = b;
    }

    public int getNewControl() {
        int save = newControl;
        newControl = -1;
        return save;
    }

    public void updateControl(String target, int control, SaveManager sm) {
        if (!keybinds.containsValue(control)) {
            keybinds.put(target, control);
            sm.updateControls(target, control);
        }
    }
    
    public void resetControls(SaveManager sm) {
        keybinds = sm.getControls();
    }

    public boolean getSelect() {
        boolean save = select;
        select = false;
        return save;
    } // getSelect

    public boolean getCancel() {
        return cancel;
    } // getCancel

    public boolean getOpenMenu() {
        boolean save = openMenu;
        openMenu = false;
        return save;
    } // getOpenMenu

    private void checkLeaveFlow(Player player, boolean[] moved) {
        // if the player has been in the same x position, they enter bit flow
        // allows leniency in flow not being exited instantly on stopping
        int check = 0;
        for (int i = 0; i < prevXs.length; i++) {
            if (x == prevXs[i] || !moved[i]) {
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
