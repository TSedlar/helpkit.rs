package rs.helpkit.plugins;

import rs.helpkit.api.Manifest;
import rs.helpkit.api.Plugin;
import rs.helpkit.api.game.Players;
import rs.helpkit.api.util.Renderable;

import java.awt.*;

@Manifest(author = "Static", name = "Example Plugin", description = "Just an example", version = 1.0)
public class Example extends Plugin implements Renderable {

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int loop() {
        return 0;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.drawString("data: " + Players.local().level(), 100, 100);
    }
}
