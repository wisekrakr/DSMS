package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;


public class EnemyHud {

    private BitmapFont font;
    private OrthographicCamera camera;

    private Label nameLabel;
    private Label healthLabel;
    private String name;
    private Label damageLabel;

    private MyAssetManager myAssetManager;
    private Integer health;
    private Integer damage;
    private float time;
    private boolean hit;

    public EnemyHud(OrthographicCamera camera) {
        this.camera = camera;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();

        font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);


    }

    private Vector3 projection(SpaceSnapshot.GameObjectSnapshot object){
        return camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
    }

    private Float radius(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.extraProperties().get("radius");
    }

    public Label nameLabel(SpaceSnapshot.GameObjectSnapshot object){
        name = object.getName();
        nameLabel = new Label(name, new Label.LabelStyle(font, Color.RED));
        nameLabel.setPosition(projection(object).x, projection(object).y + (radius(object) + 10), Align.center);
        return nameLabel;
    }

    public Label healthLabel(SpaceSnapshot.GameObjectSnapshot object){
        health = (Integer) object.healthProperties().get("health");
        if (!(health <= health*(50f/100f))){
            healthLabel = new Label(health.toString(), new Label.LabelStyle(font, Color.GREEN));
        }else{
            healthLabel = new Label(health.toString(), new Label.LabelStyle(font, Color.RED));
        }
        healthLabel.setPosition(projection(object).x, projection(object).y - (radius(object) + 10), Align.center);
        return healthLabel;
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

    public void update(float delta){

    }
}
