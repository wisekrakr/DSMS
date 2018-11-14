package com.wisekrakr.firstgame.overlays;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;

public class InGameMenu implements Disposable{

    private Stage stage;
    private PhysicalObjectSnapshot playerSnapshot;
    private InputMultiplexer inputMultiplexer;
    private MyAssetManager myAssetManager;
    private SpriteBatch batch;

    public InGameMenu(SpriteBatch batch, MyAssetManager myAssetManager, InputMultiplexer inputMultiplexer, PhysicalObjectSnapshot playerSnapshot) {
        this.batch = batch;
        this.myAssetManager = myAssetManager;
        this.inputMultiplexer = inputMultiplexer;
        this.playerSnapshot = playerSnapshot;

        stage = new Stage();

        Skin skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/flat-earth-ui.json")));

        inputMultiplexer.addProcessor(stage);

        BitmapFont font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        Label nameSetLabel = new Label(String.format("%s", playerSnapshot.getExtra().get("tag")), new Label.LabelStyle(font, Color.GOLDENROD));
        Label nearestLabel = new Label(String.format("%s", playerSnapshot.getExtra().get("nearestObject")), new Label.LabelStyle(font, Color.GOLDENROD));

        table.setSkin(skin);
        table.setPosition(-300, 500);

        table.row();
        table.add(nameSetLabel).padLeft(20f).left();
        table.row();
        table.add(nearestLabel).padLeft(20f).left();

        stage.addActor(table);
    }



    public void createAndShowGUI() {

        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
