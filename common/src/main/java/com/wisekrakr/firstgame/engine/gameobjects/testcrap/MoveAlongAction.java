package com.wisekrakr.firstgame.engine.gameobjects.testcrap;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class MoveAlongAction extends TemporalAction {

    private float x, y;
    private boolean rotate;
    private Path<Vector2> path;
    private Vector2 value = new Vector2();

    public Path<Vector2> getPath () {
        return path;
    }

    public void setPath (Path<Vector2> path) {
        this.path = path;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate (boolean rotate) {
        this.rotate = rotate;
    }

    @Override
    protected void begin () {
        x = actor.getX();
        y = actor.getY();
    }

    @Override
    protected void update (float percent) {
        path.valueAt(value, percent);
        actor.setPosition(x + value.x, y + value.y);
        if (rotate) {
            actor.setRotation(path.derivativeAt(value, percent).angle());
        }
    }

    @Override
    public void reset () {
        super.reset();
        path = null;
    }

    /**Returns a new or pooled MoveAlongAction with the given path and a duration of {@code 1.0}. */
    public static MoveAlongAction obtain(Path<Vector2> path) {
        return obtain(path, 1.0f);
    }

    /**Returns a new or pooled MoveAlongAction with the given path and duration. */
    public static MoveAlongAction obtain(Path<Vector2> path, float duration) {
        Pool<MoveAlongAction> pool = Pools.get(MoveAlongAction.class);
        MoveAlongAction action = pool.obtain();
        action.setDuration(duration);
        action.setPath(path);
        action.setPool(pool);
        return action;
    }

}
