package com.wisekrakr.firstgame;

import com.badlogic.gdx.audio.Sound;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

public class SoundManager{

    private Spaceship.SpecialPowerState powerState;
    private Spaceship.ShootingState shootingState;

    private MyAssetManager myAssetManager;

    public SoundManager(MyAssetManager myAssetManager) {

        this.myAssetManager = myAssetManager;


    }

    public void updateSounds(){
        if (shootingState == Spaceship.ShootingState.FIRING) {
            Sound pew = myAssetManager.assetManager.get("sound/photon1.wav", Sound.class);
            pew.play(0.1f);

        }
        if (shootingState == Spaceship.ShootingState.MISSILE_FIRING) {
            Sound pew = myAssetManager.assetManager.get("sound/photon2.wav", Sound.class);
            pew.play(0.1f);

        }
        if (shootingState == Spaceship.ShootingState.PLACE_MINE) {
            Sound boom = myAssetManager.assetManager.get("sound/mine_blowup.mp3", Sound.class);
            boom.play(0.1f);

        }
        if (powerState == Spaceship.SpecialPowerState.BOOSTING) {
            Sound acc = myAssetManager.assetManager.get("sound/acc1.mp3", Sound.class);
            acc.play(0.1f, 1.6f, 2f);

        }
    }

}
