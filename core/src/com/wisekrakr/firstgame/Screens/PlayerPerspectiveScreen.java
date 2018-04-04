package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wisekrakr.firstgame.GamePadControls;
import com.wisekrakr.firstgame.GameState;
import com.wisekrakr.firstgame.PopUps.DamagePopUp;
import com.wisekrakr.firstgame.PopUps.PauseScreen;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.util.List;
import java.util.Random;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {

    private Label myselfLabel;

    private Hud hud;
    private PauseScreen pauseScreen;
    private SpriteBatch batch;
    private Stage stage;

    private OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;

    private BackgroundStars backgroundStars;
    private Texture backgroundTexture;

    private ClientConnector connector;

    private String mySelf;
    private List<String> players;
    private String first = null;
    private String second = null;

    private Controller controller;

    private Spaceship.SteeringState steering;
    private Spaceship.ThrottleState throttle;
    private Spaceship.ShootingState shootingState;
    private Spaceship.SpecialPowerState powerState;
    private Spaceship.AimingState aimingState;

    private GameState gameState = GameState.RUN;
    private boolean paused = false;
    private SpaceSnapshot.GameObjectSnapshot myself;

    /**
     * Stage for labels etc overlayed on the perspective screen, but using a hud-like orientation
     */
    private Stage overlayStage;

    public PlayerPerspectiveScreen(ClientConnector connector, List<String> players, String mySelf) {
        this.connector = connector;
        this.mySelf = mySelf;
        this.players = players;

        int i = 0;
        for (String name : players) {
            if (first == null) {
                first = name;
            } else if (second == null) {
                second = name;
            }
            connector.createSpaceship(name, 100 + i * 50, 0);
            i = i + 1;
        }
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        stage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera), batch);
        camera.zoom = 1.2f;

        backgroundTexture = new Texture(Gdx.files.internal("stars.jpg"));
        backgroundStars = new BackgroundStars(backgroundTexture);
        stage.addActor(backgroundStars);

        Gdx.input.setInputProcessor(stage);
        controllerInput();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        hud = new Hud();

        createOverlayHud();

        pauseScreen = new PauseScreen(batch);


    }

    private void controllerInput(){
        Controllers.addListener(new ControllerAdapter() {
            @Override
            public void connected(Controller controller) {
                if (PlayerPerspectiveScreen.this.controller == null) {
                    PlayerPerspectiveScreen.this.controller = controller;
                }
            }

            @Override
            public void disconnected(Controller controller) {
                if (PlayerPerspectiveScreen.this.controller == controller) {
                    PlayerPerspectiveScreen.this.controller = null;

                    if (Controllers.getControllers().size > 0) {
                        PlayerPerspectiveScreen.this.controller = Controllers.getControllers().first();
                    }
                }
            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                if (controller == PlayerPerspectiveScreen.this.controller) {
                    if (buttonCode == GamePadControls.BUTTON_B) {
                        powerState = Spaceship.SpecialPowerState.BOOSTING;
                        System.out.println("boooooost!");
                    }
                    if (buttonCode == GamePadControls.BUTTON_A) {
                        powerState = Spaceship.SpecialPowerState.BEAM;
                        System.out.println("beam!");
                    }
                    if (buttonCode == GamePadControls.BUTTON_RB) {
                        shootingState = Spaceship.ShootingState.FIRING;
                        System.out.println("pew");
                    }
                    if (buttonCode == GamePadControls.BUTTON_X) {
                        powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
                        System.out.println("the jukes!");
                    }
                    if (buttonCode == GamePadControls.BUTTON_LB) {
                        shootingState = Spaceship.ShootingState.MISSILE_FIRING;
                        System.out.println("firing missile");
                    }

                    if (buttonCode == GamePadControls.BUTTON_START) {
                        if (paused) {
                            gameState = GameState.RESUME;
                        } else {
                            gameState = GameState.PAUSE;
                        }
                        System.out.println("start button pushed");
                    }
                    if (buttonCode == GamePadControls.BUTTON_BACK) {
                        gameState = GameState.STOPPED;
                        System.out.println("back button pushed");
                    }

                    return false;
                }

                return true;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                if (axisCode == GamePadControls.AXIS_LEFT_Y) {

                }
                if (axisCode == GamePadControls.AXIS_LEFT_Y) {

                }
                if (axisCode == GamePadControls.AXIS_LEFT_X) {
                    steering = Spaceship.SteeringState.LEFT;
                }
                if (axisCode == GamePadControls.AXIS_LEFT_X) {
                    steering = Spaceship.SteeringState.RIGHT;
                }
                if (axisCode == GamePadControls.AXIS_RIGHT_X) {

                }
                if (axisCode == GamePadControls.AXIS_RIGHT_X) {

                }
                if (axisCode == GamePadControls.AXIS_RIGHT_TRIGGER) {
                    throttle = Spaceship.ThrottleState.FORWARDS;
                }
                if (axisCode == GamePadControls.AXIS_LEFT_TRIGGER) {
                    throttle = Spaceship.ThrottleState.REVERSE;
                }
                if (axisCode == GamePadControls.BUTTON_LB) {
                    shootingState = Spaceship.ShootingState.MISSILE_FIRING;
                }
                if (axisCode == GamePadControls.BUTTON_RB) {
                    shootingState = Spaceship.ShootingState.FIRING;
                }


                return false;
            }

            @Override
            public boolean povMoved(Controller controller, int povCode, PovDirection value) {

                if (value == GamePadControls.BUTTON_DPAD_UP) {
                    camera.zoom += 0.08;
                }
                if (value == GamePadControls.BUTTON_DPAD_DOWN) {
                    camera.zoom -= 0.08;
                }

                return false;
            }

        });

        if (Controllers.getControllers().size > 0) {
            controller = Controllers.getControllers().first();
        }
    }


    private void handleInput() {
        applyControl(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E, Input.Keys.Q, Input.Keys.C, Input.Keys.V, Input.Keys.X, first);
        //applyControl(controller, Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.ENTER, Input.Keys.O, Input.Keys.P, Input.Keys.COLON, Input.Keys.COMMA, second);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.zoom += 0.08;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
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

        throttle = Spaceship.ThrottleState.STATUSQUO;
        steering = Spaceship.SteeringState.CENTER;

        if (controller != null) {
            if (this.controller.getAxis(GamePadControls.AXIS_LEFT_TRIGGER) > 0.2f){
                throttle = Spaceship.ThrottleState.REVERSE;
                backgroundStars.setSpeed(backgroundStars.getSpeed() - 0.0007f);
            } else if (this.controller.getAxis(GamePadControls.AXIS_RIGHT_TRIGGER) < -0.2f){
                throttle = Spaceship.ThrottleState.FORWARDS;
                backgroundStars.setSpeed(backgroundStars.getSpeed() + 0.0007f);
            }
        }

        if (throttle == Spaceship.ThrottleState.STATUSQUO) {
            if (Gdx.input.isKeyPressed(forwardsKey)) {
                throttle = Spaceship.ThrottleState.FORWARDS;
                backgroundStars.setSpeed(backgroundStars.getSpeed() + 0.0007f);
            } else if (Gdx.input.isKeyPressed(reverseKey)) {
                throttle = Spaceship.ThrottleState.REVERSE;
                backgroundStars.setSpeed(backgroundStars.getSpeed() - 0.0007f);
            }
        }

        steering = Spaceship.SteeringState.CENTER;

        if (controller != null) {
            if (controller.getAxis(GamePadControls.AXIS_LEFT_X) > 0.2f) {
                steering = Spaceship.SteeringState.LEFT;
            } else if (controller.getAxis(GamePadControls.AXIS_LEFT_X) < -0.2f) {
                steering = Spaceship.SteeringState.RIGHT;
            }
        }

        if (steering == Spaceship.SteeringState.CENTER) {
            //final Spaceship.SteeringState steering;
            if (Gdx.input.isKeyPressed(leftKey)) {
                steering = Spaceship.SteeringState.LEFT;
            } else if (Gdx.input.isKeyPressed(rightKey)) {
                steering = Spaceship.SteeringState.RIGHT;
            }
        }

        powerState = Spaceship.SpecialPowerState.NO_POWER;

        if (controller != null) {
            if (controller.getButton(GamePadControls.BUTTON_B)) {
                powerState = Spaceship.SpecialPowerState.BOOSTING;
            } else if (controller.getButton(GamePadControls.BUTTON_X)) {
                powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
            } else if (controller.getButton(GamePadControls.BUTTON_A)) {
                powerState = Spaceship.SpecialPowerState.BEAM;
            }
        }

        if (powerState == Spaceship.SpecialPowerState.NO_POWER) {
            //Spaceship.SpecialPowerState powerState;
            if (Gdx.input.isKeyPressed(boostKey)) {
                powerState = Spaceship.SpecialPowerState.BOOSTING;
            } else if (Gdx.input.isKeyPressed(dodgeKey)) {
                powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
            }
        }

        shootingState = Spaceship.ShootingState.PACIFIST;

        if (controller != null) {
            if (this.controller.getButton(GamePadControls.BUTTON_RB)) {
                shootingState = Spaceship.ShootingState.FIRING;
            }else if (this.controller.getButton(GamePadControls.BUTTON_LB)) {
                shootingState = Spaceship.ShootingState.MISSILE_FIRING;
            }
        }

        //final Spaceship.ShootingState shootingState;
        if (shootingState == Spaceship.ShootingState.PACIFIST) {
            if (Gdx.input.isKeyPressed(shootKey)) {
                shootingState = Spaceship.ShootingState.FIRING;
            } else if (Gdx.input.isKeyPressed(altShootKey)) {
                shootingState = Spaceship.ShootingState.MISSILE_FIRING;
            }
        }

        aimingState = Spaceship.AimingState.NONE;



        connector.controlSpaceship(target, throttle, steering, powerState, shootingState, aimingState);
    }

    private void addBackground() {
        batch.begin();
        backgroundStars.draw(batch, 10);
        batch.end();
    }

    private void setPauseScreen() {
        pauseScreen.update();


    }

    private void createOverlayHud(){
        FileHandle fontStyle = Gdx.files.internal("myFont.fnt");
        BitmapFont font = new BitmapFont(fontStyle);
        font.getData().setScale(0.4f);

        overlayStage = new Stage();

        myselfLabel = new Label("Myself", new Label.LabelStyle(font, Color.WHITE));
        myselfLabel.setVisible(false);

        overlayStage.addActor(myselfLabel);
    }

    private void renderGameObjects() {
        SpaceSnapshot snapshot = connector.latestSnapshot();

        boolean foundMySelf = false;
        for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
            if (mySelf.equals(object.getName())) {
                camera.position.set(object.getPosition().x, object.getPosition().y, 100);
                camera.up.set(1, 0, 0);
                camera.rotate(object.getOrientation() * 180 / (float) Math.PI, 0, 0, 1);

                camera.update();
                foundMySelf = true;
                break;
            }
        }

        if (!foundMySelf) {
            myselfLabel.setText("My corpse");
        }
        else {
            myselfLabel.setText("My ship");
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        myself = null;
        if (snapshot != null) {
            for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                if (mySelf.equals(object.getName())) {
                    myself = object;

                    Vector3 projection = camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
                    myselfLabel.setVisible(true);
                    myselfLabel.setPosition(projection.x, projection.y + 30, Align.center);
                }

                if ("Player".equals(object.getType())) {
                    shapeRenderer.setColor(Color.GOLD);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 20f);
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.circle(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()),
                            (20f / 2));
                } else if ("BeamCannon".equals(object.getType())) {
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                } else if ("Exhaust".equals(object.getType())) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(4) + 1;
                    Color exhaustColor = new Color();

                    if (randomNumber == 1) {
                        exhaustColor.set(Color.YELLOW);
                    }
                    if (randomNumber == 2) {
                        exhaustColor.set(Color.GOLDENROD);
                    }
                    if (randomNumber == 3) {
                        exhaustColor.set(Color.ORANGE);
                    }
                    if (randomNumber == 4) {
                        exhaustColor.set(Color.SCARLET);
                    }
                    shapeRenderer.setColor(exhaustColor);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
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


                } else if ("EnemyPest".equals(object.getType())) {
                    shapeRenderer.setColor(Color.FIREBRICK);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.WHITE);
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
                }else if ("Shield".equals(object.getType())) {
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
    }

    private void updateOverlay() {
        overlayStage.draw();

    }


    private void updateHud(SpaceSnapshot.GameObjectSnapshot myself, float delta) {
        hud.update(myself, delta);
        hud.stage.draw();
    }

    @Override
    public void render(float delta) {
        switch (gameState) {
            case GAME_READY:
                break;

            case RUN:

                handleInput();

                stage.act();
                stage.draw();

                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                addBackground();

                renderGameObjects();

                updateHud(myself, delta);
                updateOverlay();

                break;

            case PAUSE:
                paused = true;
                setPauseScreen();
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

}