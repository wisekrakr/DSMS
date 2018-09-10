package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;

public class ShootingBehavior extends Behavior {

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
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null && !(target instanceof WeaponObjectClass)) {

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(context.getSpeed());

            if (lastShot == 0){
                lastShot = clock;
            }

            if (fireRate != null) {
                if (clock - lastShot > fireRate) {
                    context.addGameObject(weapon.create(context.getPosition(), context.getOrientation(), 1f));
                    lastShot = clock;
                }
            }else {
                context.addGameObject(weapon.create(context.getPosition(), context.getOrientation(), 1f));
            }
        }
    }
}
