package rs.helpkit.pref;

import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class RSPreferences {

    private static final Properties PROPS = new Properties();

    private interface Keys {
        String DEFAULT_WORLD = "default_world";
        String DEFAULT_WIDTH = "default_width";
        String DEFAULT_HEIGHT = "default_height";
        String DEFAULT_X = "default_x";
        String DEFAULT_Y = "default_y";
    }

    static {
        File propertyFile = new File(HKConfig.HOME, "prefs.txt");
        if (propertyFile.exists()) {
            try (InputStream in = new FileInputStream(propertyFile)) {
                PROPS.load(in);
            } catch (IOException e) {
                System.err.println("Failed to load prefs.txt");
            }
        } else {
        }
        setIfInvalid(Keys.DEFAULT_WORLD, "70");
        setIfInvalid(Keys.DEFAULT_WIDTH, "765");
        setIfInvalid(Keys.DEFAULT_HEIGHT, "503");
        setIfInvalid(Keys.DEFAULT_X, "-1");
        setIfInvalid(Keys.DEFAULT_Y, "-1");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (OutputStream out = new FileOutputStream(propertyFile)) {
                PROPS.store(out, "RSHelpKit preferences");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private static void setIfInvalid(String key, String value) {
        if (PROPS.getProperty(key) == null) {
            PROPS.setProperty(key, value);
        }
    }

    public static Dimension getDefaultSize() {
        int width = Integer.parseInt(PROPS.get(Keys.DEFAULT_WIDTH).toString());
        int height = Integer.parseInt(PROPS.get(Keys.DEFAULT_HEIGHT).toString());
        return new Dimension(width, height);
    }

    public static void setDefaultSize(int width, int height) {
        PROPS.setProperty(Keys.DEFAULT_WIDTH, Integer.toString(width));
        PROPS.setProperty(Keys.DEFAULT_HEIGHT, Integer.toString(height));
    }

    public static Point getDefaultLocation() {
        int x = Integer.parseInt(PROPS.get(Keys.DEFAULT_X).toString());
        int y = Integer.parseInt(PROPS.get(Keys.DEFAULT_Y).toString());
        return new Point(x, y);
    }

    public static void setDefaultLocation(int x, int y) {
        PROPS.setProperty(Keys.DEFAULT_X, Integer.toString(x));
        PROPS.setProperty(Keys.DEFAULT_Y, Integer.toString(y));
    }

    public static int getDefaultWorld() {
        return Integer.parseInt(PROPS.get(Keys.DEFAULT_WORLD).toString());
    }

    public static void setDefaultWorld(int world) {
        PROPS.setProperty(Keys.DEFAULT_WORLD, Integer.toString(world));
    }
}
