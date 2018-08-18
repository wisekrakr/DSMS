package com.wisekrakr.firstgame.quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class MissionText implements Disposable{


    private Label missionLabel;
    private MyAssetManager myAssetManager;
    private Stage stage;
    private float time;

    public MissionText(MyAssetManager myAssetManager) {
        this.myAssetManager = myAssetManager;

        Viewport viewport = new ScalingViewport(Scaling.stretch, Constants.HUD_WIDTH, Constants.HUD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();

        Table table = new Table();
        table.center().right().padRight(10f);
        table.setFillParent(true);

        BitmapFont font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.3f);

        missionLabel = new Label(null, new Label.LabelStyle(font, Color.GOLD));

        table.add(missionLabel).width(100).pad(10);

        stage.addActor(table);

    }


    public void showMission(SpaceSnapshot.GameObjectSnapshot object, float delta){
        stage.act();
        stage.draw();

        Boolean pickedUpMission = (Boolean) object.extraProperties().get("pickedUp");
        if (!(pickedUpMission)){
            missionLabel.setVisible(false);
        }else {
            missionLabel.setVisible(true);
            missionLabel.setText("Kill some bastard");
            time += delta;
            if (time >= 5f){
                missionLabel.clear();
                missionLabel.setText("");
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
