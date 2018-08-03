package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.IdleBehavior;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpaceMineObject extends WeaponObjectClass {
    private float shotLeftOver;

    public SpaceMineObject(Vector2 initialPosition, float dropMineInterval, double damage, double destructInterval) {
        super(GameObjectVisualizationType.SPACE_MINE, "BlinkyBoom", initialPosition, new MyBehavior(initialPosition, dropMineInterval, destructInterval));
        setCollisionRadius(3f);
        setDamage(damage);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player){
            subject.setHealth(subject.getHealth() - getDamage());
            toDelete.add(this);
        }
    }

    private static class MyBehavior extends Behavior{

        private Vector2 initialPosition;
        private float dropMineInterval;
        private double destructInterval;
        private float lastCreation;

        private MyBehavior(Vector2 initialPosition, float dropMineInterval, double destructInterval) {
            this.initialPosition = initialPosition;
            this.dropMineInterval = dropMineInterval;
            this.destructInterval = destructInterval;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {
            context.pushSubBehavior(new IdleBehavior());

            lastCreation += delta;
            if (lastCreation >= destructInterval){
                context.removeGameObject(context.thisObject());
            }
        }
    }

}
