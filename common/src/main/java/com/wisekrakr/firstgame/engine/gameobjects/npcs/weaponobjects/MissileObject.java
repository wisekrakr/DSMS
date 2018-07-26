package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.BulletBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.MissileBehavior;

public class MissileObject extends NonPlayerCharacter{
    public MissileObject(Vector2 initialPosition, float initialDirection, GameObject target) {
        super(GameObjectVisualizationType.MISSILE, "Misselito", initialPosition, new MyBehavior(initialPosition, initialDirection, target));
    }

    private static class MyBehavior extends Behavior{

        private final Vector2 initialPosition;
        private float initialDirection;
        private GameObject target;


        public MyBehavior(Vector2 initialPosition, float initialDirection, GameObject target) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

           if (target instanceof Player) {
               context.pushSubBehavior(new MissileBehavior(initialDirection, target));
           }
        }
    }
}
