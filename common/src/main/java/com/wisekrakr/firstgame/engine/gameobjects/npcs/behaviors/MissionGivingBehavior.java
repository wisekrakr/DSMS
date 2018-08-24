package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.KillMission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class MissionGivingBehavior extends Behavior {

    private boolean missionActive;
    private GameObject target;
    private Mission mission;
    private float time;

    public MissionGivingBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (time == 0){
            time = clock;
        }

        if (target != null) {
            if (!(missionActive)) {
                //mission = new KillMission(new Vector2(context.getPosition().x + 20, context.getPosition().y + 20));
                context.addGameObject(mission);
                missionActive = true;
                if (clock - time > 5f) {
                    context.removeGameObject(mission);
                    time = clock;
                }
            }

        }
    }
}
