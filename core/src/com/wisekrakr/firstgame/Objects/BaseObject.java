package com.wisekrakr.firstgame.Objects;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by David on 11/6/2017.
 */
public abstract class BaseObject {

    private Vector2 angle;
    private Vector2 velocity;
    private Vector2 position;
    private String name;

    private float x;
    private float elapsedTime;
    private float orientation;
    private float speed;

    public abstract void init(); //initialize the object
    public abstract void movement(float delta); // method for objects that don't have a handleInput method. They move by themselves
    public abstract void update(float delta); // update the delta.
    public abstract void render(ShapeRenderer renderer); //renders a shape on top of the position of a BaseObject




    public BaseObject(String name, Vector2 position, Vector2 velocity) {
        this.position = position;
        this.name = name;
        this.velocity = velocity;

    }

    //Method to see if the player is close to any target(enemy)
    //If the player closer than 50 --> Collision imminent
    //Else if the player has the same position as the enemy --> Collision!
    //Else enemy keeps flying its route
    public abstract float collisionDetector(BaseObject target);

    public abstract void attack(float delta);


    public final float getOrientation() {
        return orientation;
    }

    public final float getSpeed() {
        return speed;
    }

    public final Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setOrientation(float newOrientation) {
        this.orientation = newOrientation;
    }

    public String getName() {
        return name;
    }


    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

}
