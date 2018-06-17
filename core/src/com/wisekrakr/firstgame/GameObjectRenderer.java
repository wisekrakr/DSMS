package com.wisekrakr.firstgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class GameObjectRenderer {

    private Sprite playerSprite;
    private Sprite bulletSprite;
    private Sprite missileSprite;
    private Sprite asteroidSprite;
    private Sprite shieldPowerUpSprite;
    private Sprite healthPowerUpSprite;

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

    public Sprite playerTexture(SpaceSnapshot.GameObjectSnapshot object){
        Texture texture = myAssetManager.assetManager.get("sprites/spaceship.png");
        playerSprite = new Sprite(texture);
        playerSprite.setPosition(object.getPosition().x - playerSprite.getWidth()/2, object.getPosition().y - playerSprite.getHeight()/2);
        playerSprite.setRotation(object.getOrientation());
        playerSprite.setScale(3f);
        return playerSprite;
    }

    public Sprite asteroidTexture(SpaceSnapshot.GameObjectSnapshot object){
        Texture asteroidTexture = myAssetManager.assetManager.get("sprites/asteroid_big.png");
        asteroidSprite = new Sprite(asteroidTexture);
        asteroidSprite.setPosition(object.getPosition().x - asteroidSprite.getWidth()/2, object.getPosition().y - asteroidSprite.getHeight()/2);
        asteroidSprite.setRotation(object.getOrientation());
        return asteroidSprite;
    }

    public Sprite bulletTexture(SpaceSnapshot.GameObjectSnapshot object){
        Texture texture = myAssetManager.assetManager.get("sprites/bullet_small.png");
        bulletSprite = new Sprite(texture);
        bulletSprite.setPosition(object.getPosition().x - bulletSprite.getWidth()/2, object.getPosition().y - bulletSprite.getHeight()/2);

        return bulletSprite;
    }

    public Sprite missileTexture(SpaceSnapshot.GameObjectSnapshot object){
        Texture texture = myAssetManager.assetManager.get("sprites/missile_default.png");
        missileSprite = new Sprite(texture);
        missileSprite.setPosition(object.getPosition().x - missileSprite.getWidth()/2, object.getPosition().y - missileSprite.getHeight()/2);

        return missileSprite;
    }

    public Sprite shieldPowerUp(SpaceSnapshot.GameObjectSnapshot object){
        Texture texture = myAssetManager.assetManager.get("sprites/powerup_shield.png");
        shieldPowerUpSprite = new Sprite(texture);
        shieldPowerUpSprite.setPosition(object.getPosition().x - shieldPowerUpSprite.getWidth()/2,
                object.getPosition().y - shieldPowerUpSprite.getHeight()/2);

        return shieldPowerUpSprite;
    }

    public Sprite healthPowerUp(SpaceSnapshot.GameObjectSnapshot object){
        Texture texture = myAssetManager.assetManager.get("sprites/powerup_health.png");
        healthPowerUpSprite = new Sprite(texture);
        healthPowerUpSprite.setPosition(object.getPosition().x - healthPowerUpSprite.getWidth()/2,
                object.getPosition().y - healthPowerUpSprite.getHeight()/2);

        return healthPowerUpSprite;
    }
    public Sprite setTexture(SpaceSnapshot.GameObjectSnapshot object, Texture texture){
        sprite = new Sprite(texture);
        sprite.setPosition(object.getPosition().x - sprite.getWidth()/2,object.getPosition().y - sprite.getHeight()/2);
        sprite.setScale(3f);

        return sprite;
    }

    public void drawObjects(){

        stage.getBatch().begin();
//        playerSprite.draw(stage.getBatch());
//        asteroidSprite.draw(stage.getBatch());

        stage.getBatch().end();
    }
}
