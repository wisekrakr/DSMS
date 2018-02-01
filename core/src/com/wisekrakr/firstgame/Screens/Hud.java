package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

/**
 * Created by David on 12/1/2017.
 */
public class Hud implements Disposable {


    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCounter;
    private Integer distanceCounter;
    private float score;
    private Integer ammoCounter;
    private String name;

    private Label timeLabel;
    private Label nameLabel;
    private Label nameSetLabel;
    private Label timeCountLabel;
    private Label distanceLabel;
    private Label distanceCountLabel;
    private Label scoreCountLabel;
    private Label scoreLabel;
    private Label ammoLabel;
    private Label ammoCountLabel;

    public Hud(SpriteBatch batch) {
        worldTimer = 0;
        timeCounter = 0;
        distanceCounter = 1;
        ammoCounter = 0;
        score = 0;
        name = "Wisekrakr";

        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        FileHandle fontStyle = Gdx.files.internal("myFont.fnt");
        BitmapFont font = new BitmapFont(fontStyle);
        font.getData().setScale(0.4f);

        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        timeCountLabel = new Label(String.format("%06d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        distanceLabel = new Label("Distance", new Label.LabelStyle(font, Color.WHITE));
        distanceCountLabel = new Label(String.format("%06d", distanceCounter), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        scoreLabel = new Label("Score", new Label.LabelStyle(font, Color.WHITE));
        scoreCountLabel = new Label(String.format("%08f", score), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        ammoLabel = new Label("Ammo", new Label.LabelStyle(font, Color.WHITE));
        ammoCountLabel = new Label(String.format("%06d", ammoCounter), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        nameLabel = new Label("Your name here", new Label.LabelStyle(font, Color.WHITE));
        nameSetLabel = new Label(String.format("%s", getNameSetLabel()), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));

        table.add(timeLabel).expandX().padTop(10);
        table.add(distanceLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(ammoLabel).expandX().padTop(10);
        table.add(nameLabel).expandX().padTop(10);
        table.row();
        table.add(timeCountLabel).expandX();
        table.add(distanceCountLabel).expandX();
        table.add(scoreCountLabel).expandX();
        table.add(ammoCountLabel).expandX();
        table.add(nameSetLabel).expandX();

        stage.addActor(table);
    }


    public Label getNameSetLabel() {
        return nameSetLabel;
    }



    public void update(SpaceSnapshot.GameObjectSnapshot myself, float delta) {



        timeCounter += delta;
        if (timeCounter >= 1) {
            timeCounter = 0;
            worldTimer++;

            timeCountLabel.setText(String.format("%s",worldTimer));
            distanceCountLabel.setText(Float.toString((Float) myself.extraProperties().get("distanceTravelled")));
            ammoCountLabel.setText(Integer.toString((Integer) myself.moreExtraProperties().get("ammoCount")));
            nameSetLabel.setText(String.format("%s", myself.getType()));

        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

