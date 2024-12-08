import parameters.Parameters;
import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class AdiposeTissue extends PApplet {

    private Bump[] bumps;

    public static void main(String[] args) {
        PApplet.main(AdiposeTissue.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        colorMode(HSB, 1);

        bumps = new Bump[NUMBER_OF_BUMPS_WITH_FORCE_1 + NUMBER_OF_BUMPS_WITH_FORCE_2];
        for (int i = 0; i < NUMBER_OF_BUMPS_WITH_FORCE_1; i++) {
            float radius = (pow(random(1), 2)) * (FORCE_1_RADIUS_FACTOR * min(width, height));
            float angle = random(TWO_PI);
            bumps[i] = new Bump(PVector.fromAngle(angle).mult(radius).add(width / 2f, height / 2f),
                    random(FORCE_1_MINIMUM_VALUE, FORCE_1_MAXIMUM_VALUE));
        }
        for (int i = 0; i < NUMBER_OF_BUMPS_WITH_FORCE_2; i++) {
            float radius = FORCE_2_RADIUS_FACTOR * min(width, height);
            float angle = random(TWO_PI);
            bumps[NUMBER_OF_BUMPS_WITH_FORCE_1 + i] = new Bump(
                    PVector.fromAngle(angle).mult(radius).add(width / 2f, height / 2f),
                    random(FORCE_2_MINIMUM_VALUE, FORCE_2_MAXIMUM_VALUE));
        }
    }

    @Override
    public void draw() {
        float x = MARGIN + (frameCount) * STEP;
        if (x > WIDTH - MARGIN) {
            noLoop();
            saveSketch(this);
        }
        for (float y = MARGIN; y <= HEIGHT - MARGIN; y += STEP) {
            if (sq(x - width / 2f) + sq(y - height / 2f) < sq(Parameters.RADIUS)) {
                PVector p = new PVector(x + randomGaussian(), y + randomGaussian());

                for (int i = 0; i < MAX_LENGTH && p.x > -1 && p.x < width && p.y > - 1 && p.y < height; i++) {
                    float brightness = 0;
                    PVector v = PVector.fromAngle(PVector.sub(new PVector(width / 2f, height / 2f), p).heading() + PI / 2f)
                            .mult(SPEED_FACTOR * sqrt(sq(width / 2f - p.x) + sq(height / 2f - p.y)));
                    for (Bump b : bumps) {
                        PVector f = PVector.sub(p, b.getPosition());
                        float d = 1 + sq(f.x) + sq(f.y);
                        brightness = max(brightness, b.getForce() * BRIGHTNESS_FACTOR / sqrt(d));
                        f.setMag(b.getForce() / d);
                        v.add(f);
                    }
                    float hue = HUE_FACTOR * noise(p.y * NOISE_SCALE, p.x * NOISE_SCALE) + BASE_HUE;
                    hue = (hue + 1) % 1;
                    stroke(hue, SATURATION, brightness, Parameters.ALPHA);
                    point(p.x, p.y);
                    p.add(v.normalize());
                }
            }
        }
    }
}
