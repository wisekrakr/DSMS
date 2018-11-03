package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class BulletCharacter extends AbstractNonPlayerGameCharacter {

    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private float bulletAge;
    private float radius;
    private Visualizations visualizations;
    private GameCharacterContext master;

    public BulletCharacter(Vector2 position, float speedMagnitude, float speedDirection, float bulletAge, float radius, Visualizations visualizations, GameCharacterContext master) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.bulletAge = bulletAge;
        this.radius = radius;
        this.visualizations = visualizations;
        this.master = master;
    }

    @Override
    public void start() {
        BehavedObject bullet = introduceBehavedObject("bullet",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                visualizations,
                radius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        BulletCharacter.this.getContext().removeMyself();
                    }
                });

        getContext().tagPhysicalObject(bullet.getObject(), Tags.PROJECTILE);

        bullet.behave(Arrays.asList(
                new AbstractBehavior(){
                    @Override
                    public void start() {
                        getContext().updatePhysicalObjectExtra("radius", radius);
                    }

                    @Override
                    public void collide(PhysicalObject object, Vector2 epicentre, float impact) {

                        if (!object.getTags().contains(Tags.DEBRIS) && object != master.getPhysicalObject()) {
                            getContext().addCharacter(new ExplosionCharacter(getContext().getSubject().getPosition(),
                                    GameHelper.generateRandomNumberBetween(5f, 20f),
                                    GameHelper.randomDirection(),
                                    5,
                                    getContext().getSubject().getCollisionRadius()*2,
                                    2f,
                                    Visualizations.EXPLOSION), null);

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
