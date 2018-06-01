package com.wisekrakr.firstgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wisekrakr.firstgame.MyAssetManager;
import sun.java2d.windows.GDIBlitLoops;

import java.awt.*;

public class SplashIntroScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;

    public SplashIntroScreen() {
        batch = new SpriteBatch();
        texture = new Texture("texture/intrologo.jpg");

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(texture, 0,  0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

    }

    @Override
    public void dispose() {
        texture.dispose();
        batch.dispose();
    }
}
