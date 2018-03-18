package com.wisekrakr.firstgame.PopUps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;


public class PauseScreen implements Disposable {

    private Texture texture;
    private TextureRegion textureRegion;
    public Stage stage;
    private SpriteBatch batch;
    private Viewport viewport;

    public PauseScreen(SpriteBatch batch) {

        this.batch = batch;

        texture = new Texture("badlogic.jpg");
        textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(Constants.WORLD_WIDTH/2, Constants.WORLD_HEIGHT/2, 100,100);

        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);



    }


    @Override
    public void dispose() {

        stage.dispose();
        batch.dispose();
    }
}
