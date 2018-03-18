package com.wisekrakr.firstgame.PopUps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.SpaceGameContainer;


public class PauseScreen extends ScreenAdapter {

    private final SpaceGameContainer container;
    private OrthographicCamera camera;
    private Texture texture;
    private Sprite sprite;
    public Stage stage;
    private SpriteBatch batch;

    public PauseScreen(SpriteBatch batch, SpaceGameContainer container) {

        this.container = container;
        this.batch = batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera.update();

        texture = new Texture("badlogic.jpg");
        sprite = new Sprite(texture);

        Gdx.input.setInputProcessor(stage);

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        sprite.draw(batch);
        batch.end();

    }



    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}
