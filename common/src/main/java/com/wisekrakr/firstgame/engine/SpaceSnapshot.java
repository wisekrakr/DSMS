package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SpaceSnapshot implements Serializable {
    private String name;
    private float time;
    private List<GameObjectSnapshot> gameObjects;

    public SpaceSnapshot() {
    }

    public SpaceSnapshot(String name, float time, List<GameObjectSnapshot> gameObjects) {
        this.name = name;
        this.time = time;
        this.gameObjects = gameObjects;
    }

    public String getName() {
        return name;
    }

    public float getTime() {
        return time;
    }

    public List<GameObjectSnapshot> getGameObjects() {
        return gameObjects;
    }

    public static class GameObjectSnapshot implements Serializable {
        private String name;
        private GameObjectType type;
        private float speed;
        private float orientation;
        private Vector2 position;
        private Map<String, Object> extra;
        private Map<String, Object> ammoExtra;
        private Map<String, Object> healthExtra;
        private Map<String, Object> maxHealthExtra;
        private Map<String, Object> scoreExtra;
        private Map<String, Object> damageExtra;
        private Map<String, Object> damageTakenExtra;
        private Map<String, Object> randomExtra;
        private Map<String, Object> killedByExtra;

        public GameObjectSnapshot() {
        }

        public GameObjectSnapshot(String name, GameObjectType type, float speed, float orientation, Vector2 position, Map<String,
                Object> extra, Map<String, Object> ammoExtra, Map<String, Object> healthExtra, Map<String, Object> maxHealthExtra,  Map<String, Object> scoreExtra,
                                  Map<String, Object> damageExtra, Map<String, Object> damageTakenExtra, Map<String, Object> randomExtra, Map<String, Object> killedByExtra) {
            this.name = name;
            this.type = type;
            this.speed = speed;
            this.orientation = orientation;
            this.position = position;
            this.extra = extra;
            this.ammoExtra = ammoExtra;
            this.healthExtra = healthExtra;
            this.maxHealthExtra = maxHealthExtra;
            this.scoreExtra = scoreExtra;
            this.damageExtra = damageExtra;
            this.randomExtra = randomExtra;
            this.killedByExtra = killedByExtra;

        }

        public String getName() {
            return name;
        }

        public GameObjectType getType() {
            return type;
        }

        public float getSpeed() {
            return speed;
        }

        public float getOrientation() {
            return orientation;
        }

        public Vector2 getPosition() {
            return position;
        }

        public Map<String, Object> extraProperties() {
            return extra;
        }

        public Map<String, Object> ammoProperties() {
            return ammoExtra;
        }

        public Map<String, Object> healthProperties() {
            return healthExtra;
        }

        public Map<String, Object> maxHealthProperties() {
            return maxHealthExtra;
        }

        public Map<String, Object> scoreProperties() {
            return scoreExtra;
        }

        public Map<String, Object> damageProperties() {
            return damageExtra;
        }

        public Map<String, Object> damageTakenProperties() {
            return damageTakenExtra;
        }

        public Map<String, Object> randomProperties() {
            return randomExtra;
        }

        public Map<String, Object> killedByProperties() {
            return killedByExtra;
        }
    }
}
