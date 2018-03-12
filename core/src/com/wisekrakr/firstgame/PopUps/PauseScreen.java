package com.wisekrakr.firstgame.PopUps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.Screens.PlayerPerspectiveScreen;
import com.wisekrakr.firstgame.SpaceGameContainer;


import java.awt.*;
import java.util.PriorityQueue;

public class PauseScreen extends ScreenAdapter {

    private Texture texture;
    private PlayerPerspectiveScreen screen;
    private com.badlogic.gdx.scenes.scene2d.Stage stage;
    private SpaceGameContainer container;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    public PauseScreen(SpaceGameContainer container, OrthographicCamera camera) {

        this.container = container;
        this.camera = camera;

        texture = new Texture("badlogic.jpg");

        stage = new Stage();
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {

        stage.act();
        stage.draw();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();

    }

    @Override
    public void dispose() {

        stage.dispose();
        batch.dispose();
    }
}
