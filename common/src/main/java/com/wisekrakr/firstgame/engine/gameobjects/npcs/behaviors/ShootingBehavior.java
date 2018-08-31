package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.SpaceMineObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;

public class ShootingBehavior extends Behavior {
    private GameObjectFactory<?> weapon;
    private GameObject target;

    public ShootingBehavior(GameObjectFactory<?> weapon, GameObject target) {
        this.weapon = weapon;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null && !(target instanceof WeaponObjectClass)) {

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(GameHelper.generateRandomNumberBetween(context.getSpeed(), 75f));
            context.addGameObject(weapon.create(context.getPosition(), context.getOrientation(), 1));

        }
    }
}
