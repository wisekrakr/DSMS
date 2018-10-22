package com.wisekrakr.firstgame.engine.gamecharacters;

import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.Behavior;

import java.util.List;

public interface CharacterTools {

    void addTargetName(String name);

    List<String> targetList();

    AbstractBehavior addAnotherBehavior(AbstractBehavior behavior);

}
