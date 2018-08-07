package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.CirclingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.TestBehavior;

import java.util.Set;

public class PlasmaBlastObject extends WeaponObjectClass {

    private GameObject master;

    public PlasmaBlastObject(Vector2 initialPosition, float initialDirection, double damage, double destructInterval, GameObject master) {
        super(GameObjectVisualizationType.ASTEROID, "PlasmaBlast", initialPosition, new MyBehavior(initialPosition, initialDirection, destructInterval));
        this.master = master;
        setCollisionRadius(3f);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof NonPlayerCharacter){
            if (subject != master) {
                subject.setHealth(subject.getHealth() - this.getDamage());
                toDelete.add(this);
                int debrisParts = GameHelper.randomGenerator.nextInt(10)+1;
                for(int i = 0; i < debrisParts; i++) {
                    toAdd.add(new WeaponDebris(this.getPosition(), this));
                }
            }
        }
    }

    private static class MyBehavior extends Behavior {

        private final Vector2 initialPosition;
        private float initialDirection;
        private double destructInterval;

        public MyBehavior(Vector2 initialPosition, float initialDirection, double destructInterval) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
            this.destructInterval = destructInterval;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (!(context.existingSubBehavior() instanceof TestBehavior)){
                context.pushSubBehavior(new TestBehavior(initialDirection, destructInterval));
            }
        }
    }
}
