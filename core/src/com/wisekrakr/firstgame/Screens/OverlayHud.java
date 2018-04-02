package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

public class OverlayHud implements Disposable {


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
    private Label chaserNameLabel;
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

    public OverlayHud(SpaceSnapshot.GameObjectSnapshot object) {

        viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);

        FileHandle fontStyle = Gdx.files.internal("myFont.fnt");
        BitmapFont font = new BitmapFont(fontStyle);
        font.getData().setScale(0.4f);

        if("Player".equals(object.getType())) {
            nameLabel.setVisible(true);
            nameLabel = new Label("wisekrakr", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
            nameLabel.setPosition(object.getPosition().x, object.getPosition().y , Align.center);

        }
        if("EnemyChaser".equals(object.getType())){
            nameLabel.setVisible(true);
            nameLabel = new Label(object.getName(), new Label.LabelStyle(new BitmapFont(), Color.GREEN));
            nameLabel.setPosition(object.getPosition().x, object.getPosition().y , Align.center);

        }

        stage.addActor(nameLabel);
    }


    @Override
    public void dispose() { stage.dispose(); }
}

