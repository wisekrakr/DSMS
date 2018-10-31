package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.*;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;
import com.wisekrakr.firstgame.engine.scenarios.WildlifeManagement;

import java.util.Arrays;

public class MyFirstSpaceGame extends Scenario {
    @Override
    public void start() {
        getContext().engine().addScenario(new WildlifeManagement(0.3f, 2, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new NPCMissileShooter(position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack);
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0.5f, 1, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new NPCAvoiding(position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack,
                        health
                );
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0.1f, 1, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new NPCMinionSpawner(position,
                        100f,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack,
                        health
                );
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0.7f, 5, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new AsteroidCharacter(position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack,
                        health);
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(1f, 10, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new NPCSpeedyDodger(position,
                        20f,
                        speedDirection,
                        GameHelper.generateRandomNumberBetween(150f, 200f),
                        radiusOfAttack,
                        health
                );
            }
        }));


        getContext().engine().addScenario(new WildlifeManagement(0.3f, 1, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new XCharacter(
                        Arrays.asList(AsteroidCharacter.class.getName()),
                                position,
                                radius,
                                speedDirection,
                                speedMagnitude,
                                radiusOfAttack,
                                health
                        );
            }
        }));
    }
}
