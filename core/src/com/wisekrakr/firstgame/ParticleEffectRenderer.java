package com.wisekrakr.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class ParticleEffectRenderer {

    private ParticleEffect particleEffect;
    private Stage stage;
    private OrthographicCamera camera;

    public ParticleEffectRenderer(OrthographicCamera camera, Stage stage) {
        this.camera = camera;
        this.stage = stage;

        particleEffect = new ParticleEffect();
    }

    private Float radius(SpaceSnapshot.GameObjectSnapshot object){
        return (Float) object.extraProperties().get("radius");
    }

    public ParticleEffect exhaustEffect(SpaceSnapshot.GameObjectSnapshot object){

        particleEffect.load(Gdx.files.internal("particles/exhaust.party"), Gdx.files.internal("particles"));
        particleEffect.getEmitters().first().setPosition(object.getPosition().x, object.getPosition().y);
        particleEffect.start();

        return particleEffect;
    }

    public void updateEffect(float delta){

        particleEffect.update(delta);

        stage.getBatch().begin();
        particleEffect.draw(stage.getBatch());
        stage.getBatch().end();

        if (particleEffect.isComplete()){
            particleEffect.reset();
        }

    }
}
