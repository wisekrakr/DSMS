package com.wisekrakr.firstgame.engine;

import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BulletPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.MissilePlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Shield;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpaceEngine {
    private final Object monitor = new Object();
    private final float minX;
    private final float minY;
    private final float width;
    private final float height;
    private Set<GameObject> gameObjects = new HashSet<GameObject>();


    public SpaceEngine(float minX, float minY, float width, float height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    public void addGameObject(GameObject object) {
        synchronized (monitor) {
            gameObjects.add(object);
        }
    }

    public void removeGameObject(GameObject object) {
        synchronized (monitor) {
            gameObjects.remove(object);
        }
    }

    public void forObject(GameObject object, GameObjectHandler action) {
        synchronized (monitor) {
            action.doIt(object);
        }
    }

    public void forAllObjects(GameObjectHandler action) {
        synchronized (monitor) {
            for (GameObject object : gameObjects) {
                action.doIt(object);
            }
        }
    }

    public interface GameObjectHandler {
        void doIt(GameObject target);
    }

    private boolean collision(GameObject object1, GameObject object2) {
        return
                Math.sqrt(
                        (((object1.getPosition().x) - (object2.getPosition().x)))
                                * ((object1.getPosition().x) - (object2.getPosition().x))
                                + ((object1.getPosition().y) - (object2.getPosition().y))
                                * ((object1.getPosition().y) - (object2.getPosition().y)))
                        < (object1.getCollisionRadius() + object2.getCollisionRadius());
    }

    private void nearestTarget(GameObject subject, Set<GameObject> targets, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        for (GameObject target : targets) {
            if (Math.abs(target.getPosition().x - subject.getPosition().x) < Math.abs(target.getPosition().x - subject.getPosition().x)) {
                if (Math.abs(target.getPosition().y - subject.getPosition().y) < Math.abs(target.getPosition().y - subject.getPosition().y)) {
                    subject.attackTarget(target, toDelete, toAdd);
                }
            }
        }
    }

    public SpaceSnapshot makeSnapshot() {
        synchronized (monitor) {
            List<SpaceSnapshot.GameObjectSnapshot> gameObjectSnapshots = new ArrayList<SpaceSnapshot.GameObjectSnapshot>();

            for (GameObject object : gameObjects) {
                gameObjectSnapshots.add(object.snapshot());
            }

            return new SpaceSnapshot("Bla", 1235, gameObjectSnapshots);
        }
    }

    public void elapseTime(final float delta) {
        synchronized (monitor) {
            Set<GameObject> toDelete = new HashSet<GameObject>();
            Set<GameObject> toAdd = new HashSet<GameObject>();

            for (GameObject target : gameObjects) {
                target.elapseTime(delta, toDelete, toAdd);
            }

            for (GameObject target : gameObjects) {
                if (!toDelete.contains(target)) {
                    if (target.getPosition().x < minX || target.getPosition().x - minX > width ||
                            target.getPosition().y < minY || target.getPosition().y - minY > height) {
                        target.signalOutOfBounds(toDelete, toAdd);
                    }
                }
            }
/**
 * See if any gameobjects collide with each other, than proceed with the collide method of GameObject extended classes
 */
            for (GameObject target : gameObjects) {
                if (!toDelete.contains(target)) {
                    for (GameObject subject : gameObjects) {
                        if (!toDelete.contains(subject)) {
                            if (target != subject) {
                                if (collision(target, subject)) {
                                    target.collide(subject, toDelete, toAdd);

                                }
                            }
                        }
                    }
                }
            }


/**
 * In this section gameobjects(enemy package) calculate how far they are of each other and they attack in their different ways
 */

            for (GameObject subject : gameObjects) {
                if (subject instanceof Enemy) {
                    for (GameObject target : gameObjects) {
                        if (target instanceof Spaceship) {
                            subject.getClosestTarget(target, toDelete, toAdd);
                            if (target != subject) {
                                subject.targetSpotted(target, toDelete, toAdd);
                                subject.attackTarget(target, toDelete, toAdd);
                            }
                        }
                    }
                }
            }
/**
 * In this section gameobjects( weaponry package) calculate how far they are of each other and they attack in their different ways
 */

            for (GameObject subject : gameObjects) {
                if (subject instanceof HomingWeaponsEnemy) {
                    for (GameObject target : gameObjects) {
                        if (target instanceof Spaceship) {
                            if (target != subject ) {
                                subject.attackTarget(target, toDelete, toAdd);
                            }
                        }
                    }
                }
            }

            /**
             * In this section gameobjects( player weaponry ) calculate how far they are of each other and they attack in their different ways
             */

            for (GameObject subject : gameObjects) {
                if (subject instanceof MissilePlayer) { //change this to PlayerWeapons when we make a PlayerWeapons Parent Class
                    for (GameObject target : gameObjects) {
                        if (target instanceof Enemy) {
                            //nearestTarget(subject, gameObjects, toDelete, toAdd);
                            subject.getClosestTarget(target, toDelete, toAdd);
                        }
                    }
                }
            }

            /**
             * Scoring system
             */
            for (GameObject player : gameObjects) {
                if (player instanceof Player) {
                    for (GameObject enemy : gameObjects) {
                        if (enemy instanceof Enemy) {
                            for(GameObject subject: gameObjects){
                                if(subject instanceof BulletPlayer){
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                                if(subject instanceof MissilePlayer){
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                                if(subject instanceof Shield){
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                            }
                        }
                    }
                }
            }



            for (GameObject gameObject : toDelete) {
                gameObjects.remove(gameObject);
            }

            for (GameObject gameObject : toAdd) {
                gameObjects.add(gameObject);
            }
        }
    }
}
