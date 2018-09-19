package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;

public class ShootingBehavior extends AbstractBehavior {

    private GameObjectFactory<?> weapon;
    private Float fireRate;
    private GameObject target;
    private float lastShot;

    public ShootingBehavior(GameObjectFactory<?> weapon, Float fireRate, GameObject target) {
        this.weapon = weapon;
        this.fireRate = fireRate;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null && !(target instanceof WeaponObjectClass)) {

            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setDirection(angle);
            getContext().setOrientation(angle);
            getContext().setSpeed(getContext().getSpeed());

            if (lastShot == 0){
                lastShot = clock;
            }

            if (fireRate != null) {
                if (clock - lastShot > fireRate) {
                    getContext().addGameObject(weapon.create(getContext().getPosition(), getContext().getOrientation(), 1f));
                    lastShot = clock;
                }
            }else {
                getContext().addGameObject(weapon.create(getContext().getPosition(), getContext().getOrientation(), 1f));
            }
        }
    }
}
