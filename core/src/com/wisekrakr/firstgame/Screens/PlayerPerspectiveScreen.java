package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.GamePadControls;
import com.wisekrakr.firstgame.GameState;
import com.wisekrakr.firstgame.PopUps.PauseScreen;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter implements ControllerListener{

    private Hud hud;
    private SpriteBatch batch;
    private Stage stage;

    private OrthographicCamera camera;
    private OrthographicCamera minimapcamera;

    private SpaceGameContainer container;

    private ShapeRenderer shapeRenderer;
    private ShapeRenderer miniMapShapeRender;

    private BackgroundStars backgroundStars;
    private Texture backgroundTexture;

    private ClientConnector connector;
    private String mySelf;
    private List<String> players;
    private String first = null;
    private String second = null;

    private boolean hasController = true;
    private Controller controller;

    private Spaceship.SteeringState steering;
    private Spaceship.ThrottleState throttle;
    private Spaceship.ShootingState shootingState;
    private Spaceship.SpecialPowerState powerState;

    private GameState gameState = GameState.RUN;
    private boolean paused = false;

    public PlayerPerspectiveScreen(ClientConnector connector, List<String> players, String mySelf) {
        this.connector = connector;
        this.mySelf = mySelf;
        this.players = players;

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
/*
        minimapcamera = new OrthographicCamera();
        minimapcamera.setToOrtho(false, width, height);
        minimapcamera.update();
*/
//TODO: see how we can create a background....either by using stage like now, or to use another camera
        backgroundTexture = new Texture(Gdx.files.internal("stars.jpg"));
        backgroundTexture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);

        backgroundStars = new BackgroundStars(backgroundTexture);

        stage.addActor(backgroundStars);

        Gdx.input.setInputProcessor(stage);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

//        miniMapShapeRender = new ShapeRenderer();
//        miniMapShapeRender.setAutoShapeType(true);

        batch = new SpriteBatch();

        hud = new Hud(batch);

        Controllers.addListener(this);

        if(Controllers.getControllers().size == 0)
        {
            hasController = false;
            System.out.println("no controller detected");
        }else {
            controller = Controllers.getControllers().first();
        }
//Todo: make a pausescreen .... this here does not work ... use the pausescreeen class.


    }



    private void handleInput() {
        applyControl(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E, Input.Keys.Q, Input.Keys.C, Input.Keys.V, Input.Keys.X, first);
        //applyControl(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.ENTER, Input.Keys.O, Input.Keys.P, Input.Keys.COLON, Input.Keys.COMMA, second);

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

    private void applyControl(int forwardsKey, int reverseKey, int leftKey, int rightKey, int boostKey, int dodgeKey,
                              int shootKey, int altShootKey, int resetKey, final String target) {
        /*
        if (Gdx.input.isKeyPressed(resetKey)) {
            target.setPosition(new Vector2(0, 0));
            target.resetControl();
        }
        */

        //final Spaceship.ThrottleState throttle;
        if (Gdx.input.isKeyPressed(forwardsKey) || controller.getAxis(GamePadControls.AXIS_LEFT_Y) < -0.2f ) {
            throttle = Spaceship.ThrottleState.FORWARDS;
            backgroundStars.setSpeed(0.1f);
        } else if (Gdx.input.isKeyPressed(reverseKey) || controller.getAxis(GamePadControls.AXIS_LEFT_Y )> 0.2f) {
            throttle = Spaceship.ThrottleState.REVERSE;
        } else {
            throttle = Spaceship.ThrottleState.STATUSQUO;
        }

        //final Spaceship.SteeringState steering;
        if (Gdx.input.isKeyPressed(leftKey) || controller.getAxis(GamePadControls.AXIS_LEFT_X) > 0.2f) {
            steering = Spaceship.SteeringState.LEFT;
        } else if (Gdx.input.isKeyPressed(rightKey) || controller.getAxis(GamePadControls.AXIS_LEFT_X) < -0.2f) {
            steering = Spaceship.SteeringState.RIGHT;
        } else {
            steering = Spaceship.SteeringState.CENTER;
        }

        //Spaceship.SpecialPowerState powerState;
        if(Gdx.input.isKeyPressed(boostKey) || controller.getButton(GamePadControls.BUTTON_B )) {
            powerState = Spaceship.SpecialPowerState.BOOSTING;
        } else if(Gdx.input.isKeyPressed(dodgeKey) || controller.getButton(GamePadControls.BUTTON_X )){
            powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
        }else {
            powerState = Spaceship.SpecialPowerState.NO_POWER;
        }

        //final Spaceship.ShootingState shootingState;
        if(Gdx.input.isKeyPressed(shootKey) || controller.getAxis(GamePadControls.AXIS_RIGHT_TRIGGER ) < -0.2f){
            shootingState = Spaceship.ShootingState.FIRING;
        }else if(Gdx.input.isKeyPressed(altShootKey) || controller.getButton(GamePadControls.BUTTON_RB )){
            shootingState = Spaceship.ShootingState.MISSILE_FIRING;
        }else {
            shootingState = Spaceship.ShootingState.PACIFIST;
        }

        connector.controlSpaceship(target, throttle, steering, powerState, shootingState);
    }



    @Override
    public void render(float delta) {
        switch (gameState) {
            case RUN:
                handleInput();

                stage.act();
                stage.draw();

                SpaceSnapshot snapshot = connector.latestSnapshot();

                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                batch.begin();
                backgroundStars.draw(batch, 10);
                batch.end();

                shapeRenderer.setProjectionMatrix(camera.combined);
//        miniMapShapeRender.setProjectionMatrix(minimapcamera.combined);

                //        System.out.println("Myself is at " + mySelf.getPosition() + ", with an orientation of: " + mySelf.getOrientation() * 180 / Math.PI);

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        miniMapShapeRender.begin(ShapeRenderer.ShapeType.Filled);

                SpaceSnapshot.GameObjectSnapshot myself = null;

                if (snapshot != null) {
                    for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                        if (mySelf.equals(object.getName())) {
                            camera.position.set(object.getPosition().x, object.getPosition().y, 100);
                            camera.up.set(1, 0, 0);
                            camera.rotate(object.getOrientation() * 180 / (float) Math.PI, 0, 0, 1);
                            camera.update();

                            //backgroundStars.rotation = object.getOrientation() ;
/*
                    minimapcamera.position.set(object.getPosition().x, object.getPosition().y, 100);
                    minimapcamera.up.set(1, 0, 0);
                    minimapcamera.translate(-1000,-1000);
                    minimapcamera.update();
*/
                            myself = object;
                        }

                        if ("Player".equals(object.getType())) {
                            shapeRenderer.setColor(Color.GOLD);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 20f);
                            shapeRenderer.setColor(Color.BLUE);
                            shapeRenderer.circle(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()),
                                    (20f / 2));

/*
                    miniMapShapeRender.setColor(Color.GOLD);
                    miniMapShapeRender.set(ShapeRenderer.ShapeType.Filled);
                    miniMapShapeRender.circle(object.getPosition().x, object.getPosition().y, 10);
                    miniMapShapeRender.setColor(Color.BLUE);
                    miniMapShapeRender.circle(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()),
                            (10/2));
*/
                        } else if ("VisionCone".equals(object.getType())) {
                            shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);


                        } else if ("BulletPlayer".equals(object.getType())) {
                            shapeRenderer.setColor(Color.CYAN);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                        } else if ("BulletEnemy".equals(object.getType())) {

                            shapeRenderer.setColor(Color.YELLOW);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                        } else if ("Asteroid".equals(object.getType())) {
                            shapeRenderer.setColor(Color.BROWN);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.GREEN);
                            shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        } else if ("EnemyChaser".equals(object.getType())) {
                            shapeRenderer.setColor(Color.RED);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.BLUE);
                            shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        } else if ("EnemyBlinker".equals(object.getType())) {
                            shapeRenderer.setColor(Color.GOLDENROD);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.RED);
                            shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        } else if ("LaserBeamEnemy".equals(object.getType())) {
                            Random random = new Random();
                            int randomNumber = random.nextInt(4) + 1;
                            Color bulletColor = new Color();

                            if (randomNumber == 1) {
                                bulletColor.set(Color.BLUE);
                            }
                            if (randomNumber == 2) {
                                bulletColor.set(Color.RED);
                            }
                            if (randomNumber == 3) {
                                bulletColor.set(Color.YELLOW);
                            }
                            if (randomNumber == 4) {
                                bulletColor.set(Color.GREEN);
                            }

                            shapeRenderer.setColor(bulletColor);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.rectLine(object.getPosition().x, object.getPosition().y,
                                    object.getPosition().x + 25 * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + 25 * (float) Math.sin(object.getOrientation()), 2);

                        } else if ("EnemyMotherShip".equals(object.getType())) {
                            shapeRenderer.setColor(Color.CYAN);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.ORANGE);
                            shapeRenderer.circle(object.getPosition().x + 6 * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius / 2));

                        } else if ("EnemyDodger".equals(object.getType())) {
                            shapeRenderer.setColor(Color.LIME);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.PINK);
                            shapeRenderer.circle(object.getPosition().x + 3 * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius / 2));
                        } else if ("EnemyHomer".equals(object.getType())) {
                            shapeRenderer.setColor(Color.ORANGE);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.VIOLET);
                            shapeRenderer.circle(object.getPosition().x + 3 * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius / 2));

                        } else if ("MissileEnemy".equals(object.getType())) {

                            shapeRenderer.setColor(Color.RED);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                        } else if ("EnemyMutator".equals(object.getType())) {
                            shapeRenderer.setColor(Color.FIREBRICK);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.ORANGE);
                            shapeRenderer.circle(object.getPosition().x + 60 * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + 20 * (float) Math.sin(object.getOrientation()), (radius / 2));
                        } else if ("Spores".equals(object.getType())) {
                            Random random = new Random();
                            int randomNumber = random.nextInt(4) + 1;
                            Color sporeColor = new Color();

                            if (randomNumber == 1) {
                                sporeColor.set(Color.PURPLE);
                            }
                            if (randomNumber == 2) {
                                sporeColor.set(Color.RED);
                            }
                            if (randomNumber == 3) {
                                sporeColor.set(Color.YELLOW);
                            }
                            if (randomNumber == 4) {
                                sporeColor.set(Color.GREEN);
                            }

                            shapeRenderer.setColor(sporeColor);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                        } else if ("EnemyShotty".equals(object.getType())) {
                            shapeRenderer.setColor(Color.MAROON);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                            shapeRenderer.setColor(Color.TEAL);
                            shapeRenderer.circle(object.getPosition().x + 3 * (float) Math.cos(object.getOrientation()),
                                    object.getPosition().y + 2 * (float) Math.sin(object.getOrientation()), (radius / 2));

                        } else if ("MissilePlayer".equals(object.getType())) {
                            shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                        } else if ("PowerUpMissile".equals(object.getType())) {
                            shapeRenderer.setColor(Color.GOLD);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 28);
                            shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 25);
                            shapeRenderer.setColor(Color.GOLD);
                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 20 / 2);
                        } else if ("PowerUpShield".equals(object.getType())) {
                            shapeRenderer.setColor(Color.RED);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 40);
                            shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 25);
                            shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 20 / 2);
                        } else if ("Shield".equals(object.getType())) {
                            String lightBlue = "8EE2EC";
                            shapeRenderer.setColor(Color.valueOf(lightBlue));
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                        } else if ("Debris".equals(object.getType())) {
                            Random random = new Random();
                            int randomNumber = random.nextInt(4) + 1;
                            Color debrisColor = new Color();

                            if (randomNumber == 1) {
                                String yellowish = "EDE49E";
                                debrisColor.set(Color.valueOf(yellowish));
                            }
                            if (randomNumber == 2) {
                                debrisColor.set(Color.DARK_GRAY);
                            }
                            if (randomNumber == 3) {
                                debrisColor.set(Color.LIGHT_GRAY);
                            }
                            if (randomNumber == 4) {
                                String reddish = "F88158";
                                debrisColor.set(Color.valueOf(reddish));
                            }

                            Float radius = (Float) object.extraProperties().get("radius");

                            shapeRenderer.setColor(debrisColor);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                        }

                    }
                }

                shapeRenderer.end();
