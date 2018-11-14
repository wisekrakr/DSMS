package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.*;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;
import com.wisekrakr.firstgame.engine.scenarios.WildlifeManagement;

public class MyFirstSpaceGame extends Scenario {
    @Override
    public void start() {

        getContext().engine().addScenario(new WildlifeManagement(0f, 2, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                return new NPCMissileShooter(position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack);
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0.0f, 2, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                return new NPCAvoiding(position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack
                );
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0f, 1, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                return new NPCMinionSpawner(position,
                        100f,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack
                );
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0.5f, 5, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                return new AsteroidCharacter(position,
                        radius,
                        speedDirection,
                        speedMagnitude
                );
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0f, 3, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                return new NPCSpeedyDodger(position,
                        20f,
                        speedDirection,
                        GameHelper.generateRandomNumberBetween(150f, 200f),
                        radiusOfAttack
                );
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0f, 3, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                return new NPCPlayerHunter(position,
                        30f,
                        speedDirection,
                        GameHelper.generateRandomNumberBetween(120f, 180f),
                        radiusOfAttack
                );
            }
        }));

        getContext().engine().addScenario(new WildlifeManagement(0.3f, 3, new CharacterFactory() {
            @Override
            public XCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {

                return new XCharacter(
                        position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack);
            }
        }));

    }
}
