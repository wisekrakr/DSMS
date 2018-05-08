package com.wisekrakr.firstgame.popups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wisekrakr.firstgame.MyAssetManager;

public class PauseScreenAdapter extends ScreenAdapter {

    private Texture texture;
    private MyAssetManager myAssetManager;
    private Stage stage;

    public PauseScreenAdapter() {

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        myAssetManager = new MyAssetManager();
        myAssetManager.loadSkins();
        myAssetManager.loadMusic();
        myAssetManager.loadTextures();

        texture = myAssetManager.assetManager.get("texture/pausedPic2.png", Texture.class);


        Skin skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/uiskin.json")));
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act();
        stage.draw();
    }
}
