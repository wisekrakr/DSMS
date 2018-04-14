package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.AutonomousWeaponsEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.BulletEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.LaserBeamEnemy;

import java.util.Set;

public class Shield extends AutonomousWeaponsEnemy {

    private float radius;
    private float direction;
    private int damage;
    private float time;
    private static final float SHIELD_TIME = 20;

    public Shield(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius, int damage) {
        super(name, initialPosition, space, direction, radius, damage);
        this.radius = radius;
        this.direction = direction;
        this.damage = damage;

        setCollisionRadius(radius);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Enemy){
            subject.setHealth(subject.getHealth() - getDamage());
            ((Enemy) subject).setDirection(((Enemy) subject).getDirection() + (float)Math.PI);
            setHealth(getHealth() - 10);
        }
        if(subject instanceof BulletEnemy){
            toDelete.add(subject);
        }
        if(subject instanceof AutonomousWeaponsEnemy){
            toDelete.add(subject);
        }
        if(subject instanceof LaserBeamEnemy){
            toDelete.add(subject);
        }
    }

    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            setPosition(new Vector2(subject.getPosition().x , subject.getPosition().y ));
        }
    }

    public float getTime() {
        return time;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        time += delta;

        if(this.getTime() >= SHIELD_TIME){
            toDelete.add(this);
            time =0;
        }

    }

}
