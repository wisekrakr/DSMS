package com.wisekrakr.firstgame.engine.gameobjects.spaceobjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.FaceHuggingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.Protector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Treasure extends GameObject {

    private float speed = GameHelper.generateRandomNumberBetween(20f, 30f);
    private Float timeCounter;

    public Treasure(Vector2 initialPosition) {
        super(GameObjectVisualizationType.POWERUP_MISSILE, "Treasure", initialPosition);
        setCollisionRadius(20f);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", getCollisionRadius());
        result.put("speed", speed);

        return result;
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            toDelete.add(this);
            int treasure = MathUtils.random(3,3);
            switch (treasure){
                case 1:
                    ((Player) subject).setMissileAmmoCount(((Player) subject).getMissileAmmoCount() + 10);
                    break;
                case 2:
                    subject.setHealth(subject.getHealth() + 20);
                    break;
                case 3:
                    Protector protector = new Protector(new Vector2(
                            subject.getPosition().x + (subject.getCollisionRadius() * 2),
                            subject.getPosition().y + (subject.getCollisionRadius() * 2)));
                    protector.setDesiredBehavior(new FaceHuggingBehavior(6f, subject));
                    toAdd.add(protector);
                    break;
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (timeCounter == null){
            timeCounter = clock;
        }

        if (clock - timeCounter > 5) {
            float angle = GameHelper.randomDirection();
            setPosition(new Vector2(getPosition().x + (float) Math.cos(angle) * speed * delta,
                    getPosition().y + (float) Math.sin(angle) * speed * delta)
            );
            setDirection(angle);
            setOrientation(angle);

            timeCounter = clock;
        }

    }
}
