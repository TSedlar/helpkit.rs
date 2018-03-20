package rs.helpkit;

import rs.helpkit.api.Manifest;
import rs.helpkit.api.Plugin;
import rs.helpkit.api.util.Renderable;
import rs.helpkit.api.util.Time;
import rs.helpkit.internal.RSCanvas;
import rs.helpkit.internal.HookLoader;
import rs.helpkit.plugins.Example;
import rs.helpkit.pref.RSPreferences;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public final List<Plugin> plugins = new ArrayList<>();

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    public OSRSContainer(Applet applet) {
        System.out.println("Game has loaded");
        this.loader = applet.getClass().getClassLoader();
        hideAllButCanvas(applet);
        HookLoader.load(loader);
        plugins.add(new Example());
        plugins.forEach(plugin -> {
            Manifest manifest = plugin.getClass().getAnnotation(Manifest.class);
            if (manifest.loop()) {
                new Thread(plugin).start();
            }
        });
        customCanvas.consumers.add(g -> plugins.stream()
                .filter(p -> p.enabled && p.validate() && p instanceof Renderable)
                .forEach(p -> ((Renderable) p).render(g)));
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
    private void hideAllButCanvas(Applet applet) {
        canvas = (Canvas) applet.getComponent(0);
        window = findWindow(canvas);
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
        for (int i = 0; i < 10 && canvas.getMouseListeners().length == 0; i++) {
            Time.sleep(1000);
        }
        customCanvas = new RSCanvas(canvas);
        panel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension size = panel.getSize();
                RSPreferences.setDefaultSize(size.width, size.height);
                canvas.setPreferredSize(size);
                customCanvas.setPreferredSize(size);
                customCanvas.raw = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
                customCanvas.buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
            }
        });
        window.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                RSPreferences.setDefaultLocation(window.getX(), window.getY());
            }
        });
        customCanvas.startReplacementTask(applet);
        window.revalidate();
        Dimension size = RSPreferences.getDefaultSize();
        canvas.setPreferredSize(size);
        customCanvas.setPreferredSize(size);
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
