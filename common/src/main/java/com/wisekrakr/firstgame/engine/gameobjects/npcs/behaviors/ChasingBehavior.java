package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

import java.util.Set;

public class ChasingBehavior extends Behavior {
    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        GameObject target = context.nearest();

        if (target != null) {
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(-angle);
            context.setSpeed(100f);
        }
    }
}
