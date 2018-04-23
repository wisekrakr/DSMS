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
        private String type;
        private float speed;
        private float orientation;
        private Vector2 position;
        private Map<String, Object> extra;
        private Map<String, Object> ammoExtra;
        private Map<String, Object> healthExtra;
        private Map<String, Object> scoreExtra;
        private Map<String, Object> missileExtra;
        private Map<String, Object> damageExtra;
        private Map<String, Object> randomExtra;


        public GameObjectSnapshot() {
        }

        public GameObjectSnapshot(String name, String type, float speed, float orientation, Vector2 position,  Map<String,
                Object> extra, Map<String, Object> ammoExtra, Map<String, Object> healthExtra, Map<String, Object> scoreExtra,
                                  Map<String, Object> missileExtra, Map<String, Object> damageExtra, Map<String, Object> randomExtra) {
            this.name = name;
            this.type = type;
            this.speed = speed;
            this.orientation = orientation;
            this.position = position;
            this.extra = extra;
            this.ammoExtra = ammoExtra;
            this.healthExtra = healthExtra;
            this.scoreExtra = scoreExtra;
            this.missileExtra = missileExtra;
            this.damageExtra = damageExtra;
            this.randomExtra = randomExtra;

        }

        public String getName() {
            return name;
        }

        public String getType() {
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

        public Map<String, Object> scoreProperties() {
            return scoreExtra;
        }

        public Map<String, Object> missileProperties() {
            return missileExtra;
        }


        public Map<String, Object> damageProperties() {
            return damageExtra;
        }

        public Map<String, Object> randomProperties() {
            return randomExtra;
        }


    }
}
