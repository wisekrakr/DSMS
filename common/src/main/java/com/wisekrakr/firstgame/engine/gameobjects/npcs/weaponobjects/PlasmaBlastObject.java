package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.CirclingBehavior;

import java.util.Set;

public class PlasmaBlastObject extends WeaponObjectClass {

    private float shotLeftOver;

    public PlasmaBlastObject(Vector2 initialPosition, Behavior initialBehavior) {
        super(GameObjectVisualizationType.SPACE_MINE, "PlasmaBlast", initialPosition, new MyBehavior(initialPosition, initialBehavior));
        setCollisionRadius(3f);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
        }
    }

    private static class MyBehavior extends Behavior {

        private Vector2 initialPosition;
        private Behavior initialBehavior;

        public MyBehavior(Vector2 initialPosition, Behavior initialBehavior) {
            this.initialPosition = initialPosition;
            this.initialBehavior = initialBehavior;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {


        }
    }
}
