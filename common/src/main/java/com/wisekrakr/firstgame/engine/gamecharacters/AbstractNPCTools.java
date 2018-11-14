package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.StringHelper;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.*;

public class AbstractNPCTools  {

    private Set<String> targetList = new HashSet<>();
    private Set<String> avoidList = new HashSet<>();

    public AbstractNPCTools() {

    }

    public CharacterTools tools() {
        return tools;
    }

    private CharacterTools tools = new CharacterTools() {

        private float health;
        private Float lastShot;

        @Override
        public void healthIndicator(float initialHealth) {
            health = initialHealth;
        }

        @Override
        public void addTargetName(String tag) {
            if (tag != null) {
                targetList.add(tag);
            }
        }

        @Override
        public void removeTargetName(String tag) {
            if (tag != null){
                targetList.remove(tag);
            }
        }

        @Override
        public void addAvoidName(String tag) {
            if (tag != null) {
                avoidList.add(tag);
            }
        }

        @Override
        public Set<String> targetList(GameCharacterContext context) {

            List<NearPhysicalObject> nearbyPhysicalObjects =
                    context.findNearbyPhysicalObjects(context.getPhysicalObject(), (float) Double.POSITIVE_INFINITY);

            Iterator<NearPhysicalObject> iterator = nearbyPhysicalObjects.iterator();

            if (targetList.isEmpty()) {
                NearPhysicalObject p;
                while (iterator.hasNext()) {
                    p = iterator.next();
                    if (nearbyPhysicalObjects.contains(p) && !p.getObject().getTags().contains(Tags.PROJECTILE) && !p.getObject().getTags().contains(Tags.DEBRIS)) {
                        targetList.add(p.getObject().getTags().toString());
                    }
                }
            }

            return targetList;
        }

        @Override
        public Set<String> avoidList(GameCharacterContext context) {
            List<NearPhysicalObject> nearbyPhysicalObjects =
                    context.findNearbyPhysicalObjects(context.getPhysicalObject(), (float) Double.POSITIVE_INFINITY);

            Iterator<NearPhysicalObject> iterator = nearbyPhysicalObjects.iterator();

            if (avoidList.isEmpty()) {
                NearPhysicalObject p;
                while (iterator.hasNext()) {
                    p = iterator.next();
                    if (nearbyPhysicalObjects.contains(p) && !p.getObject().getTags().contains(Tags.PROJECTILE) && !p.getObject().getTags().contains(Tags.DEBRIS)) {
                        avoidList.add(p.getObject().getTags().toString());
                    }
                }
            }
            return avoidList;
        }

        @Override
        public float getHealth() {

            return health;
        }

        @Override
        public void damageIndicator(float damage) {
            health -= damage;
        }

        @Override
        public void weaponry(GameCharacterContext context, Weaponry weaponry, Float fireRate, Float radiusOfAttack) {

            List<NearPhysicalObject> nearbyPhysicalObjects =
                    context.findNearbyPhysicalObjects(context.getPhysicalObject(), radiusOfAttack);

            if (!nearbyPhysicalObjects.isEmpty()) {
                for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                    PhysicalObject target = nearPhysicalObject.getObject();

                    float x = context.getPhysicalObject().getPosition().x;
                    float y = context.getPhysicalObject().getPosition().y;

                    float deltaX = ((float) Math.cos(context.getPhysicalObject().getOrientation()));
                    float deltaY = ((float) Math.sin(context.getPhysicalObject().getOrientation()));

                    if (lastShot == null) {
                        lastShot = context.getSpaceEngine().getTime();
                    }

                    if (fireRate != 0) {
                        if (context.getSpaceEngine().getTime() - lastShot > fireRate) {
                            if (!target.getTags().contains(Tags.PROJECTILE) && !target.getTags().contains(Tags.DEBRIS) && target != context.getPhysicalObject()) {

                                for (String string : targetList) {
                                    if (target.getTags().contains(string)) {

                                        if (GameHelper.distanceBetweenPhysicals(context.getPhysicalObject(), target) < radiusOfAttack) {

                                            switch (weaponry) {
                                                case NONE:
                                                    System.out.println("No weaponry chosen");
                                                    break;
                                                case BULLETS:
                                                    BulletCharacter bullet = new BulletCharacter(
                                                            new Vector2(x + context.getPhysicalObject().getCollisionRadius() * deltaX,
                                                            y + context.getPhysicalObject().getCollisionRadius() * deltaY),
                                                            context.getPhysicalObject().getSpeedMagnitude() + 200f,
                                                            context.getPhysicalObject().getOrientation(),
                                                            3f,
                                                            context.getPhysicalObject().getCollisionRadius() / 8,
                                                            Visualizations.LEFT_CANNON,
                                                            context
                                                    );
                                                    context.addCharacter(bullet, null);
                                                    break;
                                                case HOMING_MISSILES:
                                                    HomingMissileCharacter homingMissile = new HomingMissileCharacter(
                                                            new Vector2(x + context.getPhysicalObject().getCollisionRadius() * deltaX,
                                                            y + context.getPhysicalObject().getCollisionRadius() * deltaY),
                                                            context.getPhysicalObject().getSpeedMagnitude() + 200f,
                                                            context.getPhysicalObject().getOrientation(),
                                                            5f,
                                                            context.getPhysicalObject().getCollisionRadius() / 5,
                                                            radiusOfAttack,
                                                            Visualizations.RIGHT_CANNON,
                                                            context,
                                                            targetList
                                                    );
                                                    context.addCharacter(homingMissile, null);
                                                    break;
                                                case SPLASH_BULLETS:
                                                    SplashBulletCharacter splashBullet = new SplashBulletCharacter(
                                                            new Vector2(x + context.getPhysicalObject().getCollisionRadius() * deltaX,
                                                            y + context.getPhysicalObject().getCollisionRadius() * deltaY),
                                                            context.getPhysicalObject().getSpeedMagnitude() + 200f,
                                                            context.getPhysicalObject().getOrientation(),
                                                            5f,
                                                            context.getPhysicalObject().getCollisionRadius() / 3,
                                                            Visualizations.BOULDER,
                                                            context
                                                    );
                                                    context.addCharacter(splashBullet, null);
                                                    break;

                                            }
                                            lastShot = context.getSpaceEngine().getTime();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };
}




