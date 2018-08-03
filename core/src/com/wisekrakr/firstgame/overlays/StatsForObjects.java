package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StatsForObjects {

    private final BitmapFont font;
    private final TextureRegionDrawable healthBarTexture;
    private final Skin skin;
    private OrthographicCamera camera;
    private Stage stage;

    private List<Actor> volatileActors = new ArrayList<Actor>();
    private boolean activated = true;

    private Label nameLabel;
    private ProgressBar bar;

    public StatsForObjects(Stage stage, OrthographicCamera camera) {
        this.stage = stage;
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

    private Float radius(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.extraProperties().get("radius");
    }

    private Number health(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("health");
    }

    private Number healthPercentage(SpaceSnapshot.GameObjectSnapshot object){
        return (Double) object.extraProperties().get("healthPercentage");
    }

    private Vector3 projection(SpaceSnapshot.GameObjectSnapshot object){
        return camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
    }

    public void setAllLabels(SpaceSnapshot.GameObjectSnapshot object){
        if (!(activated)) {
            nameLabel.setVisible(false);
            nameLabel.clear();
            bar.setVisible(false);
        }else {
            nameLabel = new Label(object.getName(), new Label.LabelStyle(font, Color.GOLD));
            nameLabel.setPosition(object.getPosition().x, object.getPosition().y + (radius(object) * 3), Align.center);
            stage.addActor(nameLabel);
            registerVolatileActor(nameLabel);

            ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), healthBarTexture);
            barStyle.knobBefore = barStyle.knob;

            bar = new ProgressBar(healthPercentage(object).floatValue(), 100, 20f, false, barStyle);
            bar.setSize(radius(object) * 3, radius(object) * 3);
            bar.setPosition(object.getPosition().x, object.getPosition().y - (radius(object) * 5), Align.center);
            bar.setValue(health(object).floatValue());
            stage.addActor(bar);
            registerVolatileActor(bar);
        }

    }

    public boolean enableEnemyHud(){
        return activated = true;
    }
    public boolean disableEnemyHud() {
        return activated = !activated;
    }

    private void registerVolatileActor(Actor actor) {
        volatileActors.add(actor);
    }

    public void updateStatLabels(){
        for (Actor actor : volatileActors) {
            actor.remove();
        }
        volatileActors.clear();

    }
}
