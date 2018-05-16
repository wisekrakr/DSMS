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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import javafx.scene.media.VideoTrack;

public class MyAssetManager implements Disposable{

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
        assetManager.load("sound/mine_blowup.mp3", Sound.class);

        assetManager.finishLoading();
    }
    /*
     * All the Fonts loaded in the the AssetManager
     */
    public void loadFonts() {
        assetManager.load("font/myFont.fnt", BitmapFont.class);
        assetManager.load("font/myFontBlack.fnt", BitmapFont.class);
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
        SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("font/uiskin.atlas");
        assetManager.load("font/uiskin.json", Skin.class, skinParameter);
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
        assetManager.load("texture/intrologo.jpg", Texture.class);

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
