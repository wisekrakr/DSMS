package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.Game;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.*;

public class DamselInDistress extends Scenario {
    private GameObject damsel;
    private Set<GameObject> enemies = new HashSet<>();
    private Set<Player> players = new HashSet<>();
    private float runToDistance;
    private float escapeDistance;
    private final int minMinions;
    private final int maxMinions;


    public DamselInDistress(float runToDistance, float escapeDistance, int minMinions, int maxMinions) {
        this.runToDistance = runToDistance;
        this.escapeDistance = escapeDistance;
        this.minMinions = minMinions;
        this.maxMinions = maxMinions;

    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (damsel == null) {
            damsel = spaceEngine.addGameObject(new Damsel( GameHelper.randomPosition()), new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {
                    damsel = null;
                }
            });
        }


        updateDamsel(spaceEngine);

    }

    private void updateDamsel(SpaceEngine spaceEngine){

        if (enemies.size() < Math.min(minMinions + enemies.size(), maxMinions)){
            Pervert pervert = new Pervert(GameHelper.randomPosition());

            spaceEngine.addGameObject(pervert, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    enemies.add(pervert);
                }

                @Override
                public void removed() {

                }
            });
        }

        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                for (GameObject object: enemies){
                    if (target != object && !(target instanceof DebrisObject) && !(target instanceof WeaponObjectClass) && !enemies.contains(target)) {
                        if (GameHelper.distanceBetween(object.getPosition(), target.getPosition()) < object.getActionDistance()) {
                            if (target instanceof Damsel){
                                damsel = target;
                                ((Damsel) damsel).runFrom(object);
                            }else {
                                ((Damsel) damsel).lookingForAHero();
                            }
                        }
                    }
                }
                if (target instanceof Player && damsel instanceof Damsel){
                    if (GameHelper.distanceBetween(damsel.getPosition(), target.getPosition()) < runToDistance) {
                        ((Damsel) damsel).clingOn(target);
                    }
                }
            }
        });
    }
}
