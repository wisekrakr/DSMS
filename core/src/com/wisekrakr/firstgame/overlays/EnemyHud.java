package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

import java.util.ArrayList;


public class EnemyHud {

    private BitmapFont font;
    private OrthographicCamera camera;

    private Label nameLabel;

    private TextureRegionDrawable healthBarTexture;
    private ProgressBar bar;
    private Skin skin;

    private ArrayList<DamageAnimation>damageAnimations;
    private boolean activated = true;
    private float timeCounter;
    private boolean noHitYet = true;

    public EnemyHud(OrthographicCamera camera) {
        this.camera = camera;

        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();

        font = myAssetManager.assetManager.get("font/gamerFont.fnt");
        font.getData().setScale(0.4f);

        Texture healthBar = myAssetManager.assetManager.get("texture/healthbar.png");
        TextureRegion slider = new TextureRegion(healthBar);
        slider.setRegionWidth(5);
        slider.setRegionHeight(2);
        healthBarTexture = new TextureRegionDrawable(slider);

        skin = new Skin();
        Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
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

    private Number health(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("health");
    }
    private Number healthPercentage(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("healthPercentage");
    }

    private Number damageTaken(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("damageTaken");
    }

    private Boolean isHit(SpaceSnapshot.GameObjectSnapshot object){
        return (Boolean) object.extraProperties().get("hit");
    }

    public Label nameLabel(SpaceSnapshot.GameObjectSnapshot object){

        if (!(activated)){
            nameLabel.setVisible(false);
            nameLabel.clear();

        }else {
            String name = object.getName();
            nameLabel = new Label(name, new Label.LabelStyle(font, Color.RED));
            nameLabel.setPosition(projection(object).x, projection(object).y + (radius(object) * 3), Align.center);
        }
        return nameLabel;
    }

    public ProgressBar healthBar(SpaceSnapshot.GameObjectSnapshot object){
        if (!(activated)){
            bar.setVisible(false);
        }else{
            ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), healthBarTexture);
            barStyle.knobBefore = barStyle.knob;

            bar = new ProgressBar(healthPercentage(object).floatValue(), 100, 20f, false, barStyle);
            bar.setSize(radius(object) * 3, radius(object) * 3);
            bar.setPosition(projection(object).x, projection(object).y - (radius(object) * 5), Align.center);
            bar.setValue(health(object).floatValue());

        }

        return bar;
    }

    public boolean enableEnemyHud(){
        return activated = true;
    }
    public boolean disableEnemyHud() {
        return activated = !activated;
    }
}
