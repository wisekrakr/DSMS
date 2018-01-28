package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

import java.util.List;

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



