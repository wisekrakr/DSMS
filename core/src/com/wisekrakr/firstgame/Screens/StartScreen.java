package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;

import java.util.List;


public class StartScreen extends ScreenAdapter {

    private final ClientConnector connector;
    private final List<String> players;
    private final String mySelf;
    private OrthographicCamera camera;
    private PlayerPerspectiveScreen playerPerspectiveScreen;
    private SpriteBatch batch;
    private BitmapFont font;
    private SpaceGameContainer container;

    public StartScreen(ClientConnector connector, List<String> players, String mySelf) {
        this.connector = connector;
        this.players = players;
        this.mySelf = mySelf;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        font = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        font.getData().setScale(0.8f);

        batch = new SpriteBatch();

        container = new SpaceGameContainer();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Don't Shoot my Spaceship!", 50, 300);
        font.draw(batch, "Click to start", 50, 100);
        batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            container.setScreen(new PlayerPerspectiveScreen(connector, players, mySelf));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
