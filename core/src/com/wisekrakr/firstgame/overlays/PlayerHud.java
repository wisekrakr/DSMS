package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

public class PlayerHud {

    private TextureRegionDrawable healthBarTexture;
    private ProgressBar bar;
    private ProgressBar.ProgressBarStyle barStyle;
    private Skin skin;

    private BitmapFont font;
    private OrthographicCamera camera;
    private MyAssetManager myAssetManager;

    private Label nameLabel;

    private String name;



    public PlayerHud(OrthographicCamera camera) {
        this.camera = camera;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();

        font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);

        Texture healthBar = myAssetManager.assetManager.get("texture/healthbar.png");
        TextureRegion slider = new TextureRegion(healthBar);
        slider.setRegionWidth(Gdx.graphics.getWidth());
        slider.setRegionHeight(50);
        healthBarTexture = new TextureRegionDrawable(slider);

        skin = new Skin();
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

    }

    private Vector3 projection(SpaceSnapshot.GameObjectSnapshot object){
        return camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
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
        name = "Wisekrakr";
        nameLabel = new Label(name, new Label.LabelStyle(font, Color.RED));
        nameLabel.setPosition(projection(object).x, projection(object).y + 30, Align.center);
        return nameLabel;
    }

    public ProgressBar healthBar(SpaceSnapshot.GameObjectSnapshot object){

        barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), healthBarTexture);
        barStyle.knobBefore = barStyle.knob;
        bar = new ProgressBar(healthPercentage(object), maxHealth(object), 20f, false, barStyle);
        bar.setPosition(Gdx.graphics.getWidth()/2,0, Align.center);
        bar.setValue(health(object));
        return bar;
    }

    public void update() {


    }

}

