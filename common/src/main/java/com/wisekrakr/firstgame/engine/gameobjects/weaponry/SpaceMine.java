package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpaceMine extends GameObject {

    private float speed;
    private float direction;
    private float radius;
    private float time;
    private float destructTime;
    private double damage;
    private boolean isDestruct;
    private float areaOfEffect;

    private boolean playerMine;
    private boolean enemyMine;

    /*
    When initializing a SpaceMine in any other class, don't forget to setPlayer- or EnemyMine and setAreaOfEffect.
     */

    public SpaceMine(String name, Vector2 initialPosition, float direction, float speed, float radius, float areaOfEffect, double damage) {
        super(GameObjectVisualizationType.SPACE_MINE, name, initialPosition);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;
        this.damage = damage;
        this.areaOfEffect = areaOfEffect;

        setCollisionRadius(radius + areaOfEffect);

        isDestruct = false;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (playerMine) {
            setAreaOfEffect(50f);
            if (subject instanceof Enemy) {
                toDelete.add(this);
                subject.setHealth(subject.getHealth() - getDamage());
                setDestruct(true);
                initDebris(toDelete, toAdd);
            }
            if (subject instanceof Minion) {
                if (((Minion) subject).isEnemyMinion()) {
                    toDelete.add(this);
                    subject.setHealth(subject.getHealth() - getDamage());
                    setDestruct(true);
                    initDebris(toDelete, toAdd);
                }
            }
        }

        if (enemyMine){
            setAreaOfEffect(30f);
            if (subject instanceof Player){
                toDelete.add(this);
                subject.setHealth(subject.getHealth() - getDamage());
                initDebris(toDelete, toAdd);
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (playerMine){
            setDestructTime(20f);
        }
        if (enemyMine){
            setDestructTime(10f);
        }
        destructTime = getDestructTime();
        time += delta;
        if(time >= destructTime){
            if(!(isDestruct)) {
                toDelete.add(this);
                setDestruct(true);
            }else {
                setDestruct(false);
            }
        }

    }

    public boolean isPlayerMine() {
        return playerMine;
    }

    public void setPlayerMine(boolean playerMine) {
        this.playerMine = playerMine;
    }

    public boolean isEnemyMine() {
        return enemyMine;
    }

    public void setEnemyMine(boolean enemyMine) {
        this.enemyMine = enemyMine;
    }

    public float getDestructTime() {
        return destructTime;
    }

    public void setDestructTime(float destructTime) {
        this.destructTime = destructTime;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public float getRadius() {
        return radius;
    }

    public boolean isDestruct() {
        return isDestruct;
    }

    public void setDestruct(boolean destruct) {
        isDestruct = destruct;
    }

    public float getAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(float areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }


    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);
        result.put("damage", damage);
        result.put("isDestruct", isDestruct);

        return result;
    }
}
