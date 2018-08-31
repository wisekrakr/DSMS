package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.MathUtils;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.KillMission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.PackageDeliveryMission;

public class MissionGen  {

    private static Mission mission;

    public static Mission missionSetter(GameObject object){

        int randomMission = MathUtils.random(1,2);

        switch (randomMission){
            case 1:
                mission = new KillMission(object.getPosition(), null);
                break;
            case 2:
                mission = new PackageDeliveryMission(object.getPosition());
                break;

        }
        return mission;
    }
}
