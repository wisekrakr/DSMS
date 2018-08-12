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
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.Set;

public class SpaceMineObject extends WeaponObjectClass {

    public SpaceMineObject(Vector2 initialPosition, double destructInterval, GameObject master) {
        super(GameObjectVisualizationType.SPACE_MINE, "BlinkyBoom", initialPosition, new MyBehavior(initialPosition, destructInterval), master);

        setCollisionRadius(3f);
        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));
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
