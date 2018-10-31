package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class SplashBulletCharacter extends AbstractNonPlayerGameCharacter {
    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private float bulletAge;
    private float radius;
    private float damage;
    private Visualizations visualizations;
    private GameCharacterContext master;

    public SplashBulletCharacter(Vector2 position, float speedMagnitude, float speedDirection, float bulletAge, float radius, float damage, Visualizations visualizations, GameCharacterContext master) {
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
        BehavedObject bullet = introduceBehavedObject("splash bullet",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                visualizations,
                radius,
                null);

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

                            SplashBulletCharacter.this.getContext().removeMyself();
                            getContext().removePhysicalObject();
                        }
                    }

                    @Override
                    public void elapseTime(float clock, float delta) {
                        bulletAge = bulletAge - delta;
                        if (bulletAge < 0) {
                            SplashBulletCharacter.this.getContext().removeMyself();
                            getContext().removePhysicalObject();

                            int fragments = GameHelper.randomGenerator.nextInt(10)+1;
                            for(int i = 0; i < fragments; i++) {
                                getContext().addCharacter(new BulletCharacter(getContext().getSubject().getPosition(),
                                        getContext().getSubject().getSpeedMagnitude(),
                                        GameHelper.randomDirection(),
                                        5f,
                                        3f,
                                        3f,
                                        Visualizations.B,
                                        SplashBulletCharacter.this.getContext()
                                ), null);
                            }
                        }
                    }
                }, new AbstractBehavior()
        ));
    }
}