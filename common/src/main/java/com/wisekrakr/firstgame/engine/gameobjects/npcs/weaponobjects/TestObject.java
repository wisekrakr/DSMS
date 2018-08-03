package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;

import java.util.Set;

public class TestObject extends WeaponObjectClass {
    private GameObject master;

    public TestObject(Vector2 initialPosition, Behavior initialBehavior, GameObject master) {
        super(GameObjectVisualizationType.ASTEROID, "TESTWEAPON", initialPosition, initialBehavior);
        this.master = master;
        this.setCollisionRadius(3f);
        this.setDamage(3f);

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof NonPlayerCharacter && subject != master){
            subject.setHealth(subject.getHealth() - this.getDamage());
            this.initDebris(toDelete, toAdd);
            toDelete.add(this);
        }
    }
}
