package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.HomingMissileBehavior;

import java.util.Set;

public class MissileObject extends WeaponObjectClass{

    private GameObject master;
    public MissileObject(Vector2 initialPosition, float initialDirection, double damage, double destructInterval, GameObject target, GameObject master) {
        super(GameObjectVisualizationType.MISSILE, "Misselito", initialPosition,
                new MyBehavior(initialPosition, initialDirection, destructInterval, target));
        this.master = master;
        this.setCollisionRadius(2f);
        this.setDamage(damage);

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof NonPlayerCharacter || subject instanceof Player){
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

    private static class MyBehavior extends Behavior{

        private Vector2 initialPosition;
        private float initialDirection;
        private double destructInterval;
        private GameObject target;

        public MyBehavior(Vector2 initialPosition, float initialDirection, double destructInterval, GameObject target) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
            this.destructInterval = destructInterval;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (!(context.existingSubBehavior() instanceof HomingMissileBehavior)) {
                context.pushSubBehavior(new HomingMissileBehavior(initialDirection, destructInterval, target));
            }
        }
    }
}
