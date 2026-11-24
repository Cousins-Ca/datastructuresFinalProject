package game.element;

public class MovingBox extends Box {
    
    public enum MovementType {
        SINUSOIDX,
        SINUSOIDY,
        SINUSOIDU,
        SINUSOIDD,
        CIRCULAR;
    }
    
    private float mov;
    private float movX;
    private float movY;
    private MovementType movType;
    private float center;
    private float bound;
    private float counter;

    public MovingBox(float x, float y, float sizeX, float sizeY, float bound, float startAt,
            MovementType movType, float center, float mov) {
        super(x, y, sizeX, sizeY);
        this.mov = mov;
        //this.movY = movY;
        this.movX = 0;
        this.movY = 0;
        this.movType = movType;
        this.center = center;
        this.bound = bound;
        counter = startAt;
    } // MovingBox
    
    @Override
    public void update() {
        counter++;
        switch (movType) {
            case SINUSOIDU -> {
                movX = (float) (Math.sin(mov * counter / bound) / bound);
                setX(getX() + movX);
                movY = (float) (Math.sin(mov * counter / bound) / bound);
                setY(getY() + movY);
            }
            case SINUSOIDD -> {
                movX = (float) (Math.sin(mov * counter / bound) / bound);
                setX(getX() + movX);
                movY = -1 * (float) (Math.sin(mov * counter / bound) / bound);
                setY(getY() + movY);
            }
            case SINUSOIDX -> {
                movX = (float) (Math.sin(mov * counter / bound) / bound);
                setX(getX() + movX);
            }
            case SINUSOIDY -> {
                movY = (float) (Math.sin(mov * counter / bound) / bound);
                setY(getY() + movY);
            }
            case CIRCULAR -> {
                
            }
        }
        createSpriteMesh();
    } // update
    
    @Override
    public float getForceX() {
        return movX;
    } // getForceX
    
    @Override
    public float getForceY() {
        return movY;
    } // getForceY
    
} // MovingBox