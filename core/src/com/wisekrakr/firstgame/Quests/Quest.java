package com.wisekrakr.firstgame.Quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.missions.QuestGen;

import java.util.Set;

public class Quest implements Disposable{

    private OrthographicCamera camera;
    private InputMultiplexer inputMultiplexer;

    private MyAssetManager myAssetManager;
    private Stage stage;
    private Skin skin;

    private Dialog questDialog;
    private Boolean pickedUp = false;
    private boolean acceptQuest = false;
    private Dialog firstQuestDialog;


    public Quest(InputMultiplexer inputMultiplexer) {
        this.camera = camera;
        this.inputMultiplexer = inputMultiplexer;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadSkins();

        stage = new Stage();
        inputMultiplexer.addProcessor(stage);

        skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/flat-earth-ui.json")));


    }



    public Dialog firstQuestDialog(){

        System.out.println("FIRST QUEST");

        firstQuestDialog = new Dialog("Quest: Shoot them back!", skin);
        firstQuestDialog.text("Kill 3 enemy ships");
        TextButton buttonYes = new TextButton("You got it", skin);
        TextButton buttonNo = new TextButton("...Later...", skin);

        firstQuestDialog.button(buttonYes, false);
        firstQuestDialog.button(buttonNo, true);

        firstQuestDialog.show(stage);

        buttonYes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                firstQuestDialog.remove();
                acceptQuest = true;
            }
        });
        buttonNo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                firstQuestDialog.remove();
                acceptQuest = false;
            }
        });

        return firstQuestDialog;
    }

    public void update() {

        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
