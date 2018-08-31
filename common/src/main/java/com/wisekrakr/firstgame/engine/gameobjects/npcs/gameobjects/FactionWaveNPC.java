package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;

public class FactionWaveNPC extends NonPlayerCharacter {
    private Behavior desiredBehavior;

    public FactionWaveNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.ENEMY_CHASER, "Wave Minion", initialPosition);

        setCollisionRadius(GameHelper.generateRandomNumberBetween(5f, 8f));
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));

        rootBehavior(new MyBehavior());
    }

    public void cruising() {
        desiredBehavior = new CruisingBehavior(4f);
    }

    public void selfDestruct(){
        desiredBehavior = new ExplodeAndLeaveDebrisBehavior(13);
    }

    public void shootAt(GameObject target){
        desiredBehavior = new ShootingBehavior(new BulletObject(getPosition(), getOrientation(), 3f, this, 200f), target);
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
