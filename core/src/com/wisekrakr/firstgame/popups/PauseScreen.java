package com.wisekrakr.firstgame.popups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.wisekrakr.firstgame.MyAssetManager;


public class PauseScreen implements Disposable {

    private final MyAssetManager myAssetManager;
    private TextureRegion textureRegion;
    private Texture texture;
    private Stage stage;
    private SpriteBatch batch;

    public PauseScreen(MyAssetManager myAssetManager, SpriteBatch batch, Stage stage) {
        this.myAssetManager = myAssetManager;
        this.batch = batch;
        this.stage = stage;

        myAssetManager.loadFonts();

        texture = this.myAssetManager.assetManager.get("texture/pausedPic2.png");
        textureRegion = new TextureRegion(texture,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

    }

    public void update(){

        stage.getBatch().begin();
        stage.getBatch().draw(textureRegion, 0,0);
        stage.getBatch().end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}
