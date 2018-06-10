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
    private Label healthLabel;
    private String name;
    private Label damageLabel;

    private TextureRegionDrawable healthBarTexture;
    private ProgressBar bar;
    private ProgressBar.ProgressBarStyle barStyle;
    private Skin skin;

    private MyAssetManager myAssetManager;
    private float time;
    private ArrayList<DamageAnimation>damageAnimations;

    public EnemyHud(OrthographicCamera camera) {
        this.camera = camera;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();

        font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);

        Texture healthBar = myAssetManager.assetManager.get("texture/healthbar.png");
        TextureRegion slider = new TextureRegion(healthBar);
        slider.setRegionWidth(20);
        slider.setRegionHeight(7);
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

    private Float radius(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.extraProperties().get("radius");
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

    private Boolean isHit(SpaceSnapshot.GameObjectSnapshot object){
        return (Boolean) object.hitProperties().get("hit");
    }

    public Label nameLabel(SpaceSnapshot.GameObjectSnapshot object){
        name = object.getName();
        nameLabel = new Label(name, new Label.LabelStyle(font, Color.RED));
        nameLabel.setPosition(projection(object).x, projection(object).y + (radius(object) + 10), Align.center);
        return nameLabel;
    }

    public Label healthLabel(SpaceSnapshot.GameObjectSnapshot object){

        if (!(health(object) <= health(object)*(50f/100f))){
            healthLabel = new Label(health(object).toString(), new Label.LabelStyle(font, Color.GREEN));
        }else{
            healthLabel = new Label(health(object).toString(), new Label.LabelStyle(font, Color.RED));
        }
        healthLabel.setPosition(projection(object).x, projection(object).y - (radius(object) + 10), Align.center);
        return healthLabel;
    }

    public ProgressBar healthBar(SpaceSnapshot.GameObjectSnapshot object){

        barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), healthBarTexture);
        barStyle.knobBefore = barStyle.knob;

        bar = new ProgressBar(healthPercentage(object), maxHealth(object), 20f, false, barStyle);
        bar.setSize(radius(object) * 3, radius(object) * 3);
        bar.setPosition(projection(object).x, projection(object).y - (radius(object) + 10), Align.center);
        bar.setValue(health(object));
        return bar;
    }


    public Label damageLabel(SpaceSnapshot.GameObjectSnapshot object){

        damageLabel = new Label(damageTaken(object).toString(), new Label.LabelStyle(font, Color.RED));
        damageLabel.setPosition(projection(object).x + (radius(object) + 10), projection(object).y);


        return damageLabel;
    }

    public void update(SpaceSnapshot.GameObjectSnapshot object, float delta){
/*
        if (isHit(object)) {
            damageLabel.setVisible(true);
            time += delta;
            if (time >= 2) {
                damageLabel.setVisible(false);
                time = 0;
            }
      }
*/
    }


}
