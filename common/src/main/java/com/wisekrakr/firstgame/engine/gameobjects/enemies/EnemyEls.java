package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MinionShooterEnemy;

import java.util.Set;

public class EnemyEls extends Enemy {

    private MinionShooterEnemy minionShooterEnemy;
    private float minionAngle;

    public EnemyEls(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(GameObjectType.ELS, name, position, health, direction, speed, radius, space);

        setAggroDistance(950f);
        setAttackDistance(750f);
        setChangeDirectionTime(3f);

        minionShooterEnemy = new MinionShooterEnemy("minionEls", new Vector2(getPosition().x + getCollisionRadius() * 2,
                getPosition().y + getCollisionRadius() * 2), 20, getOrientation(), 8f, getSpace());

    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_BULLETS);
                setMovingState(MovingState.FLY_AROUND);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);
        minionAngle += 2f * delta;
        minionShooterEnemy.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 45f),
                (float) (getPosition().y + Math.sin(minionAngle) * 45f)));

        toAdd.add(minionShooterEnemy);


    }
}
