package game.element;

import game.interfaces.*;

public class Player {

    public enum State {
        GROUNDED,
        AIRBORNE,
        DIVE,
        BOUNCE,
        SLIDE;
    }

    public enum Boost {
        JUMP,
        ENEMY,
        SPRING,
        HIT;
    }

    public enum Flow {
        BIT,
        BYTE,
        KILO;
    }

    private int health;
    private float x;
    private float y;
    private float sizeX;
    private float sizeY;
    private float hSpd;
    private float vSpd;
    private float lastGroundedX;
    private float lastGroundedY;
    private boolean godmode;
    private boolean antigravAvailable;
    private boolean doubleJump;
    private boolean midAirJump;
    private boolean flowMove;
    private boolean flowCharge;
    private State state;
    private Flow flowState;
    private int invCounter;

    public Player(float x, float y) {
        health = 4;
        this.x = x;
        this.y = y;
        sizeX = 0.25f;
        sizeY = 1f;
        hSpd = 0;
        vSpd = 0;
        lastGroundedX = x;
        lastGroundedY = y;
        godmode = false;
        antigravAvailable = true;
        doubleJump = true;
        midAirJump = false;
        flowMove = false;
        flowCharge = false;
        state = State.GROUNDED;
        flowState = Flow.BIT;
        invCounter = 0;
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

    public float getSizeX() {
        return sizeX;
    } // getSizeX
    
    public float getSizeY() {
        return sizeY;
    }
    
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
        return state.equals(State.DIVE) || state.equals(State.SLIDE) || flowState.equals(Flow.KILO)
                || flowMove || flowCharge;
    } // attacking

    public State getState() {
        return state; // REMOVE
    }

    public void setFlow(boolean b) {
        flowMove = b;
    } // setFlow
    
    public void activateMidAirJump() {
        midAirJump = true;
    } // activateMidAirJumo
    
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
                if (state.equals(State.AIRBORNE) && midAirJump) {
                    totalForce = 0.125f;
                    vSpd = 0;
                    midAirJump = false;
                } else if (state.equals(State.AIRBORNE) && doubleJump) {
                    totalForce = 0.125f;
                    vSpd = 0;
                    doubleJump = false;
                }
            }
            case (Boost.ENEMY) -> {
                totalForce = 0.1f;
                if (state.equals(State.DIVE)) {
                    totalForce = 0.2f;
                    state = State.BOUNCE;
                }
                vSpd = 0;
                doubleJump = true;
                antigravAvailable = true;
            }
            case (Boost.SPRING) -> {
                totalForce = 0.2f;
                vSpd = 0;
                doubleJump = true;
                antigravAvailable = true;
            }
            case (Boost.HIT) -> {
                totalForce = 0.08f;
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
    
    public boolean isInvincible() {
        return invCounter != 0;
    } // isInvincible
    
    public void hit() {
        health--;
        invCounter = 90;
    } // getHit
    
    public void updatePosition(float x, float y, boolean hitWall, boolean hitCeiling, boolean hitFloor) {
        this.x = x;
        this.y = y;
        if (invCounter > 0) {
            invCounter--;
        }
        if ((state.equals(State.GROUNDED) || state.equals(State.SLIDE)) && !hitWall) {
            lastGroundedX = x;
            lastGroundedY = y;
        }
        if (hitWall) {
            hSpd = 0;
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
    
    public float getVSpd() {
        return vSpd;
    } // getvSpd
    
    public float getHSpd() {
        return hSpd;
    } // getHSpd
        
    public void setHSpd(float f) {
        hSpd = f;
    } // setHSpd
    
    public void setVSpd(float f) {
        vSpd = f;
    } // setVSpd

    public boolean checkCollision(GameObject o1, float newX, float newY) {
        boolean collidingX = false;
        boolean collidingY = false;
        if (o1.getX() + o1.getSizeX() > newX - getSizeX() &&
        o1.getX() - o1.getSizeX() < newX + getSizeX()) collidingX = true;
        if (o1.getY() + o1.getSizeY() > newY &&
        o1.getY() - o1.getSizeY() < newY + getSizeY()) collidingY = true;
        return collidingX && collidingY;
    } // checkCollision
    
    public boolean checkCollision(Enemy e1, float newX, float newY) {
        boolean collidingX = false;
        boolean collidingY = false;
        if (e1.getX() + e1.getSizeX() > newX - getSizeX() &&
        e1.getX() - e1.getSizeX() < newX + getSizeX()) collidingX = true;
        if (e1.getY() + e1.getSizeY() > newY &&
        e1.getY() - e1.getSizeY() < newY + getSizeY()) collidingY = true;
        return collidingX && collidingY;
    } // checkCollision
    
    public boolean outOfBounds(float[] bounds) {
        boolean oob = false;
        if (x < bounds[0]) oob = true;
        if (x > bounds[1]) oob = true;
        if (y < bounds[2]) oob = true;
        if (y > bounds[3]) oob = true;
        return oob;
    } // outOfBounds
    
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
                    0.16f;
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
                if (vSpd <= -0.225) {
                    vSpd = -0.225f;
                }
            }                                                                                               
            case (State.DIVE) ->
                vSpd = -0.4f;
            case (State.BOUNCE) -> {
                vSpd += 0.2f;
                state = State.AIRBORNE;
            }
        }
        vSpd += force;
    } // updateVelY
    
} // Player
