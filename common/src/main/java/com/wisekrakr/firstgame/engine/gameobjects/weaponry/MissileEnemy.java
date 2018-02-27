package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Weapons;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;

import java.util.Set;

public class MissileEnemy extends Weapons {
    private float rotationSpeed;
    private float direction;
    private float radius;
    private float time;


    private static final float DEFAULT_MISSILE_SPEED = 300;

    public MissileEnemy(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius) {
        super(name, initialPosition, space, direction, radius);
        this.direction = direction;
        this.radius = radius;

        setCollisionRadius(4);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - randomDamageCountMissile());
        }
    }

    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Player) {

                float angle = angleBetween(this, subject);

                // to make the chaser chase the player with less vigilance, divide cos and sin by 2
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle) , getPosition().y +=  Math.sin(angle) ));

                setOrientation(angle);

                setDirection(angle);

        }
    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_MISSILE_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_MISSILE_SPEED * delta)
        );
        setOrientation(direction);

        float destructTime = 5.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
}
