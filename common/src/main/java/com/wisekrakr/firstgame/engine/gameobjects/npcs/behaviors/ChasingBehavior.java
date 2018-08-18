package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class ChasingBehavior extends Behavior {

    private GameObject target;

    public ChasingBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null && !(target instanceof WeaponObjectClass)) {
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);

            if (context.getRadius() <= 8f) {
                context.setSpeed(GameHelper.generateRandomNumberBetween(50f, 75f));
            }else if (context.getRadius() >= 9f && context.getRadius() <= 15f){
                context.setSpeed(GameHelper.generateRandomNumberBetween(40f, 50f));
            }else if (context.getRadius() >= 16f && context.getRadius() <= 25f) {
                context.setSpeed(GameHelper.generateRandomNumberBetween(25f, 40f));
            }else {
                context.setSpeed(GameHelper.generateRandomNumberBetween(1f, 20f));
            }
        }
    }
}
