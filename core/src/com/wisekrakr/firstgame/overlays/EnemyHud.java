package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.wisekrakr.firstgame.HealthBar;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;


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
    private Integer damage;
    private float time;
    private boolean hit;

    private float health;


    public EnemyHud(OrthographicCamera camera) {
        this.camera = camera;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();

        font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);

        Texture healthBar = myAssetManager.assetManager.get("texture/healthbar.png");
        TextureRegion slider = new TextureRegion(healthBar);
        slider.setRegionWidth(30);
        slider.setRegionHeight(10);
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

    private Float damageTaken(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.extraProperties().get("damageTaken");
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
        //barStyle.knobBefore = barStyle.knob;
        bar = new ProgressBar(0, health(object), 1.5f, false, barStyle);
        bar.setAnimateDuration(1.2f);
        bar.setSize(radius(object) * 1.5f, radius(object) * 1.5f);
        bar.setValue((float)health(object));
        bar.setPosition(projection(object).x, projection(object).y - (radius(object) + 10), Align.center);




        return bar;
    }

    public Label damageLabel(SpaceSnapshot.GameObjectSnapshot object){
        damage = (Integer) object.damageProperties().get("damage");
        hit = (Boolean) object.randomProperties().get("hit");

        damageLabel = new Label(damage.toString(), new Label.LabelStyle(font, Color.RED));
        if (!(hit)){
            damageLabel.setVisible(false);
        }
        damageLabel.setPosition(projection(object).x + (radius(object) + 10), projection(object).y, Align.center);
        return damageLabel;
    }

    public void update(SpaceSnapshot.GameObjectSnapshot object, float delta) {

    }


}
