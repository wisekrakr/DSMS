package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class BackgroundStars extends Actor{

    private Texture texture;
    private int scroll;

    private final int LAYER_SPEED_DIFFERENCE = 2;

    float x,y,width,heigth,scaleX,scaleY;
    int originX, originY,rotation,srcX,srcY;
    boolean flipX,flipY;

    private int speed;

    public BackgroundStars(Texture texture) {

        this.texture = texture;
        texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        scroll = 0;
        speed = 0;

        x = y = originX = originY = rotation = srcY = 0;
        width =  Gdx.graphics.getWidth();
        heigth = Gdx.graphics.getHeight();
        scaleX = scaleY = 1;
        flipX = flipY = false;

    }



    public void setSpeed(int newSpeed){
        this.speed = newSpeed;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        scroll+=speed;

        srcX = scroll + this.LAYER_SPEED_DIFFERENCE *scroll;
        batch.draw(texture, x, y, originX, originY, width, heigth,scaleX,scaleY,rotation,srcX,srcY,(int)texture.getWidth(),(int)texture.getHeight(),flipX,flipY);

    }
}
