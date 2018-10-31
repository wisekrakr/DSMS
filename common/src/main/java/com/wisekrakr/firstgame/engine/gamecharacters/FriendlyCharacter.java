package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FriendlyCharacter extends AbstractNonPlayerGameCharacter implements CharacterTools{

    private List<String> avoidList = new ArrayList<>();

    public FriendlyCharacter() {
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
                    // TODO: lets not use the name
                    avoidList.add(p.getObject().getName());
                }
            }
        }

        return avoidList;
    }


}




