package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class OverlayHud implements Disposable {

    private MyAssetManager myAssetManager;
    public Stage stage;
    private Viewport viewport;

    private Label nameLabel;
    private Label enemyLabel;
    private SpaceSnapshot snapshot;

    public OverlayHud(SpaceSnapshot snapshot) {

        this.snapshot = snapshot;

        viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();

        BitmapFont font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);
        if (snapshot != null) {
            for (SpaceSnapshot.GameObjectSnapshot object: snapshot.getGameObjects()) {
                if ("EnemyChaser".equals(object.getType())) {
                    enemyLabel.setVisible(true);
                    enemyLabel = new Label(String.format("%s", getEnemyLabel()), new Label.LabelStyle(new BitmapFont(), Color.GREEN));
                    enemyLabel.setPosition(object.getPosition().x, object.getPosition().y, Align.center);
                }
            }
        }

        //stage.addActor(nameLabel);
        //stage.addActor(enemyLabel);
    }

    public void update() {
/*
        for (SpaceSnapshot.GameObjectSnapshot object: snapshot.getGameObjects()) {
            if ("ChaserEnemy".equals(object.getType())) {
                enemyLabel.setText(String.format("%s", object.getName()));
            }
        }
*/

    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getEnemyLabel() {
        return enemyLabel;
    }

    @Override
    public void dispose() { stage.dispose(); }
}

