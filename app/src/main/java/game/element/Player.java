package game.element;

public class Player {

    public enum State {
        GROUNDED,
        AIRBORNE,
        DIVE,
        BOUNCE,
        SLIDE,
        INACTIONABLE;
    }

    public enum Boost {
        JUMP,
        ENEMY,
        SPRING,
    }

    public enum Flow {
        BIT,
        BYTE,
        KILO;
    }

    private int health;
    private float x;
    private float y;
    private float hSpd;
    private float vSpd;
    private float lastGroundedX;
    private float lastGroundedY;
    private boolean godmode;
    private boolean antigravAvailable;
    private boolean doubleJump;
    private boolean flowMove;
    private boolean flowCharge;
    private State state;
    private Flow flowState;

    public Player(float x, float y, boolean gm) {
        health = 3;
        this.x = x;
        this.y = y;
        hSpd = 0;
        vSpd = 0;
        lastGroundedX = x;
        lastGroundedY = y;
        godmode = gm;
        antigravAvailable = true;
        doubleJump = true;
        flowMove = false;
        flowCharge = false;
        state = State.GROUNDED;
        flowState = Flow.BIT;
    }

    public int getHealth() {
        return health;
    } // getHealth

    public float getX() {
        return x;
    } // getX

    public float getY() {
        return y;
    } // getY

    public float getLastGroundedX() {
        return lastGroundedX;
    } // getLastGroundedX

    public float getLastGroundedY() {
        return lastGroundedY;
    } // getLastGroundedY

    public boolean getGodmode() {
        return godmode;
    } // getGodmode

    public Flow getFlow() {
        return flowState;
    } // getFlow

    public boolean attacking() {
        return state.equals(State.DIVE) || state.equals(State.SLIDE);
    } // attacking

    public State getState() {
        return state; // REMOVE
    }

    public void setFlow(boolean b) {
        flowMove = b;
    } // setFlow
    
    public void disableAntigrav() {
        if (state.equals(State.AIRBORNE)) {
            antigravAvailable = false;
        }   
    } // disableAntigrav
    
    public void setCharge(boolean b) {
        flowCharge = b;
    } // setCharge

    public void setState(State state) {
        this.state = state; 
    } // setState

    public void updateFlow(int count) {
        if (count >= 40) {
            flowState = Flow.KILO;
        } else if (count < 0 && !flowCharge) {
            flowState = Flow.BIT;
        } else {
            flowState = Flow.BYTE;
        }
    } // updateFlow
    
    public void printFlow() {
        System.out.println(flowState);
    }
    
    public float intendedXPos(boolean moving, boolean right, float force) {
        updateHSpd(moving, right, force);
        float intendedX = x + hSpd;
        return intendedX;
    } // intendedXPos

    public float intendedYPos(Boost boost, float force) {
        float totalForce = force;
        // switch-case based on type of vertical boost being applied to Kila
        switch (boost) {
            /**
             *
             * Slide - Boost of 1.5, State = Airborne Grounded - Boost of 1,
             * State = Airborne Airborne - Check doubleJump, Boost of 1,
             * doubleJump = false
             *
             */
            case (Boost.JUMP) -> {
                if (state.equals(State.SLIDE)) {
                    totalForce += 0.175f;
                }
                if (state.equals(State.GROUNDED)) {
                    totalForce += 0.125;
                }
                if (state.equals(State.AIRBORNE) && doubleJump) {
                    totalForce = 0.125f;
                    vSpd = 0;
                    doubleJump = false;
                }
            }
            case (Boost.ENEMY) -> {
                totalForce = 0.0625f;
                vSpd = 0;
                doubleJump = true;
                antigravAvailable = true;
            }
            case (Boost.SPRING) -> {
                totalForce = 0.25f;
                vSpd = 0;
                doubleJump = true;
                antigravAvailable = true;
            }
        }
        state = State.AIRBORNE;
        updateVSpd(totalForce);
        float intendedY = y + vSpd;
        return intendedY;
    } // intendedYPos

    public float intendedYPos(float force) {
        updateVSpd(force);
        float intendedY = y + vSpd;
        return intendedY;
    } // intendedYPos

    public void updatePosition(float x, float y, boolean hitWall, boolean hitCeiling, boolean hitFloor) {
        this.x = x;
        this.y = y;
        if ((state.equals(State.GROUNDED) || state.equals(State.SLIDE)) && !hitWall) {
            lastGroundedX = x;
            lastGroundedY = y;
        }
        if (hitWall) {
            this.x -= hSpd;
        }
        if (hitCeiling) {
            vSpd = 0;
        }
        if (hitFloor) {
            vSpd = 0;
            if (state.equals(State.DIVE)) {
                state = State.BOUNCE;
                updateFlow(0);
            } else if (!state.equals(State.SLIDE)) {
                state = State.GROUNDED;
            }
            antigravAvailable = true;
            doubleJump = true;
        }
    } // updatePosition
    
    // helpers
    private void updateHSpd(boolean moving, boolean right, float force) {
        float flowSpeed = 0;
        if (moving && !(flowCharge && state.equals(State.GROUNDED))) {
            flowSpeed = switch (flowState) {
                case (Flow.BIT) ->
                    0.04f;
                case (Flow.BYTE) ->
                    0.08f;
                case (Flow.KILO) ->
                    0.12f;
            };
        }
        flowSpeed = (right ? flowSpeed : -flowSpeed);
        hSpd = flowSpeed + force;
    } // updateHSpd

    private void updateVSpd(float force) {
        switch (state) {
            case (State.GROUNDED) ->
                vSpd = 0;
            case (State.SLIDE) ->
                vSpd = 0;
            case (State.AIRBORNE) -> {
                if (!antigravAvailable || !flowMove) {
                    vSpd -= 0.01;
                }
                if (vSpd < 0 && antigravAvailable && flowMove) {
                    vSpd = 0;
                }
                if (vSpd <= -0.125) {
                    vSpd = -0.125f;
                }
            }                                                                                               
            case (State.DIVE) ->
                vSpd = -0.2f;
            case (State.BOUNCE) -> {
                vSpd = 0.2f;
                state = State.AIRBORNE;
            }
        }
        vSpd += force;
    } // updateVelY
    
} // Player
