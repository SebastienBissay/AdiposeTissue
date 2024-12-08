import processing.core.PVector;

public class Bump {
    private final PVector position;
    private final float force;

    Bump(PVector p, float f) {
        position = p;
        force = f;
    }

    public PVector getPosition() {
        return position;
    }

    public float getForce() {
        return force;
    }
}