//        miniMapShapeRender.end();

                batch.setProjectionMatrix(stage.getCamera().combined);
                hud.update(myself, delta);
                hud.stage.draw();

                break;

            case PAUSE:
                paused = true;
                container.setScreen(new PauseScreen(container, camera));
                break;
            case RESUME:
                paused = false;
                setGameState(GameState.RUN);
                break;
            case STOPPED:
                Gdx.app.exit();
                break;

        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        this.gameState = GameState.PAUSE;
    }

    @Override
    public void resume() {
        this.gameState = GameState.RESUME;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void dispose() {

        stage.dispose();
        shapeRenderer.dispose();
 //       miniMapShapeRender.dispose();
        batch.dispose();

    }

//Todo: Fix how the controller connects to the game.
    @Override
    public void connected(Controller controller) {
        hasController = true;
    }

    @Override
    public void disconnected(Controller controller) {
        hasController = false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        if(buttonCode == GamePadControls.BUTTON_B){
            powerState = Spaceship.SpecialPowerState.BOOSTING;
            System.out.println("boooooost!");
        }
        if(buttonCode == GamePadControls.BUTTON_RB){
            shootingState = Spaceship.ShootingState.MISSILE_FIRING;
            System.out.println("firing missile");
        }
        if(buttonCode == GamePadControls.BUTTON_X){
            powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
            System.out.println("the jukes!");
        }

        if(buttonCode == GamePadControls.BUTTON_START){
            if(paused){
                gameState = GameState.RESUME;
            }else {
                gameState = GameState.PAUSE;
            }
            System.out.println("start button pushed");
        }
        if(buttonCode == GamePadControls.BUTTON_BACK){
            gameState = GameState.STOPPED;
            System.out.println("back button pushed");
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {

        if(axisCode == GamePadControls.AXIS_LEFT_Y){
            throttle = Spaceship.ThrottleState.FORWARDS;
        }
        if(axisCode == GamePadControls.AXIS_LEFT_Y){
            throttle = Spaceship.ThrottleState.REVERSE;
        }
        if(axisCode == GamePadControls.AXIS_LEFT_X){
            steering = Spaceship.SteeringState.LEFT;
        }
        if(axisCode == GamePadControls.AXIS_LEFT_X){
            steering = Spaceship.SteeringState.RIGHT;
        }
        if(axisCode == GamePadControls.AXIS_RIGHT_TRIGGER){
            shootingState = Spaceship.ShootingState.FIRING;
            System.out.println("pew pew");
        }

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {

        if(value == GamePadControls.BUTTON_DPAD_UP){
            camera.zoom += 0.08;
        }
        if(value == GamePadControls.BUTTON_DPAD_DOWN){
            camera.zoom -= 0.08;
        }

        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}