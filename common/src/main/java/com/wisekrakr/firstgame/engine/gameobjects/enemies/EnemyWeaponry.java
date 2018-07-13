package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions.EnemyWithMinion;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MineMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MinionMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;

import java.util.Random;

public class EnemyWeaponry {

    public static Bullet loadBullet(Enemy subject){
        Bullet bullet = new Bullet("enemybullito", subject.getPosition(), subject.getOrientation(), subject.getSpeed(),
                BulletMechanics.radius(1), BulletMechanics.determineBulletDamage());
        bullet.setEnemyBullet(true);
        bullet.setBulletSpeed(subject.getSpeed() * 3);
        return bullet;
    }

    public static HomingMissile loadMissile(Enemy subject){
        HomingMissile missile = new HomingMissile("enemymissile", new Vector2(subject.getPosition().x + subject.getCollisionRadius(),
                subject.getPosition().y + subject.getCollisionRadius()),
                subject.getOrientation(), subject.getSpeed(), MissileMechanics.radius(1), MissileMechanics.determineMissileDamage(), true);
        missile.missileEnable(subject);
        missile.setMissileSpeed(subject.getSpeed() * 3);
        return missile;
    }

    public static LaserBeam loadLaser(Enemy subject){
        LaserBeam laserBeam = new LaserBeam("laser", subject.getPosition(), subject.getOrientation(), BulletMechanics.radius(1),
                BulletMechanics.determineBulletDamage(), subject.getSpeed() * 3);
        return laserBeam;
    }

    public static SpaceMine loadMine(Enemy subject){
        SpaceMine spaceMine = new SpaceMine("enemy_mine", subject.getPosition(), subject.getOrientation(),
                subject.getSpeed(), MineMechanics.radius(1), 20, MineMechanics.determineMineDamage());
        spaceMine.setEnemyMine(true);
        return spaceMine;
    }

    public static Spores loadSpores(Enemy subject){
        Random random = new Random();
        Spores spores =  new Spores("spores", new Vector2(subject.getPosition().x + random.nextFloat() * subject.getCollisionRadius(),
                subject.getPosition().y + random.nextFloat() * subject.getCollisionRadius()),
                subject.getOrientation() + random.nextFloat() * subject.getCollisionRadius(), subject.getSpeed() * 4,
                BulletMechanics.radius(0.5f),BulletMechanics.determineBulletDamage() / 5);
        return spores;
    }

    public static Minion initMinion(EnemyWithMinion subject){
        Minion minionShooter = new Minion("Shooter Minion", new Vector2(subject.getPosition().x + subject.getCollisionRadius() * 2,
                subject.getPosition().y + subject.getCollisionRadius() * 2),
                (int) (subject.getMaxHealth()/3),
                subject.getOrientation(),
                MinionMechanics.radius(1));
        minionShooter.setSpeed(subject.getSpeed());
        subject.setMinionActivated(subject.isMinionActivated());
        minionShooter.setMinionShooter(true);
        minionShooter.setEnemyMinion(true);
        return minionShooter;
    }
}
