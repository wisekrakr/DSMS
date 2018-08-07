package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
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
    private GameObject master;

    public SpaceMineObject(Vector2 initialPosition, double damage, double destructInterval, GameObject master) {
        super(GameObjectVisualizationType.SPACE_MINE, "BlinkyBoom", initialPosition, new MyBehavior(initialPosition, destructInterval));
        this.master = master;
        setCollisionRadius(3f);
        setDamage(damage);
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

        private double destructInterval;
        private float lastCreation;

        private MyBehavior(Vector2 initialPosition,  double destructInterval) {
            this.initialPosition = initialPosition;
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
