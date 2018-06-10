package com.wisekrakr.firstgame.overlays;

public class DamageAnimation {
    private int damage;
    private int x;
    private int y;
    private int max;

    public DamageAnimation(int damage, int x, int y)
    {
        this.damage = damage;
        this.x = x;
        this.y = y;
        max = y+30;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y++;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getMax()
    {
        return max;
    }
}
