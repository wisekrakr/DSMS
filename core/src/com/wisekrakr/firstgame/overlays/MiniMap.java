package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MiniMap extends Viewport {


    private OrthographicCamera camera;
    private float mapScreenHeightFraction, paddingScreenHeightFraction;


    public MiniMap (float mapScreenHeightFraction, float paddingScreenHeightFraction, OrthographicCamera camera) {
        this.mapScreenHeightFraction = mapScreenHeightFraction;
        this.paddingScreenHeightFraction = paddingScreenHeightFraction;
        this.camera = camera;
        setCamera(camera);

    }
    @Override
    public void update (int screenWidth, int screenHeight, boolean centerCamera) {
        if (getWorldHeight() == 0)
            throw new UnsupportedOperationException("Cannot update MinimapViewport before setting its world size.");
        float worldAspectRatio = getWorldWidth() / getWorldHeight();
        int minimapHeight = Math.round(mapScreenHeightFraction * screenHeight);
        int minimapWidth = Math.round(minimapHeight * worldAspectRatio);
        int padding = Math.round(paddingScreenHeightFraction * screenHeight);

        setScreenBounds(screenWidth - minimapWidth - padding, screenHeight - minimapHeight - padding, minimapWidth, minimapHeight);
        apply(centerCamera);
    }

    @Override
    public void apply() {
        super.apply();
    }
}



