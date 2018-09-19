package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PackageObject extends WeaponObjectClass{

    private Behavior desiredBehavior;
    private boolean timedMission;

    public PackageObject(Vector2 initialPosition, GameObject master) {
        super(GameObjectVisualizationType.SPORE, "Package", initialPosition, master);

        this.rootBehavior(new MyBehavior());

        this.setDimensions(14f, 14f);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        setDirection(this.getDirection() + (float) Math.PI);
        setOrientation(this.getDirection());
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("width", getWidth());
        result.put("height", getHeight());


        return result;
    }

    public void sendOrder() {
        desiredBehavior = new RotatingBehavior(5f);
    }

    public void inPostmanCare(GameObject target) {
        desiredBehavior = new PackageBehavior(target);
    }

    public void timedMissionInProgress(GameObject target){
        desiredBehavior = new PackageBehavior(target);
        timedMission = true;
    }

    public void delivery(GameObject target){
        desiredBehavior = new ChasingBehavior(target);
    }

    public void missionEnd() {
        desiredBehavior = new ExplodeAndLeaveDebrisBehavior(8f);
    }

    private class MyBehavior extends AbstractBehavior {
        @Override
        public void elapseTime(float clock, float delta) {
            if (desiredBehavior != null) {
                getContext().pushSubBehavior(desiredBehavior);
                desiredBehavior = null;
            }
        }
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (timedMission){
            if (subject instanceof Player){
                toDelete.remove(this);
            }
        }
    }

    public Behavior getDesiredBehavior() {
        return desiredBehavior;
    }

    public boolean isTimedMission() {
        return timedMission;
    }
}
