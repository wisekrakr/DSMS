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
        private GameObjectVisualizationType type;
        private float speed;
        private float orientation;
        private Vector2 position;
        private Map<String, Object> extra;

        public GameObjectSnapshot() {
        }

        public GameObjectSnapshot(String name, GameObjectVisualizationType type, float speed, float orientation, Vector2 position, Map<String,
                Object> extra) {
            this.name = name;
            this.type = type;
            this.speed = speed;
            this.orientation = orientation;
            this.position = position;
            this.extra = extra;
        }

        public String getName() {
            return name;
        }

        public GameObjectVisualizationType getType() {
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
    }
}
