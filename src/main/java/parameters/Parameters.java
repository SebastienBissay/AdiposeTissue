package parameters;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static processing.core.PApplet.min;

public final class Parameters {
    public static final long SEED = 11;
    public static final int WIDTH = 2000;
    public static final int HEIGHT = 2000;
    public static final int MARGIN = 400;
    public static final int NUMBER_OF_BUMPS_WITH_FORCE_1 = 1000;
    public static final float FORCE_1_RADIUS_FACTOR = .3f;
    public static final float FORCE_1_MINIMUM_VALUE = 5;
    public static final float FORCE_1_MAXIMUM_VALUE = 25;
    public static final int NUMBER_OF_BUMPS_WITH_FORCE_2 = 200;
    public static final float FORCE_2_RADIUS_FACTOR = .32f;
    public static final float FORCE_2_MINIMUM_VALUE = 5;
    public static final float FORCE_2_MAXIMUM_VALUE = 15;
    public static final float STEP = 2.5f;
    public static final float RADIUS = .3f * min(WIDTH, HEIGHT);
    public static final float MAX_LENGTH = 200f;
    public static final float SPEED_FACTOR = .0001f;
    public static final float HUE_FACTOR = .3f;
    public static final float BASE_HUE = .4f;
    public static final float SATURATION = .75f;
    public static final float BRIGHTNESS_FACTOR = .2f;
    public static final float ALPHA = .25f;
    public static final float NOISE_SCALE = .01f;
    public static final Color BACKGROUND_COLOR = new Color(255);

    /**
     * Helper method to extract the constants in order to save them to a json file
     *
     * @return a Map of the constants (name -> value)
     */
    public static Map<String, ?> toJsonMap() throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        Field[] declaredFields = Parameters.class.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(Parameters.class));
        }

        return Collections.singletonMap(Parameters.class.getSimpleName(), map);
    }

    public record Color(float red, float green, float blue, float alpha) {
        public Color(float red, float green, float blue) {
            this(red, green, blue, 255);
        }

        public Color(float grayscale, float alpha) {
            this(grayscale, grayscale, grayscale, alpha);
        }

        public Color(float grayscale) {
            this(grayscale, 255);
        }

        public Color(String hexCode) {
            this(decode(hexCode));
        }

        public Color(Color color) {
            this(color.red, color.green, color.blue, color.alpha);
        }

        public static Color decode(String hexCode) {
            return switch (hexCode.length()) {
                case 2 -> new Color(Integer.valueOf(hexCode, 16));
                case 4 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16));
                case 6 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16),
                        Integer.valueOf(hexCode.substring(4, 6), 16));
                case 8 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16),
                        Integer.valueOf(hexCode.substring(4, 6), 16),
                        Integer.valueOf(hexCode.substring(6, 8), 16));
                default -> throw new IllegalArgumentException();
            };
        }
    }
}
