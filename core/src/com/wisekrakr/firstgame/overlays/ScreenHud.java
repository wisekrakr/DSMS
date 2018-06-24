package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

/**
 * Created by David on 12/1/2017.
 */
public class ScreenHud implements Disposable {

    private ProgressBar bar;
    private ProgressBar.ProgressBarStyle barStyle;
    private Skin skin;
    private Spaceship.SwitchWeaponState switchWeaponState = Spaceship.SwitchWeaponState.NONE;

    private final MyAssetManager myAssetManager;
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCounter;
    private Integer distanceCounter;
    private Integer score;
    private Integer ammoCounter;
    private Integer missileCounter;
    private String name;
    private Integer healthCounter;
    private ProgressBar progressBar;

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
    private Label missileLabel;
    private Label missileCountLabel;
    private Label healthLabel;
    private Label healthCountLabel;
    private String weaponName;

    public ScreenHud(MyAssetManager myAssetManager) {
        this.myAssetManager = myAssetManager;
        worldTimer = 0;
        timeCounter = 0;
        distanceCounter = 1;
        ammoCounter = 0;
        missileCounter = 0;
        score = 0;
        healthCounter = 1000;

        viewport = new ScalingViewport(Scaling.stretch, Constants.HUD_WIDTH, Constants.HUD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        BitmapFont font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.6f);

        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        timeCountLabel = new Label(String.format("%06d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        distanceLabel = new Label("Distance", new Label.LabelStyle(font, Color.WHITE));
        distanceCountLabel = new Label(String.format("%06d", distanceCounter), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        scoreLabel = new Label("Score", new Label.LabelStyle(font, Color.WHITE));
        scoreCountLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        //ammoLabel = new Label("Ammo", new Label.LabelStyle(font, Color.WHITE));
        ammoLabel = new Label(String.format("%s", getAmmoLabel()), new Label.LabelStyle(font, Color.WHITE));
        ammoCountLabel = new Label(String.format("%06d", ammoCounter), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        nameLabel = new Label("Your name here", new Label.LabelStyle(font, Color.WHITE));
        nameSetLabel = new Label(String.format("%s", getNameSetLabel()), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        healthLabel = new Label("HP", new Label.LabelStyle(font, Color.WHITE));
        healthCountLabel = new Label(String.format("%06d", healthCounter), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));

        Table infoTable = new Table();
        infoTable.bottom();
        infoTable.setFillParent(true);

        table.add(timeLabel).expandX().padTop(10);
        table.add(distanceLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(ammoLabel).expandX().padTop(10);
        table.add(nameLabel).expandX().padTop(10);
        table.add(healthLabel).expandX().padTop(10);
        table.row();
        table.add(timeCountLabel).expandX();
        table.add(distanceCountLabel).expandX();
        table.add(scoreCountLabel).expandX();
        table.add(ammoCountLabel).expandX();
        table.add(nameSetLabel).expandX();
        table.add(healthCountLabel).expandX();

        stage.addActor(table);

    }


    public Label getNameSetLabel() {
        return nameSetLabel;
    }

    public Spaceship.SwitchWeaponState getSwitchWeaponState() {
        return switchWeaponState;
    }

    public Label getAmmoLabel() {
        return ammoLabel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void update(SpaceSnapshot.GameObjectSnapshot myself, float delta) {
        timeCounter += delta;
        if (timeCounter >= 1) {
            if (myself != null) {
                timeCounter = 0;
                worldTimer++;
                weaponName = getSwitchWeaponState().toString();
                setName("Wisekrakr");

                timeCountLabel.setText(String.format("%s",worldTimer));
                distanceCountLabel.setText(Float.toString((Float) myself.extraProperties().get("distanceTravelled")));
                scoreCountLabel.setText(Float.toString((Float) myself.scoreProperties().get("score")));
                ammoLabel.setText(String.format("%s", myself.randomProperties().get("switchWeaponState")));
                ammoCountLabel.setText(Integer.toString((Integer) myself.ammoProperties().get("ammoCount")));
                healthCountLabel.setText(Float.toString((Float) myself.healthProperties().get("health")));
                nameSetLabel.setText(String.format("%s", getName()));



            }
            else {

                distanceCountLabel.setText("N/A");
               // scoreCountLabel.setText("N/A");
                ammoLabel.setText("N/A");
                ammoCountLabel.setText("N/A");
                healthCountLabel.setText("N/A");
                nameSetLabel.setText("N/A");
            }
        }


    }


    public ProgressBar getBar() {
        return bar;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

