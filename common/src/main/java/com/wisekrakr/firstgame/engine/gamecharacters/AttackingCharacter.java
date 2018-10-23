package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AttackingCharacter extends AbstractNonPlayerGameCharacter {

    private List<String> targetList = new ArrayList<>();

    public AttackingCharacter() {
    }

    public CharacterTools tools() {
        return tools;
    }

    private CharacterTools tools = new CharacterTools() {

        @Override
        public AbstractBehavior addAnotherBehavior(AbstractBehavior behavior) {
            return behavior;
        }

        @Override
        public void addTargetName(String name) {
            if (name != null) {
                targetList.add(name);
            }
        }

        @Override
        public List<String> targetList() {

            List<NearPhysicalObject> nearbyPhysicalObjects =
                    getContext().findNearbyPhysicalObjects(getContext().getPhysicalObject(), (float) Double.POSITIVE_INFINITY);

            Iterator<NearPhysicalObject> iterator = nearbyPhysicalObjects.iterator();

            if (targetList.isEmpty()) {
                NearPhysicalObject p;
                while (iterator.hasNext()) {
                    p = iterator.next();
                    if (nearbyPhysicalObjects.contains(p) && !p.getObject().getName().contains("weapon") && !p.getObject().getName().contains("debris")) {
                        targetList.add(p.getObject().getName());
                    }
                }
            }

            return targetList;
        }
    };

}




