package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MyAssetManager {

    public final com.badlogic.gdx.assets.AssetManager assetManager = new com.badlogic.gdx.assets.AssetManager();

    private final String laserSound = "laser.mp3";

    private final String myFont = "myFont.fnt";
    private final String myFontBlack = "myFontBlack.fnt";

    private final String sparksPE ="particles/sparks.pe";

    private final String skin = "uiskin.json";

    public void loadSounds(){
        assetManager.load(laserSound, Sound.class);
    }

    public void loadFonts(){
        assetManager.load(myFont, BitmapFont.class);
        assetManager.load(myFontBlack, BitmapFont.class);
    }

    public void loadParticleEffects(){
        ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
        //pep.atlasFile = "images/images.pack";
        //assetManager.load(sparksPE, ParticleEffect.class, pep);

    }

    public void loadSkin(){
        SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("uiskin.atlas");
        assetManager.load(skin, Skin.class, skinParameter);
    }
}
