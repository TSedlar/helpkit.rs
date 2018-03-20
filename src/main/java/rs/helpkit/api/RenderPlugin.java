package rs.helpkit.api;

import rs.helpkit.api.util.Renderable;

public abstract class RenderPlugin extends Plugin implements Renderable {

    @Override
    public int loop() {
        return -1;
    }
}
