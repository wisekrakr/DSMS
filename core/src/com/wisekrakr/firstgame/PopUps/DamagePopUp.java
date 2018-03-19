package com.wisekrakr.firstgame.PopUps;

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
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class DamagePopUp implements Disposable {

    private Table table;
    private Integer damageCount;
    private Label damageCountLabel;

    private SpaceGameContainer container;
    private OrthographicCamera camera;
    public Stage stage;
    private SpriteBatch batch;


    public DamagePopUp(SpriteBatch batch, SpaceGameContainer container) {

        this.batch = batch;
        this.container = container;

        damageCount = 0;

        camera = new OrthographicCamera();
        camera.update();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);

        FileHandle fontStyle = Gdx.files.internal("myFont.fnt");
        BitmapFont font = new BitmapFont(fontStyle);
        font.getData().setScale(5f);

        damageCountLabel = new Label(String.format("%06d", damageCount), new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));

        stage.addActor(table);
    }

    public void update(SpaceSnapshot.GameObjectSnapshot object, float delta){


        if ("EnemyChaser".equals(object.getType())) {

            damageCountLabel.setText(Integer.toString((Integer) object.healthProperties().get("health")));
            damageCountLabel.setPosition(object.getPosition().x + 20, object.getPosition().y = 20);

        }


    }

    @Override
    public void dispose() {

    }
}
