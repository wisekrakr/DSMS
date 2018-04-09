package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;

import java.util.Arrays;
import java.util.UUID;


public class StartScreen extends ScreenAdapter {

    private MyAssetManager myAssetManager;
    private Stage stage;

    public StartScreen(final SpaceGameContainer container, final ClientConnector connector) {
        final String unique = UUID.randomUUID().toString();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        myAssetManager = new MyAssetManager();
        myAssetManager.loadSkins();
        myAssetManager.loadMusic();
        final Music music = myAssetManager.assetManager.get("music/space_explorers.mp3", Music.class);
        music.play();
        music.setLooping(true);
        music.setVolume(0.5f);

        Skin skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/uiskin.json")));
        TextureAtlas textureAtlas = new TextureAtlas();

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        table.add(newGame).uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).uniformX();
        table.row();
        table.add(exit).uniformX();

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Gdx.app.exit();
                music.stop();

            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                container.setScreen(new PlayerPerspectiveScreen(connector, Arrays.asList(unique + "-A"), unique + "-A"));
                music.stop();
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {

        stage.dispose();
    }
}
