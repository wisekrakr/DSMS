package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.Behavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.BehaviorContext;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.ArrayList;
import java.util.List;

public class AbstractNonPlayerGameCharacter extends AbstractGameCharacter {
    private List<Behavior> activeBehaviors = new ArrayList<>();

    protected final void rootBehavior(Behavior initialBehavior) {
        addBehavior(initialBehavior);
    }

    private void addBehavior(Behavior b) {
        int myIndex = activeBehaviors.size();

        activeBehaviors.add(b);

        b.init(new BehaviorContext() {
            @Override
            public void pushSubBehavior(Behavior b) {

            }

            @Override
            public Behavior existingSubBehavior() {
                if (activeBehaviors.size() > myIndex + 1) {
                    return activeBehaviors.get(myIndex + 1);
                }

                return null;
            }

            @Override
            public void addCharacter(GameCharacter newObject) {
                getContext().addCharacter(newObject);
            }

            @Override
            public PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener alistener) {
                // TODO: keep track of physical objects created by this behavior?

                return AbstractNonPlayerGameCharacter.this.getContext().addPhysicalObject(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius, alistener);
            }

            @Override
            public void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualizationEngine, Float collisionRadius) {
                AbstractNonPlayerGameCharacter.this.getContext().updatePhysicalObject(target, name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius);
            }

            @Override
            public void updatePhysicalObjectExtra(PhysicalObject target, String key, Object value) {
                AbstractNonPlayerGameCharacter.this.getContext().updatePhysicalObjectExtra(target, key, value);
            }


            @Override
            public void removePhysicalObject(PhysicalObject object) {
                AbstractNonPlayerGameCharacter.this.getContext().removePhysicalObject(object);
            }
        });

        b.start();
    }

    private void cancelBehavior(com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior remove) {
        remove.stop();
    }

    @Override
    public void elapseTime(float delta) {
        int index = 0;

        while (index < activeBehaviors.size()) {
            Behavior b = activeBehaviors.get(index);

            int myIndex = index;

            b.elapseTime(getContext().getSpaceEngine().getTime(), delta);

            index = index + 1;
        }
/*
        // TODO: move towards the general infrastructure
        setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * speed * delta,
                getPosition().y + (float) Math.sin(getDirection()) * speed * delta)
        );
        */
    }
}
