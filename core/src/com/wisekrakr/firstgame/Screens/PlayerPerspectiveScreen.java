package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.util.List;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {
    private Hud hud;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;
    private ShapeRenderer playerRenderer;

    private ClientConnector connector;
    private final String mySelf;
    private String first = null;
    private String second = null;

    public PlayerPerspectiveScreen(ClientConnector connector, List<String> players, String mySelf) {
        this.connector = connector;
        this.mySelf = mySelf;

        int i = 0;
        for (String name: players) {
            if (first == null) {
                first = name;
            }
            else if (second == null) {
                second = name;
            }
            connector.createSpaceship(name, 100 + i * 50, 0);
             i = i +1;
        }

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
        applyControl(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.X, first);
        applyControl(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.COMMA, second);
    }

    private void applyControl(int forwardsKey, int reverseKey, int leftKey, int rightKey, int resetKey, final String target) {
        /*
        if (Gdx.input.isKeyPressed(resetKey)) {
            target.setPosition(new Vector2(0, 0));
            target.resetControl();
        }
        */

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

        connector.controlSpaceship(target, throttle, steering);
    }


    @Override
    public void render(float delta) {
        handleInput();

        SpaceSnapshot snapshot = connector.latestSnapshot();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        //        System.out.println("Myself is at " + mySelf.getPosition() + ", with an orientation of: " + mySelf.getOrientation() * 180 / Math.PI);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (snapshot != null) {
            for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                if (mySelf.equals(object.getName())) {
                    camera.position.set(object.getPosition().x, object.getPosition().y, 100);
                    camera.up.set(1, 0, 0);
                    camera.rotate(object.getOrientation() * 180 / (float) Math.PI, 0, 0, 1);
                    camera.update();
                }

                if ("Player".equals(object.getType())) {
                    shapeRenderer.setColor(Color.GOLD);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 10);

                    shapeRenderer.setColor(Color.BLUE);

                    shapeRenderer.cone(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()), object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()), 0, 5, 0);
                } else if ("Asteroid".equals(object.getType())) {
                    shapeRenderer.setColor(Color.BROWN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                    shapeRenderer.setColor(Color.GREEN);

                    shapeRenderer.cone(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()), object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), 0, (radius / 2), 0);
                } else {
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 6);

                    shapeRenderer.setColor(Color.PINK);
                    shapeRenderer.cone(object.getPosition().x + 2 * (float) Math.cos(object.getOrientation()), object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), 0, 3, 0);
                }
            }
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.update(delta);
        hud.stage.draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        hud.dispose();

    }
}