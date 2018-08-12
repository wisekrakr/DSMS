package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ExplodeAndLeaveDebrisBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ShootingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;

public class Protector extends NonPlayerCharacter {
    private Behavior desiredBehavior;

    public Protector(Vector2 initialPosition) {
        super(GameObjectVisualizationType.PEST, "Protector", initialPosition);

        setCollisionRadius(14f);
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));

        rootBehavior(new MyBehavior());
    }

    public void aimFor(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void protect(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void comeHome(Vector2 position) {
        desiredBehavior = new ExplodeAndLeaveDebrisBehavior(123);
    }

    private class MyBehavior extends Behavior {
        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {
            if (desiredBehavior != null) {
                context.pushSubBehavior(desiredBehavior);
                desiredBehavior = null;
            }

            if (context.getHealth() <= 0){
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }
        }
    }
}
