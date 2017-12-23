package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Asteroid;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {
    private Hud hud;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;
    private ShapeRenderer playerRenderer;

    private SpaceEngine engine;
    private GameObject mySelf;
    private final Spaceship controllee1;
    private final Spaceship controllee2;


    public PlayerPerspectiveScreen(SpaceEngine engine, GameObject mySelf, Spaceship controllee1, Spaceship controllee2) {
        this.engine = engine;
        this.mySelf = mySelf;
        this.controllee1 = controllee1;
        this.controllee2 = controllee2;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera.update();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        playerRenderer = new ShapeRenderer();
        playerRenderer.setAutoShapeType(true);

        batch = new SpriteBatch();

        hud = new Hud(batch);
    }

    private void handleInput() {
        if (controllee1 != null) {
            applyControl(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.X, controllee1);
        }

        if (controllee2 != null) {
            applyControl(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.COMMA, controllee2);
        }
    }

    private void applyControl(int forwardsKey, int reverseKey, int leftKey, int rightKey, int resetKey, final Spaceship target) {
        if (Gdx.input.isKeyPressed(resetKey)) {
            target.setPosition(new Vector2(0, 0));
            target.resetControl();
        }

        final Spaceship.ThrottleState throttle;
        if (Gdx.input.isKeyPressed(forwardsKey)) {
            throttle = Spaceship.ThrottleState.FORWARDS;
        } else if (Gdx.input.isKeyPressed(reverseKey)) {
            throttle = Spaceship.ThrottleState.REVERSE;
        } else {
            throttle = Spaceship.ThrottleState.STATUSQUO;
        }

        final Spaceship.SteeringState steering;
        if (Gdx.input.isKeyPressed(leftKey)) {
            steering = Spaceship.SteeringState.LEFT;
        } else if (Gdx.input.isKeyPressed(rightKey)) {
            steering = Spaceship.SteeringState.RIGHT;
        } else {
            steering = Spaceship.SteeringState.CENTER;
        }

        engine.forObject(target, new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject a) {
                target.control(throttle, steering);
            }
        });
    }

    @Override
    public void render(float delta) {
        handleInput();

        camera.position.set(mySelf.getPosition().x, mySelf.getPosition().y, 100);
        camera.up.set(1, 0, 0);
        camera.rotate(mySelf.getOrientation() * 180 / (float) Math.PI, 0, 0, 1);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        renderObjects();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.update(delta);
        hud.stage.draw();
    }

    private void renderObjects() {
        System.out.println("Myself is at " + mySelf.getPosition() + ", with an orientation of: " + mySelf.getOrientation() * 180 / Math.PI);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        engine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject object) {
                if (object instanceof Player) {
                    shapeRenderer.setColor(Color.GOLD);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 10);

                    shapeRenderer.setColor(Color.BLUE);

                    shapeRenderer.cone(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()), object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()), 0, 5, 0);
                }
                else if (object instanceof Asteroid) {
                    Asteroid asteroid = (Asteroid) object;
                    shapeRenderer.setColor(Color.BROWN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, asteroid.getRadius());

                    shapeRenderer.setColor(Color.GREEN);

                    shapeRenderer.cone(object.getPosition().x + (asteroid.getRadius() / 2) * (float) Math.cos(object.getOrientation()), object.getPosition().y + (asteroid.getRadius() / 2)* (float) Math.sin(object.getOrientation()), 0, (asteroid.getRadius() / 2), 0);
                }
                else {
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 6);

                    shapeRenderer.setColor(Color.PINK);
                    shapeRenderer.cone(object.getPosition().x + 2 * (float) Math.cos(object.getOrientation()), object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), 0, 3, 0);
                }
            }
        });

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        hud.dispose();

    }
}