package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

public class InfoHud implements Disposable {

    private final ClientConnector connector;
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

    public InfoHud(SpriteBatch spriteBatch, ClientConnector connector) {
        this.connector = connector;

        viewport = new FitViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        FileHandle fontStyle = Gdx.files.internal("myFont.fnt");
        BitmapFont font = new BitmapFont(fontStyle);
        font.getData().setScale(0.4f);

        SpaceSnapshot snapshot = connector.latestSnapshot();
        if (snapshot != null) {
            for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                if("Player".equals(object.getType())) {
                    nameLabel = new Label(object.getName(), new Label.LabelStyle(new BitmapFont(), Color.GREEN));
                    nameLabel.setPosition(object.getPosition().x, object.getPosition().y , Align.center);
                }
                if("EnemyChaser".equals(object.getType())){
                    nameLabel = new Label(object.getName(), new Label.LabelStyle(new BitmapFont(), Color.GREEN));
                    nameLabel.setPosition(object.getPosition().x, object.getPosition().y , Align.center);
                }
            }
        }




        stage.addActor(nameLabel);
    }

    @Override
    public void dispose() { stage.dispose(); }
}

