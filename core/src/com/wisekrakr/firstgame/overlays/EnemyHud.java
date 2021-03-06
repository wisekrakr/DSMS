package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;

import java.util.ArrayList;


public class EnemyHud {

    private BitmapFont debugFont;
    private BitmapFont font;
    private OrthographicCamera camera;

    private Label nameLabel;

    private TextureRegionDrawable healthBarTexture;
    private ProgressBar bar;
    private Skin skin;

    private ArrayList<DamageAnimation>damageAnimations;
    private boolean activated = true;
    private boolean activatedData = true;
    private float timeCounter;
    private boolean noHitYet = true;
    private Label orientationLabel;
    private Label speedLabel;
    private Label positionLabel;
    private Label directionLabel;

    public EnemyHud(OrthographicCamera camera) {
        this.camera = camera;

        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadTextures();

        font = myAssetManager.assetManager.get("font/gamerFont.fnt");
        font.getData().setScale(0.4f);

        debugFont = myAssetManager.assetManager.get("font/default.fnt");
        debugFont.getData().setScale(0.8f);

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

    private Vector3 projection(PhysicalObjectSnapshot  object){
        return camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
    }

    private Float radius(PhysicalObjectSnapshot  object){
        return (Float) object.getExtra().get("radius");
    }

    private Number health(PhysicalObjectSnapshot object){
        return (Double) object.getExtra().get("health");
    }
    private Number healthPercentage(PhysicalObjectSnapshot  object){
        return (Double) object.getExtra().get("healthPercentage");
    }

    public Label nameLabel(PhysicalObjectSnapshot  object){

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

    public ProgressBar healthBar(PhysicalObjectSnapshot  object){
        if (!(activated)){
            bar.setVisible(false);
        }else{
            ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), healthBarTexture);
            barStyle.knobBefore = barStyle.knob;

            bar = new ProgressBar(healthPercentage(object).floatValue(), 100, 20f, false, barStyle);
            bar.setSize(radius(object) * 3, radius(object) * 3);
            bar.setPosition(projection(object).x, projection(object).y - (radius(object) +10), Align.center);
            bar.setValue(health(object).floatValue());

        }

        return bar;
    }

    public Label positionLabel(PhysicalObjectSnapshot objectSnapshot){

        Float x = objectSnapshot.getPosition().x;
        Float y = objectSnapshot.getPosition().y;

        if (!(activatedData)) {
            positionLabel.setVisible(false);
            positionLabel.clear();
        }else {
            positionLabel = new Label("X = " + String.valueOf(x) + ", Y = " + String.valueOf(y), new Label.LabelStyle(debugFont, Color.WHITE));
            positionLabel.setPosition(projection(objectSnapshot).x + radius(objectSnapshot) + 5, projection(objectSnapshot).y);
        }
        return positionLabel;
    }

    public Label orientationLabel(PhysicalObjectSnapshot  objectSnapshot){

        if (!(activatedData)) {
            orientationLabel.setVisible(false);
            orientationLabel.clear();
        }else {
            orientationLabel = new Label("orientation = " + objectSnapshot.getOrientation(), new Label.LabelStyle(debugFont, Color.WHITE));
            orientationLabel.setPosition(projection(objectSnapshot).x + radius(objectSnapshot) +5, projection(objectSnapshot).y -10 );
        }
        return orientationLabel;
    }

    public Label directionLabel(PhysicalObjectSnapshot  objectSnapshot){

        if (!(activatedData)) {
            directionLabel.setVisible(false);
            directionLabel.clear();
        }else {
            directionLabel = new Label("direction = " + objectSnapshot.getSpeedDirection(), new Label.LabelStyle(debugFont, Color.WHITE));
            directionLabel.setPosition(projection(objectSnapshot).x + radius(objectSnapshot) +5, projection(objectSnapshot).y -20 );
        }
        return directionLabel;
    }

    public Label speedLabel(PhysicalObjectSnapshot  objectSnapshot){
        if (!(activatedData)) {
            speedLabel.setVisible(false);
            speedLabel.clear();
        }else {
            Float speed = objectSnapshot.getSpeedMagnitude();
            speedLabel = new Label("speed = " + speed, new Label.LabelStyle(debugFont, Color.WHITE));
            speedLabel.setPosition(projection(objectSnapshot).x + radius(objectSnapshot) +5, projection(objectSnapshot).y - 30);
        }
        return speedLabel;
    }

    public boolean enableEnemyHud(){
        return activated = true;
    }
    public boolean disableEnemyHud() {
        return activated = !activated;
    }

    public boolean enableMetaData(){
        return activatedData = true;
    }

    public boolean disableMetaData(){
        return activatedData = !activated;
    }
}
