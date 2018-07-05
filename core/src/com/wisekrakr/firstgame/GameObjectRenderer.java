package com.wisekrakr.firstgame;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;


public class GameObjectRenderer {

    private Spaceship.SteeringState steeringState;

    private Sprite playerSprite;

    private Stage stage;
    private OrthographicCamera camera;
    private MyAssetManager myAssetManager;
    private Sprite sprite;

    public GameObjectRenderer(OrthographicCamera camera, Stage stage) {
        this.camera = camera;
        this.stage = stage;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadTextures();
    }

    private Float radius(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.extraProperties().get("radius");
    }

    public void setSpriteTo(SpaceSnapshot.GameObjectSnapshot object, Sprite sprite){
        this.sprite = new Sprite(sprite);
        sprite.setPosition(object.getPosition().x - sprite.getWidth()/2, object.getPosition().y - sprite.getHeight()/2);

        float rotation = (float) (object.getOrientation() * 180 / Math.PI - 90);
        sprite.setRotation(rotation);
        //setscale in playscreen

    }

    public Sprite playerSprite(SpaceSnapshot.GameObjectSnapshot object){
        Texture playerTexture = myAssetManager.assetManager.get("sprites/spaceship_boost.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(object.getPosition().x - playerSprite.getWidth()/2, object.getPosition().y - playerSprite.getHeight()/2);
        float rotation = (float) (object.getOrientation() * 180 / Math.PI - 90);
        playerSprite.setRotation(rotation);
        //playerSprite.setScale(0.7f);

        return playerSprite;
    }




}
