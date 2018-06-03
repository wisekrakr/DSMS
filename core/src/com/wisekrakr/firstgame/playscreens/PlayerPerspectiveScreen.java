package com.wisekrakr.firstgame.playscreens;


import com.badlogic.gdx.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wisekrakr.firstgame.*;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;
import com.wisekrakr.firstgame.input.GamePadControls;
import com.wisekrakr.firstgame.input.InputManager;
import com.wisekrakr.firstgame.overlays.AchievementTexts;
import com.wisekrakr.firstgame.overlays.EnemyHud;
import com.wisekrakr.firstgame.overlays.PlayerHud;
import com.wisekrakr.firstgame.overlays.ScreenHud;
import com.wisekrakr.firstgame.popups.PauseScreenAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {

    private ScreenHud screenHud;
    private AchievementTexts achievementTexts;
    private EnemyHud enemyHud;
    private PlayerHud playerHud;

    private SpriteBatch batch;
    private Stage stage;
    private PauseScreenAdapter pauseScreenAdapter;
    private BackgroundStars backgroundStars;

    private MyAssetManager myAssetManager;

    private OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;
    private Float radius;
    private Integer health;
    private float x;
    private float y;

    private ClientConnector connector;

    private String mySelf;
    private List<String> players;
    private String first = null;
    private String second = null;

    private Controller controller;
    private InputManager inputManager;
    private boolean up, down, left, right;

    private Spaceship.SteeringState steering;
    private Spaceship.ThrottleState throttle;
    private Spaceship.ShootingState shootingState;
    private Spaceship.SpecialPowerState powerState;
    private Spaceship.AimingState aimingState;
    private Spaceship.SwitchWeaponState switchWeaponState;

    private GameState gameState = GameState.RUN;
    private boolean paused = false;
    private SpaceSnapshot.GameObjectSnapshot myself;
    private SpaceSnapshot.GameObjectSnapshot enemy;
    private SpaceSnapshot.GameObjectSnapshot spaceObject;
    private GameObjectType gameObjectType;

    /**
     * Stage for labels etc overlayed on the perspective screen, but using a hud-like orientation
     */
    private Stage overlayStage;
    private Vector3 overlayCameraProjection;

    /**
     * Stage for background on the perspective screen, extending Actor class
     */
    private Stage backgroundStage;
    private BitmapFont font;
    private int chosenWeapon;
    private int chosenNumber;
    private List<Label> volatileLabels = new ArrayList<Label>();
    private List<ProgressBar> volatileBars = new ArrayList<ProgressBar>();
    private Random random = new Random();
    private Float hardSteering;

    private GameObjectRenderer gameObjectRenderer;
    private boolean foundMySelf;

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

        myAssetManager = new MyAssetManager();
        myAssetManager.loadFonts();
        myAssetManager.loadSounds();
        myAssetManager.loadTextures();
        myAssetManager.loadSkins();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        stage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera), batch);
        //stage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
        camera.zoom = 1.2f;

        backgroundStage = new Stage();
        Texture backgroundTexture = myAssetManager.assetManager.get("background/bg1.png", Texture.class);
        backgroundStars = new BackgroundStars(backgroundTexture);
        backgroundTexture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        backgroundStars.setSpeed(0.05f);

        //InputManager set as inputprocessor in show method
        inputManager = new InputManager();
        controllerInput();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        screenHud = new ScreenHud(myAssetManager);

        achievementTexts = new AchievementTexts(myAssetManager);
        enemyHud = new EnemyHud(camera);
        playerHud = new PlayerHud(camera);

        createOverlayHud();

        pauseScreenAdapter = new PauseScreenAdapter();



    }

    private void controllerInput() {
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
                    if (buttonCode == GamePadControls.BUTTON_Y) {

                    }
                    if (buttonCode == GamePadControls.BUTTON_X) {
                        setChosenWeapon(getChosenWeapon() + 1);
                        if (getChosenWeapon() >= 4) {
                            setChosenWeapon(0);
                        }
                        System.out.println("choosing weapon");
                    }
                    if (buttonCode == GamePadControls.BUTTON_A) {
                        if (getChosenWeapon() == 1) {
                            shootingState = Spaceship.ShootingState.FIRING;
                        }
                        if (getChosenWeapon() == 2) {
                            shootingState = Spaceship.ShootingState.MISSILE_FIRING;
                        }
                        if (getChosenWeapon() == 3) {
                            shootingState = Spaceship.ShootingState.PLACE_MINE;
                        }
                        System.out.println("pew");

                    }
                    if (buttonCode == GamePadControls.BUTTON_RB) {
                        throttle = Spaceship.ThrottleState.FULL_STOP;
                        System.out.println("FULL STOP!");
                    }
                    if (buttonCode == GamePadControls.BUTTON_LB) {
                        powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
                        System.out.println("the jukes!");
                    }

                    if (buttonCode == GamePadControls.BUTTON_START) {
                        if (paused) {
                            gameState = GameState.RESUME;
                        } else {
                            gameState = GameState.PAUSE;
                        }
                        System.out.println("Paused");
                    }
                    if (buttonCode == GamePadControls.BUTTON_BACK) {
                        gameState = GameState.STOPPED;
                        System.out.println("Resumed");
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
        applyControl(first);

        //Keyboard input
        if (inputManager.isKeyDown(Input.Keys.UP)) {
            camera.zoom += 0.08;
        }
        if (inputManager.isKeyDown(Input.Keys.DOWN)) {
            camera.zoom -= 0.08;
        }
        if (inputManager.isKeyDown(Input.Keys.ESCAPE)) {
            if (paused) {
                gameState = GameState.RESUME;
            } else {
                gameState = GameState.PAUSE;
            }
            System.out.println("Paused");
        }
        if (inputManager.isKeyDown(Input.Keys.BACKSPACE)) {
            gameState = GameState.STOPPED;
            System.out.println("Stopped");
        }
        if (inputManager.isKeyDown(Input.Keys.P)){
            if (paused) {
                gameState = GameState.RESUME;
            } else {
                gameState = GameState.PAUSE;
            }
            System.out.println("Paused");
        }

        //Mouse input
        inputManager.getTouchState(0);

        if (inputManager.isTouchPressed(0)) {
            System.out.println("PRESSED");

            Vector3 mouseProjection = camera.project(new Vector3(0, 0, 100));
            float touchCoordinates = (float) Math.sqrt(inputManager.touchCoordX(0) + inputManager.touchCoordY(0));
            this.hardSteering = (float) Math.atan2(inputManager.touchCoordX(0) - mouseProjection.x, inputManager.touchCoordY(0) - mouseProjection.y);

        }

        if (inputManager.isTouchDown(0)) {
            System.out.println("DOWN");
            System.out.println("Touch coordinates: " + inputManager.touchCoordX(0) + ", " + inputManager.touchCoordY(0));
            System.out.println("Touch displacement" + inputManager.touchDisplacementX(0) + ", " + inputManager.touchDisplacementY(0));

        }

        if (inputManager.isTouchReleased(0)) {
            System.out.println("RELEASED");

        }

        inputManager.update();
    }

    public int getChosenWeapon() {
        return chosenWeapon;
    }

    public void setChosenWeapon(int chosenWeapon) {
        this.chosenWeapon = chosenWeapon;
    }

    private void applyControl(final String target) {

        throttle = Spaceship.ThrottleState.STATUSQUO;

        //Gamepad/Controller

        if (controller != null) {
            if (this.controller.getAxis(GamePadControls.AXIS_LEFT_TRIGGER) > 0.2f) {
                throttle = Spaceship.ThrottleState.REVERSE;
            } else if (this.controller.getAxis(GamePadControls.AXIS_RIGHT_TRIGGER) < -0.2f) {
                throttle = Spaceship.ThrottleState.FORWARDS;
            } else if (controller.getButton(GamePadControls.BUTTON_RB)) {
                throttle = Spaceship.ThrottleState.FULL_STOP;
            }
        }

        //Keyboard

        if (throttle == Spaceship.ThrottleState.STATUSQUO) {
            if (inputManager.isKeyDown(Input.Keys.W)) {
                throttle = Spaceship.ThrottleState.FORWARDS;
                System.out.println("go");
            } else if (inputManager.isKeyDown(Input.Keys.S)) {
                throttle = Spaceship.ThrottleState.REVERSE;
            }
        }

        steering = Spaceship.SteeringState.CENTER;

        //Gamepad/Controller

        if (controller != null) {
            if (controller.getAxis(GamePadControls.AXIS_LEFT_X) > 0.2f) {
                steering = Spaceship.SteeringState.LEFT;
                backgroundStars.setRotation(0.5f);
            } else if (controller.getAxis(GamePadControls.AXIS_LEFT_X) < -0.2f) {
                steering = Spaceship.SteeringState.RIGHT;
                backgroundStars.setRotation(-0.5f);
            }
        }

        //Keyboard

        if (steering == Spaceship.SteeringState.CENTER) {
            if (inputManager.isKeyDown(Input.Keys.A)) {
                steering = Spaceship.SteeringState.LEFT;
            } else if (inputManager.isKeyDown(Input.Keys.D)) {
                steering = Spaceship.SteeringState.RIGHT;
            }
        }

        powerState = Spaceship.SpecialPowerState.NO_POWER;

        //Gamepad/Controller

        if (controller != null) {
            if (controller.getButton(GamePadControls.BUTTON_B)) {
                powerState = Spaceship.SpecialPowerState.BOOSTING;
            } else if (controller.getButton(GamePadControls.BUTTON_LB)) {
                powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
            }
        }

        //Keyboard

        if (powerState == Spaceship.SpecialPowerState.NO_POWER) {
            if (inputManager.isKeyDown(Input.Keys.E)) {
                powerState = Spaceship.SpecialPowerState.BOOSTING;
            } else if (inputManager.isKeyDown(Input.Keys.Q)) {
                powerState = Spaceship.SpecialPowerState.ULTRA_DODGE;
            }
        }

        shootingState = Spaceship.ShootingState.PACIFIST;
        switchWeaponState = Spaceship.SwitchWeaponState.NONE;

        switch (chosenWeapon) {
            case 0:
                setSwitchWeaponState(Spaceship.SwitchWeaponState.NONE);
                break;
            case 1:
                setSwitchWeaponState(Spaceship.SwitchWeaponState.BULLETS);
                break;
            case 2:
                setSwitchWeaponState(Spaceship.SwitchWeaponState.MISSILES);
                break;
            case 3:
                setSwitchWeaponState(Spaceship.SwitchWeaponState.SPACE_MINES);
                break;

        }

        //Gamepad/Controller

        if (controller != null) {
            if (this.controller.getButton(GamePadControls.BUTTON_X)) {
                setChosenWeapon(getChosenWeapon() + 1);
                if (getChosenWeapon() >= 4) {
                    setChosenWeapon(0);
                }
            } else if (this.controller.getButton(GamePadControls.BUTTON_A)) {
                if (getChosenWeapon() == 1) {
                    shootingState = Spaceship.ShootingState.FIRING;
                }
                if (getChosenWeapon() == 2) {
                    shootingState = Spaceship.ShootingState.MISSILE_FIRING;
                }
                if (getChosenWeapon() == 3) {
                    shootingState = Spaceship.ShootingState.PLACE_MINE;
                }
            }

        }

        //Keyboard
        int a = getChosenWeapon();

        if (inputManager.isKeyPressed(Input.Keys.F)) {
            setChosenWeapon(getChosenWeapon() + 1);
            if (getChosenWeapon() >= 4) {
                setChosenWeapon(0);
            }
        }

        //Keyboard

        if (shootingState == Spaceship.ShootingState.PACIFIST) {
            if (inputManager.isKeyDown(Input.Keys.C)) {
                if (getChosenWeapon() == 1) {
                    shootingState = Spaceship.ShootingState.FIRING;
                }
                if (getChosenWeapon() == 2) {
                    shootingState = Spaceship.ShootingState.MISSILE_FIRING;
                }
                if (getChosenWeapon() == 3) {
                    shootingState = Spaceship.ShootingState.PLACE_MINE;
                }
            }
        }

        //Mouse
/*
        if (shootingState == Spaceship.ShootingState.PACIFIST) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (getChosenWeapon() == 1) {
                    shootingState = Spaceship.ShootingState.FIRING;
                }
                if (getChosenWeapon() == 2) {
                    shootingState = Spaceship.ShootingState.MISSILE_FIRING;
                }
                if (getChosenWeapon() == 3) {
                    shootingState = Spaceship.ShootingState.PLACE_MINE;
                }
            }
        }
*/
        aimingState = Spaceship.AimingState.NONE;

        connector.controlSpaceship(target, throttle, steering, powerState, shootingState, aimingState, switchWeaponState, hardSteering);

        hardSteering = null;
    }

    private void addSoundEffects() {
        if (shootingState == Spaceship.ShootingState.FIRING) {
            Sound pew = myAssetManager.assetManager.get("sound/photon1.wav", Sound.class);
            pew.play(0.1f);
        }
        if (shootingState == Spaceship.ShootingState.MISSILE_FIRING) {
            Sound pew = myAssetManager.assetManager.get("sound/photon2.wav", Sound.class);
            pew.play(0.1f);
        }
        if (shootingState == Spaceship.ShootingState.PLACE_MINE) {
//            Sound boom = myAssetManager.assetManager.get("sound/mine_blowup.mp3", Sound.class);
            //boom.play();
//            boom.play(0.1f);
        }
        if (powerState == Spaceship.SpecialPowerState.BOOSTING) {
            Sound acc = myAssetManager.assetManager.get("sound/acc1.mp3", Sound.class);
            acc.play(0.1f, 1.6f, 2f);
        }
    }


    private void renderGameObjects() {
        for (Label label : volatileLabels) {
            label.remove();
        }
        for (ProgressBar progressBar : volatileBars){
            progressBar.remove();
        }

        SpaceSnapshot snapshot = connector.latestSnapshot();

        foundMySelf = false;
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

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        myself = null;
        enemy = null;
        spaceObject = null;


        if (snapshot != null) {
            for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                if (mySelf.equals(object.getName())) {
                    myself = object;

                    Vector3 projection = camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));



                }

                overlayCameraProjection = camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
                radius = (Float) object.extraProperties().get("radius");
                //health = (Integer) object.healthProperties().get("health");
                x = object.getPosition().x;
                y = object.getPosition().y;

                switch (object.getType()) {
                    case POWERUP_GENERATOR:
                        // TODO: remove the need for the power up generator

                        break;

                    case SPACESHIP:
                        myself = object;

                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 20f);
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()),
                                object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()),
                                (20f / 2));
                /*
                        Label playerLabel = playerHud.nameLabel(object);
                        ProgressBar bar = playerHud.progressBar(object);
                        overlayStage.addActor(playerLabel);
                        overlayStage.addActor(bar);
                        volatileLabels.add(playerLabel);
*/
                        break;

                    case SPACE_MINE:
                        spaceObject = object;
                        Color blinkingColor = chooseRandomColor(new Color[]{Color.RED, Color.WHITE, Color.WHITE, Color.RED});
                        shapeRenderer.setColor(blinkingColor);
                        shapeRenderer.circle(x, y, radius);

                        Boolean isDestruct = (Boolean) object.randomProperties().get("isDestruct");
                        if (isDestruct) {
                            Sound boom = myAssetManager.assetManager.get("sound/mine_blowup.mp3", Sound.class);
                            boom.play(1f);
                        }
                        break;
                    case BULLET:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.CYAN);
                        shapeRenderer.circle(x, y, radius);

                        break;
                    case ASTEROID:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.BROWN);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        break;
                    case ROTUNDA:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x, y, radius);
                        break;
                    case ENEMY_CHASER:
                        enemy = object;
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        //Label chaserHealthLabel = enemyHud.healthLabel(object);
                        Label chaserNameLabel = enemyHud.nameLabel(object);
                        //overlayStage.addActor(chaserHealthLabel);
                        overlayStage.addActor(chaserNameLabel);
                        //volatileLabels.add(chaserHealthLabel);
                        volatileLabels.add(chaserNameLabel);

                        ProgressBar healthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(healthBar);
                        volatileBars.add(healthBar);

                        break;

                    case ELS:
                        enemy = object;
                        shapeRenderer.setColor(Color.SKY);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label elsHealthLabel = enemyHud.healthLabel(object);
                        Label elsNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(elsNameLabel);
                        overlayStage.addActor(elsHealthLabel);
                        volatileLabels.add(elsNameLabel);
                        volatileLabels.add(elsHealthLabel);
                        break;
                    case FACE_HUGGER:
                        enemy = object;
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x + (radius / 3) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 3) * (float) Math.sin(object.getOrientation()), (radius / 3));

                        Label flybyNameLabel = enemyHud.nameLabel(object);
                        Label flybyHealthLabel = enemyHud.healthLabel(object);
                        overlayStage.addActor(flybyNameLabel);
                        overlayStage.addActor(flybyHealthLabel);
                        volatileLabels.add(flybyNameLabel);
                        volatileLabels.add(flybyHealthLabel);
                        break;
                    case GANG:
                        enemy = object;
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.PURPLE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));


                        break;
                    case SHITTER:
                        enemy = object;
                        shapeRenderer.setColor(Color.LIGHT_GRAY);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.SLATE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));
                        break;
                    case PEST:
                        enemy = object;
                        shapeRenderer.setColor(Color.FIREBRICK);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));
                        break;

                    case BLINKER:
                        enemy = object;
                        shapeRenderer.setColor(Color.GOLDENROD);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        break;

                    case LASER_BEAM:
                        spaceObject = object;
                        Color bulletColor = chooseRandomColor(BULLET_COLORS);
                        shapeRenderer.setColor(bulletColor);
                        shapeRenderer.rectLine(x, y,x + 25 * (float) Math.cos(object.getOrientation()),
                                y + 25 * (float) Math.sin(object.getOrientation()), 2);
                        break;

                    case MOTHERSHIP:
                        enemy = object;
                        shapeRenderer.setColor(Color.CYAN);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.ORANGE);
                        shapeRenderer.circle(x + 6 * (float) Math.cos(object.getOrientation()),
                                y + 2 * (float) Math.sin(object.getOrientation()), (radius / 2));

                        break;

                    case DODGER:
                        enemy = object;
                        shapeRenderer.setColor(Color.LIME);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.PINK);
                        shapeRenderer.circle(x + 3 * (float) Math.cos(object.getOrientation()),
                                y + 2 * (float) Math.sin(object.getOrientation()), (radius / 2));

                        break;

                    case HOMER:
                        enemy = object;
                        shapeRenderer.setColor(Color.ORANGE);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.VIOLET);
                        shapeRenderer.circle(x + 3 * (float) Math.cos(object.getOrientation()),
                                y + 2 * (float) Math.sin(object.getOrientation()), (radius / 2));

                        break;
                    case MISSILE:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, radius);
                        break;
                    case MUTATOR:
                        enemy = object;
                        shapeRenderer.setColor(Color.FIREBRICK);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.ORANGE);
                        shapeRenderer.circle(x + 60 * (float) Math.cos(object.getOrientation()),
                                y + 20 * (float) Math.sin(object.getOrientation()), (radius / 2));

                        break;

                    case SPORE:
                        spaceObject = object;
                        Color sporeColor = chooseRandomColor(SPORE_COLORS);
                        shapeRenderer.setColor(sporeColor);
                        shapeRenderer.circle(x, y, radius);
                        break;

                    case SHOTTY:
                        enemy = object;
                        shapeRenderer.setColor(Color.MAROON);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.TEAL);
                        shapeRenderer.circle(x + 3 * (float) Math.cos(object.getOrientation()),
                                y + 2 * (float) Math.sin(object.getOrientation()), radius / 2);

                        break;

                    case POWERUP_MISSILE:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(x, y, 28);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, 22);
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(x, y, 15 / 2);

                        break;
                    case POWERUP_SHIELD:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, 30);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, 22);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, 15 / 2);

                        break;

                    case POWERUP_MINION:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, 30);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, 22);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, 15 / 2);

                        break;

                    case POWERUP_HEALTH:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, 30);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, 22);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, 15 / 2);
                        break;

                    case MINION_SHOOTER:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.SKY);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        break;
                    case MINION_FIGHTER:
                        spaceObject = object;
                        shapeRenderer.setColor(Color.SKY);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));
                        break;
                    case SHIELD:
                        spaceObject = object;
                        String lightBlue = "8EE2EC";
                        shapeRenderer.setColor(Color.valueOf(lightBlue));
                        shapeRenderer.circle(x, y, radius);
                        break;

                    case EXHAUST:
                        spaceObject = object;
                        Color exhaustColor = chooseRandomColor(EXHAUST_COLORS);
                        shapeRenderer.setColor(exhaustColor);
                        shapeRenderer.circle(x, y, radius);
                        break;

                    case DEBRIS:
                        spaceObject = object;
                        Color debrisColor = chooseRandomColor(DEBRIS_COLORS);
                        shapeRenderer.setColor(debrisColor);
                        shapeRenderer.circle(x, y, radius);
                        break;


                    default:
                        System.out.println("Unknown game object type: " + object.getType());
                }
            }
        }

        shapeRenderer.end();
    }

    private static final String yellowish = "EDE49E";
    private static final String reddish = "F88158";

    private static final Color[] SPORE_COLORS = {
            Color.PURPLE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN
    };


    private static final Color[] DEBRIS_COLORS = {
            Color.valueOf(yellowish),
            Color.DARK_GRAY,
            Color.LIGHT_GRAY,
            Color.valueOf(reddish)
    };

    private static final Color[] EXHAUST_COLORS = {
            Color.YELLOW,
            Color.GOLDENROD,
            Color.ORANGE,
            Color.SCARLET
    };

    private static final Color[] BULLET_COLORS = {
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN
    };


    private Color chooseRandomColor(Color[] randomColors) {
        return randomColors[random.nextInt(randomColors.length)];
    }



    private void addBackground() {

        //backgroundStage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
        backgroundStage.getBatch().begin();
        backgroundStars.draw(backgroundStage.getBatch(), 10);
        backgroundStage.getBatch().end();

        backgroundStage.addActor(backgroundStars);

    }

    private void setPauseScreen() {
        //pauseScreen.update();
        pauseScreenAdapter.render();

    }

    private void createOverlayHud() {
        font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);
        overlayStage = new Stage();
    }

    private void updateOverlay() {
        overlayStage.draw();
    }

    private void updateHud(SpaceSnapshot.GameObjectSnapshot myself, float delta) {
        screenHud.update(myself, delta);
        screenHud.stage.draw();
        achievementTexts.update(myself, delta);
        achievementTexts.stage.draw();


    }

    @Override
    public void render(float delta) {
        switch (gameState) {
            case GAME_READY:
                break;

            case RUN:
                handleInput();
                addSoundEffects();

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
                Gdx.app.getApplicationListener().pause();
                setPauseScreen();
                break;

            case RESUME:
                paused = false;
                Gdx.app.getApplicationListener().resume();
                setGameState(GameState.RUN);

                break;

            case STOPPED:
                Gdx.app.exit();
                break;

        }

    }

    public Vector3 getOverlayCameraProjection() {
        return overlayCameraProjection;
    }

    public void setOverlayCameraProjection(Vector3 overlayCameraProjection) {
        this.overlayCameraProjection = overlayCameraProjection;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setSwitchWeaponState(Spaceship.SwitchWeaponState switchWeaponState) {
        this.switchWeaponState = switchWeaponState;
    }

    public Spaceship.ShootingState getShootingState() {
        return shootingState;
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputManager);

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

        backgroundStage.dispose();
        stage.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        myAssetManager.dispose();

    }

}