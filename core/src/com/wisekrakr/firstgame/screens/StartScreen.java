package com.wisekrakr.firstgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.VideoPlayer;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;
import com.wisekrakr.firstgame.playscreens.PlayerPerspectiveScreen;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;


public class StartScreen extends ScreenAdapter {

    private static final String TAG = "";
    private TextField textField;
    private BitmapFont font;
    private PlayerPerspectiveScreen playScreen;

    private VideoPlayer videoPlayer;
    private ShapeRenderer shapeRenderer;
    private Texture texture;
    private MyAssetManager myAssetManager;
    private Stage stage;

    private Spaceship spaceship;
    private float time;

    private float minX, minY, width, height;

    public StartScreen(final SpaceGameContainer container, final ClientConnector connector) {
        final String unique = UUID.randomUUID().toString();

        playScreen = new PlayerPerspectiveScreen(connector, Arrays.asList(unique + "-A"), unique + "-A");
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        myAssetManager = new MyAssetManager();
        myAssetManager.loadSkins();
        myAssetManager.loadMusic();
        myAssetManager.loadTextures();
        myAssetManager.loadFonts();

        final Music music = myAssetManager.assetManager.get("music/space_explorers.mp3", Music.class);
        music.play();
        music.setLooping(true);
        music.setVolume(0.2f);

        texture = myAssetManager.assetManager.get("background/stars.jpg", Texture.class);

        minX = 0;
        minY = 0;
        width = 800;
        height = 800;
        spaceship = new Spaceship("start ship", new Vector2(100,300));


        Skin skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/flat-earth-ui.json")));

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        textField = new TextField("", skin);
        textField.setText("Ship Name?");


        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        table.add(textField).uniformX();
        table.row();
        table.add(newGame).uniformX();
        table.row();
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
                container.setScreen(playScreen);
                music.stop();
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void setSpaceshipBounds(){
        if (spaceship.getPosition().x < minX || spaceship.getPosition().x - minX > width ||
                spaceship.getPosition().y < minY || spaceship.getPosition().y - minY > height) {
            spaceship.setOrientation((float) (spaceship.getOrientation() +  Math.PI));
        }
    }


    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.getBatch().begin();
        stage.getBatch().draw(texture, 0, 0);
        stage.getBatch().end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        time += delta;
        if (time > 3) {
            spaceship.setOrientation(spaceship.setRandomDirectionStartScreen());
            setSpaceshipBounds();
            time = 0;
        }
        shapeRenderer.circle(spaceship.getPosition().x, spaceship.getPosition().y, 20f);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(spaceship.getPosition().x + 4 * (float) Math.cos(spaceship.getOrientation()),
                spaceship.getPosition().y + 4 * (float) Math.sin(spaceship.getOrientation()),
                (20f / 2));
        shapeRenderer.end();

        spaceship.setPosition(new Vector2(spaceship.getPosition().x + (float) Math.cos(spaceship.getOrientation()) * 50 * delta,
                spaceship.getPosition().y + (float) Math.sin(spaceship.getOrientation()) * 50 * delta)
        );

        stage.act();
        stage.draw();

    }

    public TextField getTextField() {
        return textField;
    }

    @Override
    public void dispose() {

        stage.dispose();
    }
}
