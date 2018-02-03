package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.util.List;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {


    private float minX = -500;
    private float minY = -500;
    private float width = 1000;
    private float height = 1000;

    private Hud hud;
    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;

    private OrthographicCamera minimapcamera;

    private MiniMap miniMap;

    private SpaceGameContainer container;

    private ShapeRenderer shapeRenderer;

    private ClientConnector connector;
    private String mySelf;
    private String first = null;
    private String second = null;

    public PlayerPerspectiveScreen(ClientConnector connector, List<String> players, String mySelf) {
        this.connector = connector;
        this.mySelf = mySelf;

        container = new SpaceGameContainer();

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

        stage = new Stage();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera.update();

// TODO: how to create a minimap?
//        minimapcamera = new OrthographicCamera();
//        minimapcamera.setToOrtho(false, 1000, 1000);

//        miniMap = new MiniMap(0.2f, 0.025f, minimapcamera);
//        miniMap.setWorldSize(width, height);


//TODO: see how we can create a background....either by using stage like now, or to use another camera
//        Texture texture = new Texture(Gdx.files.internal("stars.jpg"));
//        texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);

//        BackgroundStars backgroundStars = new BackgroundStars(texture);
//        backgroundStars.setSize(Constants.WORLD_WIDTH*2, Constants.WORLD_HEIGHT*2);
//        backgroundStars.setSpeed(1);
//        stage.addActor(backgroundStars);
        Gdx.input.setInputProcessor(stage);


        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        batch = new SpriteBatch();

        hud = new Hud(batch);

    }



    private void handleInput() {
        applyControl(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E, Input.Keys.Q, Input.Keys.C, Input.Keys.X, first);
        applyControl(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.ENTER, Input.Keys.O, Input.Keys.P, Input.Keys.COMMA, second);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.zoom += 0.08;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.zoom -= 0.08;
        }
/*
        if(Gdx.input.isKeyPressed(Input.Keys.INSERT)){
            container.setScreen(new PauseScreen());
        }
        */
    }

    private void applyControl(int forwardsKey, int reverseKey, int leftKey, int rightKey, int boostKey, int dodgeKey, int shootKey, int resetKey, final String target) {
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

        Spaceship.SpecialPowerState powerState;
        if(Gdx.input.isKeyPressed(boostKey)) {
            powerState = Spaceship.SpecialPowerState.BOOSTING;
        } else if(Gdx.input.isKeyPressed(dodgeKey)){
            powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
        }else {
            powerState = Spaceship.SpecialPowerState.NO_POWER;
        }

        final Spaceship.ShootingState shootingState;
        if(Gdx.input.isKeyPressed(shootKey)){
            shootingState = Spaceship.ShootingState.FIRING;
        }else{
            shootingState = Spaceship.ShootingState.PACIFIST;
        }

        connector.controlSpaceship(target, throttle, steering, powerState, shootingState);
    }



    @Override
    public void render(float delta) {
        handleInput();

        stage.act();
        stage.draw();


        SpaceSnapshot snapshot = connector.latestSnapshot();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        //        System.out.println("Myself is at " + mySelf.getPosition() + ", with an orientation of: " + mySelf.getOrientation() * 180 / Math.PI);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        SpaceSnapshot.GameObjectSnapshot myself = null;

        if (snapshot != null) {
            for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                if (mySelf.equals(object.getName())) {
                    camera.position.set(object.getPosition().x, object.getPosition().y, 100);
                    camera.up.set(1, 0, 0);
                    camera.rotate(object.getOrientation() * 180 / (float) Math.PI, 0, 0, 1);
                    camera.update();

                    myself = object;
                }

                if ("Player".equals(object.getType())) {
                    shapeRenderer.setColor(Color.GOLD);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 10);
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.circle(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()),
                             (10/2));
                }

                else if ("Bullet".equals(object.getType())) {
                    shapeRenderer.setColor(Color.CYAN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y,  2);

                }

                else if ("Asteroid".equals(object.getType())) {
                    shapeRenderer.setColor(Color.BROWN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.GREEN);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()),  (radius / 2));

                } else if ("ChaserEnemy".equals(object.getType())) {
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.circle(object.getPosition().x + 2 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius/2));

                }else if ("MotherShipEnemy".equals(object.getType())){
                    shapeRenderer.setColor(Color.CYAN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.ORANGE);
                    shapeRenderer.circle(object.getPosition().x + 6 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()),  (radius/2));

                }else if ("DodgingEnemy".equals(object.getType())){
                    shapeRenderer.setColor(Color.LIME);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.PINK);
                    shapeRenderer.circle(object.getPosition().x + 3 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius/2));
                }else if("MissileEnemy".equals(object.getType())){
                    shapeRenderer.setColor(Color.ORANGE);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.VIOLET);
                    shapeRenderer.circle(object.getPosition().x + 3 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius/2));

                }else if("Missile".equals(object.getType())){

                    shapeRenderer.setColor(Color.CYAN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y,  4);
                    //shapeRenderer.rect(object.getPosition().x, object.getPosition().y, 4, 4);
                }

                else {
                    shapeRenderer.setColor(Color.MAROON);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.TEAL);
                    shapeRenderer.circle(object.getPosition().x + 3 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius/2));
                }

            }
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(stage.getCamera().combined);
        hud.update(myself, delta);
        hud.stage.draw();

        //miniMap.apply();


    }

    @Override
    public void resize(int width, int height) {

        //miniMap.update(width, height);
    }

    @Override
    public void dispose() {

        stage.dispose();
        shapeRenderer.dispose();



    }


}