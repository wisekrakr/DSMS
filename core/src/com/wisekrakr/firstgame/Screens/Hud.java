package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.GameDSMS;

/**
 * Created by David on 12/1/2017.
 */
public class Hud implements Disposable{

    private GameDSMS gameDSMS;

    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCounter;
    private float distanceCounter;
    private float score;

    private Label timeLabel;
    private Label timeCountLabel;
    private Label distanceLabel;
    private Label scoreLabel;

    public Hud(SpriteBatch batch) {
        gameDSMS = new GameDSMS();

        worldTimer = 0;
        timeCounter = 0;
        distanceCounter = 0;
        score = 0;

        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeCountLabel = new Label(String.format("%06d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        distanceLabel = new Label("Distance", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%08f", score), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));

        table.add(timeLabel).expandX().padTop(10);
        table.add(distanceLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.row();
        table.add(timeCountLabel).expandX();
        table.add(scoreLabel).expandX();


        stage.addActor(table);



    }

    public void update(float delta){

        float miliCount = 0;
        float secCount = 0;
        float minCount = 0;
        float hourCount = 0;

        timeCounter += delta;
        if(timeCounter >= 1){
            timeCounter = 0;
            worldTimer++;
            timeCountLabel.setText(String.format("%06d", worldTimer));
        }



    }

    @Override
    public void dispose() {

        stage.dispose();

    }
}

