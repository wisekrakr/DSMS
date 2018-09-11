package com.wisekrakr.firstgame.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class Compass implements Disposable{

    private Sprite spriteCompass;
    private Stage stage;
    private MyAssetManager myAssetManager;

    public Compass() {
        myAssetManager = new MyAssetManager();
        myAssetManager.loadTextures();

        stage = new Stage();

        Texture texture = myAssetManager.assetManager.get("sprites/compass_small.png");
        spriteCompass = new Sprite(texture);
        spriteCompass.setPosition(Gdx.graphics.getWidth() - spriteCompass.getWidth(),0);

    }

    public void updateCompass(SpaceSnapshot.GameObjectSnapshot object){
        if (object != null){

            stage.getBatch().begin();
            spriteCompass.draw(stage.getBatch());
            spriteCompass.setRotation((float) (-object.getOrientation() * 180 / Math.PI - 90));
            stage.getBatch().end();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
