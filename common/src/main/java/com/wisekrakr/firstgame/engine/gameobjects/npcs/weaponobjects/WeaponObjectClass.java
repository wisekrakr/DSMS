package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.Set;

public class WeaponObjectClass extends NonPlayerCharacter {
    private GameObject master;

    public WeaponObjectClass(GameObjectVisualizationType type, String name, Vector2 initialPosition, Behavior initialBehavior, GameObject master) {
        super(type, name, initialPosition, initialBehavior);
        this.master = master;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof NonPlayerCharacter || subject instanceof Player){
            if (subject != master) {
                subject.setHealth(subject.getHealth() - this.getDamage());
                toDelete.add(this);
                int debrisParts = GameHelper.randomGenerator.nextInt((int) master.getCollisionRadius())+4;
                for(int i = 0; i < debrisParts; i++) {
                    toAdd.add(new DebrisObject(this.getPosition(), GameHelper.generateRandomNumberBetween(0, this.getCollisionRadius()* 2)));
                }
            }
        }
    }
}
