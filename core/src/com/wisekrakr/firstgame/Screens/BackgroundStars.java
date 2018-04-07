package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.wisekrakr.firstgame.Constants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class BackgroundStars extends Actor{

    private Texture texture;
    private float scroll;

    private final float LAYER_SPEED_DIFFERENCE = 1;

    private float x,y,width,heigth,scaleX,scaleY;
    public float originX, originY,rotation,srcX,srcY;
    private boolean flipX,flipY;

    private float speed;

    public BackgroundStars(Texture texture) {

        this.texture = texture;
        //texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        scroll = 0;
        speed = 0;

        x = y = originX = originY= rotation = srcY = srcX = 0;
        width = Constants.WORLD_WIDTH;
        heigth = Constants.WORLD_HEIGHT;
        scaleX = scaleY = 1f;
        flipX = flipY = false;

    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float newSpeed){
        this.speed = newSpeed;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public float getOriginX() {
        return originX;
    }

    @Override
    public void setOriginX(float originX) {
        this.originX = originX;
    }

    @Override
    public float getOriginY() {
        return originY;
    }

    @Override
    public void setOriginY(float originY) {
        this.originY = originY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        scroll+=speed;

        srcX = scroll + this.LAYER_SPEED_DIFFERENCE *scroll;

        batch.draw(texture, x, y, originX, originY, width, heigth,scaleX,scaleY,rotation,(int)srcX,(int)srcY,texture.getWidth(),texture.getHeight(),flipX,flipY);


    }



}
