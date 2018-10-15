package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.MiscBehaviors;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class BulletCharacter extends AbstractNonPlayerGameCharacter {

    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private float bulletAge;
    private float radius;
    private float damage;
    private Visualizations visualizations;
    private GameCharacterContext master;

    public BulletCharacter(Vector2 position, float speedMagnitude, float speedDirection, float bulletAge, float radius, float damage, Visualizations visualizations, GameCharacterContext master) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.bulletAge = bulletAge;
        this.radius = radius;
        this.damage = damage;
        this.visualizations = visualizations;
        this.master = master;
    }

    @Override
    public void start() {

        BehavedObject bullet = introduceBehavedObject("weapon",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                0,
                damage,
                visualizations,
                radius
        );

        bullet.behave(Arrays.asList(
                new AbstractBehavior(){
                    @Override
                    public void start() {
                        getContext().updatePhysicalObjectExtra("radius", radius);
                    }

                    @Override
                    public void collide(PhysicalObject object, Vector2 epicentre, float impact) {

                        String name = object.getName();

                        if (!name.contains("debris") && object != master.getPhysicalObject()) {
                            MiscBehaviors.exploding(BulletCharacter.this.getContext(), 3, getContext().getSubject().getCollisionRadius() * 2, 3f);

                            BulletCharacter.this.getContext().removeMyself();
                            getContext().removePhysicalObject();
                        }

                    }

                    @Override
                    public void elapseTime(float clock, float delta) {
                        bulletAge = bulletAge - delta;
                        if (bulletAge < 0) {
                            BulletCharacter.this.getContext().removeMyself();
                            getContext().removePhysicalObject();
                        }
                    }
                }, new AbstractBehavior()
        ));

    }

}
