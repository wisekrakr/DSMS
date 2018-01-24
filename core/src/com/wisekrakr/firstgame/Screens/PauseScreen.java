package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.SpaceGameContainer;
import javafx.stage.Stage;

import java.util.PriorityQueue;

public class PauseScreen extends ScreenAdapter {

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private TextButton buttonContinue, buttonQuit;
    private BitmapFont white, black;
    private Label heading;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private SpaceGameContainer container;


    public PauseScreen() {

        atlas = new TextureAtlas("button.pack");
        skin = new Skin(atlas);

        white = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        black = new BitmapFont(Gdx.files.internal("myFontBlack.fnt"));


    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);


    }

    @Override
    public void dispose() {

    }
}
