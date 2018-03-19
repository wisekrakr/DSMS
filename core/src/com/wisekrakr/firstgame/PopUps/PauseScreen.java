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


public class PauseScreen implements Disposable {

    private final SpaceGameContainer container;
    private TextureRegion textureRegion;
    private OrthographicCamera camera;
    private Texture texture;
    public Stage stage;
    private SpriteBatch batch;

    public PauseScreen(SpriteBatch batch, SpaceGameContainer container) {

        this.container = container;
        this.batch = batch;

        camera = new OrthographicCamera();
        camera.update();

        texture = new Texture("pausedPic2.png");
        textureRegion = new TextureRegion(texture,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);


    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void update(float delta){

        camera.update();
        stage.getBatch().setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(textureRegion, 0,0);
        batch.end();

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        texture.dispose();
    }
}
