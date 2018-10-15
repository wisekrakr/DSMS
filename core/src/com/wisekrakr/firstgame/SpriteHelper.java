package com.wisekrakr.firstgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;

public class SpriteHelper {
    private SpriteHelper() {
        // fake constructor to prevent instantiation
    }

    public static void drawSpriteForGameObject(MyAssetManager myAssetManager, String spritePath, PhysicalObjectSnapshot object, Batch targetBatch, Float scale) {
        Texture texture = myAssetManager.assetManager.get(spritePath);
        Sprite sprite = new Sprite(texture);

        float rotation = (float) (object.getOrientation() * 180 / Math.PI - 90);

        if (scale != null) {
            sprite.setScale(scale);
        }
        sprite.setRotation(rotation);
        sprite.setPosition(object.getPosition().x - sprite.getWidth() / 2, object.getPosition().y - sprite.getHeight() / 2);
        //sprite.setOriginCenter();


        sprite.draw(targetBatch);
    }
}
