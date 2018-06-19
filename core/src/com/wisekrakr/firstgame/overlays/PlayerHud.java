package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class PlayerHud implements Disposable {

    private InputMultiplexer inputMultiplexer;

    private MyAssetManager myAssetManager;
    private TextureRegionDrawable healthBarTexture;
    private Skin skin;

    private BitmapFont font;
    private OrthographicCamera camera;
    public Stage stage;

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
        String fontString = font.toString();

        skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/uiskin.json")));
        skin.getFont("default-font").getData().scale(0.07f);

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

    }

    private Vector3 projection(SpaceSnapshot.GameObjectSnapshot object){
        return camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
    }

    private Boolean hit(SpaceSnapshot.GameObjectSnapshot object){
        return (Boolean) object.hitProperties().get("hit");

    }

    private Float health(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.healthProperties().get("health");
    }

    private Float maxHealth(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.maxHealthProperties().get("maxHealth");
    }
    private Float healthPercentage(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.damageProperties().get("healthPercentage");
    }

    private Float damageTaken(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.damageTakenProperties().get("damageTaken");
    }


    public Label nameLabel(SpaceSnapshot.GameObjectSnapshot object){
        String name = "Wisekrakr";
        Label nameLabel = new Label(name, new Label.LabelStyle(font, Color.RED));
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

        ProgressBar bar = new ProgressBar(healthPercentage(object), maxHealth(object), 20f, true, barStyle);
        bar.setSize(5 * 5, 5 * 5);
        bar.setPosition(Gdx.graphics.getWidth() - bar.getWidth()/2, Gdx.graphics.getHeight() - 25, Align.right);
        bar.setValue(health(object));
        return bar;
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

