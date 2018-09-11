package com.wisekrakr.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import javafx.scene.media.VideoTrack;

public class MyAssetManager implements Disposable {

    public AssetManager assetManager = new AssetManager();
    /*
     * All the Sounds loaded in the the AssetManager
     */
    public void loadSounds() {
        assetManager.load("sound/laser.mp3", Sound.class);
        assetManager.load("sound/mothership1.wav", Sound.class);
        assetManager.load("sound/photon1.wav", Sound.class);
        assetManager.load("sound/photon2.wav", Sound.class);
        assetManager.load("sound/acc1.mp3", Sound.class);
//        assetManager.load("sound/mine_blowup.mp3", Sound.class);

        assetManager.finishLoading();
    }
    /*
     * All the Fonts loaded in the the AssetManager
     */
    public void loadFonts() {
        assetManager.load("font/default.fnt", BitmapFont.class);
        assetManager.load("font/myFont.fnt", BitmapFont.class);
        assetManager.load("font/myFontBlack.fnt", BitmapFont.class);
        assetManager.load("font/achievementFont.fnt", BitmapFont.class);
        assetManager.load("font/gamerFont.fnt", BitmapFont.class);
        assetManager.finishLoading();
    }
    /*
     * All the Videos loaded in the the AssetManager
     */
    public void loadVideos() {
        assetManager.load("video/test.avi", VideoTrack.class);
        assetManager.finishLoading();
    }
    /*
     * All the Skins loaded in the the AssetManager
     */
    public void loadSkins() {
        SkinLoader.SkinParameter skinParameterUISkin = new SkinLoader.SkinParameter("font/uiskin.atlas");
        assetManager.load("font/uiskin.json", Skin.class, skinParameterUISkin);
        SkinLoader.SkinParameter skinParameterFlatEarthSkin = new SkinLoader.SkinParameter("font/flat-earth-ui.atlas");
        assetManager.load("font/flat-earth-ui.json", Skin.class, skinParameterFlatEarthSkin);
        assetManager.finishLoading();
    }
    /*
     * All the Textures loaded in the the AssetManager
     */
    public void loadTextures() {
        assetManager.load("background/bg1.png", Texture.class);
        assetManager.load("texture/pausedPic2.png", Texture.class);
        assetManager.load("background/stars.jpg", Texture.class);
        assetManager.load("texture/crosshair.png", Texture.class);
        assetManager.load("texture/cursor.png", Texture.class);
        assetManager.load("texture/intrologo.jpg", Texture.class);
        assetManager.load("texture/healthbar.png", Texture.class);
        assetManager.load("texture/healthbarFore.png", Texture.class);
        assetManager.load("texture/healthbarBack.png", Texture.class);
        assetManager.load("texture/healthbarBorder.png", Texture.class);
        assetManager.load("texture/compass.png", Texture.class);
        assetManager.load("sprites/spaceship.png", Texture.class);
        assetManager.load("sprites/spaceship_fly.png", Texture.class);
        assetManager.load("sprites/spaceship_boost.png", Texture.class);
        assetManager.load("sprites/bullet_small.png", Texture.class);
        assetManager.load("sprites/missile_default.png", Texture.class);
        assetManager.load("sprites/asteroid_big.png", Texture.class);
        assetManager.load("sprites/asteroid_medium.png", Texture.class);
        assetManager.load("sprites/asteroid_small.png", Texture.class);
        assetManager.load("sprites/powerup_health.png", Texture.class);
        assetManager.load("sprites/powerup_shield.png", Texture.class);
        assetManager.load("sprites/human_mothership.png", Texture.class);
        assetManager.load("sprites/ssDodger.png", Texture.class);
        assetManager.load("sprites/ssEls.png", Texture.class);
        assetManager.load("sprites/ssHomer.png", Texture.class);
        assetManager.load("sprites/ssMutator.png", Texture.class);
        assetManager.load("sprites/ssPlane.png", Texture.class);
        assetManager.load("sprites/compass_small.png", Texture.class);

        assetManager.finishLoading();
    }

    /*
     * All the Music loaded in the the AssetManager
     */
    public void loadMusic() {
        assetManager.load("music/space_explorers.mp3", Music.class);
        assetManager.load("music/crazy2.mp3", Music.class);
        assetManager.load("music/creepy1.mp3", Music.class);
        assetManager.load("music/scary1.mp3", Music.class);
        assetManager.load("music/crazy1.mp3", Music.class);

        assetManager.finishLoading();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }


/*
    public void loadParticleEffects(){
        ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
        //pep.atlasFile = "images/images.pack";
        //assetManager.load("particles/sparks.pe", ParticleEffect.class, pep);
        assetManager.finishLoading();
    }
*/


}
