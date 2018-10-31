package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScenarioCharacter extends AbstractNonPlayerGameCharacter implements CharacterTools{
    private Vector2 initialPosition;
    private float initialRadius;
    private float initialDirection;
    private float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;
    private List<String> avoidList = new ArrayList<>();

    public ScenarioCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }

    public ScenarioCharacter() {
    }

    @Override
    public AbstractBehavior addAnotherBehavior(AbstractBehavior behavior) {
        return behavior;
    }


    @Override
    public void addTargetName(String name) {
        if (name != null){
            avoidList.add(name);
        }
    }

    @Override
    public List<String> targetList() {
        List<NearPhysicalObject> nearbyPhysicalObjects =
                getContext().findNearbyPhysicalObjects(getContext().getPhysicalObject(), (float) Double.POSITIVE_INFINITY);

        Iterator<NearPhysicalObject> iterator = nearbyPhysicalObjects.iterator();

        if (avoidList.isEmpty()) {
            NearPhysicalObject p;
            while (iterator.hasNext()) {
                p = iterator.next();
                if (nearbyPhysicalObjects.contains(p) && !p.getObject().getTags().contains(Tags.PROJECTILE) && !p.getObject().getTags().contains(Tags.DEBRIS)) {
                    avoidList.add(p.getObject().getName());
                }
            }
        }

        return avoidList;
    }
}




