package com.wisekrakr.firstgame.client;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class GameObjectCreationRequest  implements Serializable {
    private String name;
    private String type;
    private Vector2 initialPosition;


    public GameObjectCreationRequest(String name, String type, Vector2 initialPosition) {
        this.name = name;
        this.type = type;
        this.initialPosition = initialPosition;

    }

    public GameObjectCreationRequest() {
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Vector2 getInitialPosition() {
        return initialPosition;
    }


}
