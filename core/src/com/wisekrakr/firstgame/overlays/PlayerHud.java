package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;

import java.util.ArrayList;
import java.util.List;

public class PlayerHud implements Disposable {

    private BitmapFont debugFont;
    private InputMultiplexer inputMultiplexer;

    private MyAssetManager myAssetManager;
    private TextureRegionDrawable healthBarTexture;
    private Skin skin;

    private BitmapFont font;
    private OrthographicCamera camera;
    public Stage stage;
    private Label nameLabel;
    private ProgressBar bar;

    private List<Scenario> scenarios = new ArrayList<Scenario>();
    private Label orientationLabel;

    public PlayerHud(OrthographicCamera camera, InputMultiplexer inputMultiplexer) {
        this.camera = camera;
        this.inputMultiplexer = inputMultiplexer;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();
        myAssetManager.loadSkins();

        stage = new Stage();
        inputMultiplexer.addProcessor(stage);

        font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);
        debugFont = myAssetManager.assetManager.get("font/default.fnt");
        font.getData().setScale(0.8f);

        skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/flat-earth-ui.json")));

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

    }

    private Vector3 projection(SpaceSnapshot.GameObjectSnapshot object){
        return camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
    }


    private Float radius(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.extraProperties().get("radius");
    }

    private Double health(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("health");
    }

    private Double maxHealth(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("maxHealth");
    }
    private Double healthPercentage(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("healthPercentage");
    }

    private Double damageTaken(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("damageTaken");
    }


    public Label nameLabel(SpaceSnapshot.GameObjectSnapshot object){
        String name = "P1";
        nameLabel = new Label(name, new Label.LabelStyle(font, Color.RED));
        nameLabel.setPosition(projection(object).x, projection(object).y + 30, Align.center);
        return nameLabel;
    }

    public ProgressBar healthBar(SpaceSnapshot.GameObjectSnapshot object){
        Texture healthBar = myAssetManager.assetManager.get("texture/healthbar.png");
        TextureRegion slider = new TextureRegion(healthBar);
        slider.setRegionWidth(3);
        slider.setRegionHeight(1);
        healthBarTexture = new TextureRegionDrawable(slider);

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), healthBarTexture);
        barStyle.knobBefore = barStyle.knob;

        bar = new ProgressBar(healthPercentage(object).floatValue(), maxHealth(object).floatValue(), 20f, true, barStyle);
        bar.setSize(5 * 5, 5 * 5);
        bar.setPosition(Gdx.graphics.getWidth() - bar.getWidth()/2, Gdx.graphics.getHeight() - 25, Align.right);
        bar.setValue(health(object).floatValue());

        return bar;
    }

    public Label orientationLabel(SpaceSnapshot.GameObjectSnapshot objectSnapshot){

        orientationLabel = new Label("orientation = " + String.valueOf(objectSnapshot.getOrientation()), new Label.LabelStyle(debugFont, Color.WHITE));
        orientationLabel.setPosition(projection(objectSnapshot).x + 10, projection(objectSnapshot).y - 10);

        return orientationLabel;
    }

    public Label speedLabel(SpaceSnapshot.GameObjectSnapshot objectSnapshot){

        Float speed = (Float) objectSnapshot.extraProperties().get("speed");
        Label speedLabel = new Label("speed = " + String.valueOf(speed), new Label.LabelStyle(debugFont, Color.WHITE));
        speedLabel.setPosition(projection(objectSnapshot).x + 10, projection(objectSnapshot).y - 20);

        return speedLabel;
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

