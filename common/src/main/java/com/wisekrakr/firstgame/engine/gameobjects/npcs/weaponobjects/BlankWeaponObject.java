package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;

import java.util.Set;

public class BlankWeaponObject extends WeaponObjectClass {

    private GameObject weapon;
    private GameObject master;

    public BlankWeaponObject(Vector2 initialPosition, Behavior initialBehavior, GameObject weapon, GameObject master) {
        super(master.getType(), "BlankWeapon", initialPosition, initialBehavior);
        this.weapon = weapon;
        this.master = master;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof NonPlayerCharacter || subject instanceof Player){
            if (subject != master) {
                subject.setHealth(subject.getHealth() - this.getDamage());
                toDelete.add(this);
                int debrisParts = GameHelper.randomGenerator.nextInt(10)+1;
                for(int i = 0; i < debrisParts; i++) {
                    toAdd.add(new WeaponDebris(this.getPosition(), this));
                }
            }
        }
    }

}
