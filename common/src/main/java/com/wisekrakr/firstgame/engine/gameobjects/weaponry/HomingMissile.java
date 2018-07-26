package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 *To set a missile as a weapon for either enemy or player, create a HomingMissile, then use enableMissile method, and also
 *set the speed for the missile ===> do this all in the class you want to use a missile in/for.
 */
public class HomingMissile extends GameObject {

    private float radius;
    private int damage;
    private float direction;
    private float speed;
    private float time;
    private float attackDistance;
    private boolean canMissile;

    private boolean playerMissile;
    private boolean enemyMissile;

    private float missileSpeed;

    public HomingMissile(Vector2 initialPosition, float direction, float speed, float radius, int damage, boolean canMissile) {
        super(GameObjectVisualizationType.MISSILE, "HomingMissile", initialPosition);
        this.radius = radius;
        this.damage = damage;
        this.direction = direction;
        this.speed = speed;
        this.canMissile = canMissile;

        setCollisionRadius(radius);
        setDamage(damage);
        setSpeed(speed);
        setAttackDistance(125f);
    }

    public void missileEnable(GameObject subject){
        if (canMissile){
            if (subject instanceof Player){
                setPlayerMissile(true);
            }
            if (subject instanceof Enemy){
                setEnemyMissile(true);
            }
        }
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject != null) {
            if (isPlayerMissile()) {
                if (subject instanceof Enemy)  {
                    toDelete.add(this);
                    subject.setHealth(subject.getHealth() - getDamage());
                }
                if (subject instanceof NonPlayerCharacter){
                    toDelete.add(this);
                    subject.setHealth(subject.getHealth() - getDamage());
                }
                if (subject instanceof Minion) {
                    if (((Minion) subject).isEnemyMinion()) {
                        toDelete.add(this);
                        subject.setHealth(subject.getHealth() - getDamage());
                    }
                }
            }
            if (isEnemyMissile()) {
                if (subject instanceof Player) {
                    toDelete.add(this);
                    subject.setHealth(subject.getHealth() - MissileMechanics.determineMissileDamage());
                    if (((Player) subject).isKilled()) {
                        ((Player) subject).setKillerName((String) subject.getExtraSnapshotProperties().get("killedBy"));
                    }
                }
            }
            if (subject instanceof Asteroid) {
                toDelete.add(this);
                toDelete.add(subject);
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target != null) {
            if (isPlayerMissile()) {
                if (target instanceof Enemy || target instanceof NonPlayerCharacter) {
                    if (GameHelper.distanceBetween(this, target) <= getAttackDistance()) {
                        float angle = GameHelper.angleBetween(this, target);
                        setOrientation(angle);
                        setDirection(angle);
                    }
                }
            }

            if (isEnemyMissile()) {
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(this, target) <= getAttackDistance()) {
                        float angle = GameHelper.angleBetween(this, target);
                        setOrientation(angle);
                        setDirection(angle);
                    }
                }
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float destructTime = 5.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * missileSpeed * delta,
                getPosition().y + (float) Math.sin(direction) * missileSpeed * delta)
        );
        setOrientation(direction);

    }

    public boolean isPlayerMissile() {
        return playerMissile;
    }

    public void setPlayerMissile(boolean playerMissile) {
        this.playerMissile = playerMissile;
    }

    public boolean isEnemyMissile() {
        return enemyMissile;
    }

    public void setEnemyMissile(boolean enemyMissile) {
        this.enemyMissile = enemyMissile;
    }

    public float getMissileSpeed() {
        return missileSpeed;
    }

    public void setMissileSpeed(float missileSpeed) {
        this.missileSpeed = missileSpeed;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(float attackDistance) {
        this.attackDistance = attackDistance;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);
        result.put("damage", damage);

        return result;
    }
}
