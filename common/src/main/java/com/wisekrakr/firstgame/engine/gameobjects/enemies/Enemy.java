package com.wisekrakr.firstgame.engine.gameobjects.enemies;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.*;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class Enemy extends GameObject {

    private float direction;
    private float radius;
    private float health;
    private float speed;
    private float attackDistance;
    private float aggroDistance;

    private float damageTaken = 0;
    private boolean hit = false;
    private float healthPercentage;
    private float maxHealth;

    private float changeDirectionTime;

    public Enemy(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;
        maxHealth = this.health;

        setCollisionRadius(radius);

    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
       this.setDirection(this.getDirection() + (float) Math.PI);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            ((Player) subject).setHealth(((Player) subject).getHealth() - 15);
        }
        if (subject instanceof Bullet) {
            if (((Bullet) subject).isPlayerBullet()) {
                setHit(true);
                setDamageTaken(subject.getDamage());
                setHealth(getHealth() - subject.getDamage());
            }
        }
        if (subject instanceof HomingMissile){
            if(((HomingMissile) subject).isPlayerMissile()){
                setHit(true);
                setDamageTaken(subject.getDamage());
                setHealth(getHealth() - subject.getDamage());
            }
        }
        if (subject instanceof SpaceMine){
            if(((SpaceMine) subject).isPlayerMine()){
                setHit(true);
                setDamageTaken(subject.getDamage());
                setHealth(getHealth() - subject.getDamage());
            }
        }
    }

    private float healthInPercentages(){
        if (isHit()) {
            float z = getHealth() - getDamageTaken();
            healthPercentage = z / maxHealth * 100;
            healthPercentage /= 100;
        }
        return healthPercentage;
    }

    @Override
    public void overlappingObjects(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        Random random = new Random();
        if(subject instanceof Enemy){
            float angle = angleBetween(this, subject);
            if(distanceBetween(this, subject)<= this.getCollisionRadius() + subject.getCollisionRadius()) {
                setPosition(new Vector2(getPosition().x -= Math.cos(angle) * random.nextFloat() * 2.5,
                        getPosition().y -= Math.sin(angle) * random.nextFloat() * 2.5));
                setOrientation(-angle);
                setDirection(direction + (float) Math.PI);
            }else{
                toDelete.add(this);
            }
        }
    }

    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd){}
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd){}
    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {}

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
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

    public float getAggroDistance() {
        return aggroDistance;
    }

    public void setAggroDistance(float aggroDistance) {
        this.aggroDistance = aggroDistance;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getRadius() {
        return radius;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(float damageTaken) {
        this.damageTaken = damageTaken;
    }

    public float getChangeDirectionTime() {
        return changeDirectionTime;
    }

    public void setChangeDirectionTime(float changeDirectionTime) {
        this.changeDirectionTime = changeDirectionTime;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }

    @Override
    public Map<String, Object> getHealthProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("health", getHealth());

        return result;
    }

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("healthPercentage", healthInPercentages());

        return result;
    }

    @Override
    public Map<String, Object> getDamageTakenProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("damageTaken", damageTaken);

        return result;
    }

    @Override
    public Map<String, Object> getHitProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("hit", hit);

        return result;
    }

    @Override
    public Map<String, Object> getMaxHealthProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("maxHealth", maxHealth);

        return result;
    }
}
