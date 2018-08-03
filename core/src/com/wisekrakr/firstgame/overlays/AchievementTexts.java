package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class AchievementTexts implements Disposable {

    private Label distanceLabel;
    private Label killedByLabel;

    private MyAssetManager myAssetManager;
    public Stage stage;

    private float timeCounter;
    private float timeNow;

    public AchievementTexts(MyAssetManager myAssetManager) {
        this.myAssetManager = myAssetManager;

        Viewport viewport = new ScalingViewport(Scaling.stretch, Constants.HUD_WIDTH, Constants.HUD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();

        Table table = new Table();
        table.bottom().left();
        table.setFillParent(true);

        BitmapFont font = myAssetManager.assetManager.get("font/default.fnt");
        font.getData().setScale(0.8f);

        distanceLabel = new Label(null, new Label.LabelStyle(font, Color.GOLDENROD));
        killedByLabel = new Label(null, new Label.LabelStyle(font, Color.GOLDENROD));

        table.add(distanceLabel).width(100).pad(10);
        table.add(killedByLabel).width(100).pad(10);

        stage.addActor(table);
    }

    public void update(SpaceSnapshot.GameObjectSnapshot myself, float delta){

        timeCounter += delta;
        if(timeCounter != 0) {
            if (myself != null) {
                Float distance = (Float) myself.extraProperties().get("distanceTravelled");
                String killerName = (String) myself.extraProperties().get("killedBy");
                Double health = (Double) myself.extraProperties().get("health");
                if (distance >= 2222) {
                    timeNow += delta;
                    if (timeNow <= 10) {
                        distanceLabel.setText("Achievement: 2222222 space miles travelled");
                    } else {
                        distanceLabel.remove();
                    }
                }
                if (health <= 0){
                    timeNow += delta;
                    if (timeNow <= 10){
                        killedByLabel.setText(killerName);
                    }
                }

                else {
                    distanceLabel.setText(null);

                }
                timeCounter = 0;
            }
        }

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
