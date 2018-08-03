package com.wisekrakr.firstgame.engine.gameobjects.npcs;

public interface HealthAndDamageContext {

    double getHealth();
    float actionDistance();
    double damage();

    void setHealth(double health);
    void setActionDistance(float actionDistance);
    void setDamage(double damage);
}
