package com.wisekrakr.firstgame;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

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
    private Texture texture;
    private float rotation;


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

        rotation = object.getOrientation();
        sprite.setRotation(rotation);
        //sprite.setScale(0.5f);
    }

    public Sprite playerSprite(SpaceSnapshot.GameObjectSnapshot object){
        Texture playerTexture = myAssetManager.assetManager.get("sprites/spaceship_boost.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(object.getPosition().x - playerSprite.getWidth()/2, object.getPosition().y - playerSprite.getHeight()/2);

        //rotation = object.getOrientation();
        //playerSprite.setRotation(rotation += 2f);

        return playerSprite;
    }



    public void update(float delta,SpaceSnapshot.GameObjectSnapshot object){

        stage.getBatch().begin();
//        playerSprite.draw(stage.getBatch());
        stage.getBatch().end();
    }


}
