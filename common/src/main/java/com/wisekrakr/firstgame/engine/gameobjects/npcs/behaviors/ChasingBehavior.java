package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class ChasingBehavior extends AbstractBehavior {

    private GameObject target;

    public ChasingBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null && !(target instanceof WeaponObjectClass)) {
            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setDirection(angle);
            getContext().setOrientation(angle);

            if (getContext().getRadius() <= 8f) {
                getContext().setSpeed(GameHelper.generateRandomNumberBetween(50f, 75f));
            }else if (getContext().getRadius() >= 9f && getContext().getRadius() <= 15f){
                getContext().setSpeed(GameHelper.generateRandomNumberBetween(40f, 50f));
            }else if (getContext().getRadius() >= 16f && getContext().getRadius() <= 25f) {
                getContext().setSpeed(GameHelper.generateRandomNumberBetween(25f, 40f));
            }else {
                getContext().setSpeed(GameHelper.generateRandomNumberBetween(1f, 20f));
            }
        }
    }
}
