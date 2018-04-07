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
import com.wisekrakr.firstgame.GameState;
import com.wisekrakr.firstgame.Screens.PlayerPerspectiveScreen;
import com.wisekrakr.firstgame.SpaceGameContainer;


public class PauseScreen implements Disposable {

    private TextureRegion textureRegion;
    private OrthographicCamera camera;
    private Texture texture;
    public Stage stage;
    private SpriteBatch batch;

    public PauseScreen(SpriteBatch batch) {

        this.batch = batch;

        texture = new Texture("pausedPic2.png");
        textureRegion = new TextureRegion(texture,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


    }

    public void update(){

        batch.begin();
        batch.draw(textureRegion, 0,0);
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}
