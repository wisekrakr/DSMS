package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.Set;

public class Spores extends HomingEnemyWeaponry {

    private float time;

    public Spores(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(GameObjectType.SPORE, name, initialPosition, space, direction, speed, radius, damage);

        setCollisionRadius(radius);
        setDamage(damage);
        setSpeed(speed);

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
            ((Player) subject).modifySpeed(0.5f);
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (target instanceof Player) {
            if (distanceBetween(this, target) <= 500) {

                float angle = angleBetween(this, target);

                // to make the chaser chase the player with less vigilance, divide cos and sin by 2
                setPosition(new Vector2(getPosition().x += Math.cos(angle), getPosition().y += Math.sin(angle)));

                setOrientation(angle);

                setDirection(angle);
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * getSpeed() * delta,
                getPosition().y + (float) Math.sin(getDirection()) * getSpeed() * delta)
        );
        setOrientation(getDirection());

        float destructTime = 8.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

    }

}
