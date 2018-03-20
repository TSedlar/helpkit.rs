package rs.helpkit;

import rs.helpkit.api.Manifest;
import rs.helpkit.api.Plugin;
import rs.helpkit.api.game.Client;
import rs.helpkit.api.util.Renderable;
import rs.helpkit.internal.HookLoader;
import rs.helpkit.internal.RSCanvas;
import rs.helpkit.plugins.Example;
import rs.helpkit.pref.RSPreferences;
import rs.helpkit.reflect.Classes;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class OSRSContainer {

    private final ClassLoader loader;
    private Canvas canvas;
    private RSCanvas customCanvas;
    private Window window;
    private Panel panel;

    private final List<Plugin> plugins = new ArrayList<>();

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    public OSRSContainer(Applet applet) {
        System.out.println("Game has loaded");
        this.loader = applet.getClass().getClassLoader();
        hideAllButCanvas(applet);
        HookLoader.load(loader);
        plugins.add(new Example());
        customCanvas.consumers.add(g -> plugins.stream()
                .filter(plugin -> plugin instanceof Renderable).forEach(plugin -> {
                    if (plugin.enabled && plugin.validate()) {
                        ((Renderable) plugin).render(g);
                    }
                }));
        plugins.forEach(plugin -> {
            Manifest manifest = plugin.getClass().getAnnotation(Manifest.class);
            if (manifest.loop()) {
                new Thread(plugin).start();
            }
        });
    }

    private Window findWindow(Canvas canvas) {
        Component component = canvas;
        while ((component = component.getParent()) != null) {
            if (component instanceof Window) {
                return (Window) component;
            }
        }
        throw new IllegalStateException("Unable to find window");
    }

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    private void replaceCanvas(Applet applet, Canvas canvas) {
        customCanvas = new RSCanvas(canvas);
        applet.remove(0);
        applet.add(customCanvas);
        Class<?> engine = applet.getClass().getSuperclass();
        Field canvasField = Classes.findField(engine, field -> field.getType().equals(Canvas.class));
        try {
            Objects.requireNonNull(canvasField).set(applet, customCanvas);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to set custom canvas");
        }
        customCanvas.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension size = customCanvas.getSize();
                RSPreferences.setDefaultSize(size.width, size.height);
                customCanvas.raw = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
                customCanvas.buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
            }
        });
        window.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                RSPreferences.setDefaultLocation(window.getX(), window.getY());
            }
        });
    }

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    private void hideAllButCanvas(Applet applet) {
        canvas = (Canvas) applet.getComponent(0);
        window = findWindow(canvas);
        replaceCanvas(applet, canvas);
        Component[] components = window.getComponents();
        for (Component child : components) {
            if (child instanceof Container) {
                Container container = (Container) child;
                panel = (Panel) container;
                for (Component gc : container.getComponents()) {
                    if (!(gc instanceof Panel)) {
                        gc.setVisible(false);
                        container.remove(gc);
                    }
                }
                break;
            }
        }
        window.revalidate();
        Dimension size = RSPreferences.getDefaultSize();
        customCanvas.raw = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
        customCanvas.buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
        panel.setSize(size);
        panel.setPreferredSize(size);
        window.pack();
        Point location = RSPreferences.getDefaultLocation();
        if (location.x == -1 && location.y == -1) {
            window.setLocationRelativeTo(null);
        } else {
            window.setLocation(location);
        }
        window.toFront();
        window.requestFocus();
    }
}
