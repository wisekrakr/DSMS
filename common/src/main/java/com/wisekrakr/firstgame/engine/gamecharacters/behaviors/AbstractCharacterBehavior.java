package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import java.util.ArrayList;
import java.util.List;

public class AbstractCharacterBehavior implements CharacterBehavior {

    private List<Behavior> behaviors = new ArrayList<>();

    @Override
    public void addBehavior(Behavior behavior) {
        if (!behaviors.isEmpty()){
            behaviors.add(behavior);
        }
    }

    public List<Behavior> getBehaviors() {
        return this.behaviors = behaviors;
    }
}
