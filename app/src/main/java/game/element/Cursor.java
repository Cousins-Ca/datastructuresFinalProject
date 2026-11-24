package game.element;

public class Cursor {
    
    private int selectedX;
    private int selectedY;
    
    public Cursor() {
        selectedX = 0;
        selectedY = 0;
    } // Cursor
    
    public int intendedX (int i) {
        if (i > 0) return selectedX + 1;
        else return selectedX - 1;
    } // moveX
    
    public int intendedY (int i) {
        if (i > 0) return selectedY + 1;
        else return selectedY - 1;
    } // moveY
    
    public int getX() {
        return selectedX;
    } // getX
    
    public int getY() {
        return selectedY;
    } // getY

    public void setX(int i) {
        selectedX = i;
    } // setX
    
    public void setY(int i) {
        selectedY = i;
    } // setY
    
} // Cursor