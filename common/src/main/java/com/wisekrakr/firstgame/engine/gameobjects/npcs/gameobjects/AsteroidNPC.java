package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.RotatingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ExplodeAndLeaveDebrisBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.SplashBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.Set;

public class AsteroidNPC extends NonPlayerCharacter {

    public AsteroidNPC(Vector2 initialPosition, float initialRadius) {
        super(GameObjectVisualizationType.ASTEROID, "Asteroid", initialPosition, new MyBehavior(initialPosition, initialRadius));
        setCollisionRadius(initialRadius);
        setHealth(getCollisionRadius());
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject.getClass() != this.getClass()){
            if (subject instanceof WeaponObjectClass) {
                float asteroidParts = GameHelper.randomGenerator.nextInt(2) + 1;
                for (int i = 0; i < asteroidParts; i++) {
                    toAdd.add(new AsteroidNPC(this.getPosition(), GameHelper.generateRandomNumberBetween(0f, getCollisionRadius())));
                }
                toDelete.add(this);
            }else if (subject instanceof NonPlayerCharacter || subject instanceof Player){
                this.setDirection((float) (this.getDirection() + Math.PI));
            }
        }
    }

    private static class MyBehavior extends Behavior{

        private Vector2 initialPosition;
        private float initialRadius;

        public MyBehavior(Vector2 initialPosition, float initialRadius) {
            this.initialPosition = initialPosition;
            this.initialRadius = initialRadius;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (context.getRadius() <= 0.5f){
                context.removeGameObject(context.thisObject());
            }

            if (!(context.existingSubBehavior() instanceof RotatingBehavior)){
                context.pushSubBehavior(new RotatingBehavior(GameHelper.generateRandomNumberBetween(5f, 25f)));

            }else if (context.getHealth() <= 0){
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(7f));
            }
        }
    }
}
