package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.GameState;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.MyAssetManager;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class StartScreen extends ScreenAdapter {

    private GameState gameState = GameState.GAME_READY;

    private FitViewport viewport;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private SpaceGameContainer container;
    private OrthographicCamera camera;

    public StartScreen(final SpaceGameContainer container) {
        final ClientConnector connector = new ClientConnector(new InetSocketAddress("localhost", 12345));

        try {
            connector.start();
        } catch (Exception e) {
            System.out.println("Cannot connect: " + e.getMessage());

            return;
        }

        final String unique = UUID.randomUUID().toString();

        this.container = container;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        //font = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        //font.getData().setScale(0.5f);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
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

            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                container.setScreen(new PlayerPerspectiveScreen(connector, Arrays.asList(unique + "-A", unique + "-B"), unique + "-A"));

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
