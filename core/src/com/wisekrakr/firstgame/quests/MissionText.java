package com.wisekrakr.firstgame.quests;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;


public class MissionText implements Disposable{


    private Label missionLabel;
    private Label missionSortLabel;
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
        table.center().left().padLeft(5f);
        table.setFillParent(true);

        BitmapFont font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.3f);

        missionLabel = new Label("MISSION:", new Label.LabelStyle(font, Color.GOLD));
        missionSortLabel = new Label(null, new Label.LabelStyle(font, Color.WHITE));
        missionSortLabel.setFontScale(0.5f);

        table.add(missionLabel).center();
        table.row();
        table.add(missionSortLabel).width(100).pad(5);

        stage.addActor(table);
    }

    private String name(PhysicalObjectSnapshot object){
        return object.getName();
    }

    public void showMission(PhysicalObjectSnapshot object, float delta){
        stage.act();
        stage.draw();

        Boolean pickedUpMission = (Boolean) object.getExtra().get("pickedUp");

        if (!(pickedUpMission)){
            missionLabel.setVisible(false);
            missionSortLabel.setVisible(false);
        }else {
            missionLabel.setVisible(true);
            missionSortLabel.setVisible(true);
            missionSortLabel.setText(name(object));
            time += delta;
            if (time >= 5f){
                missionLabel.setVisible(false);
                missionSortLabel.setVisible(false);
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
