package com.wisekrakr.firstgame.playscreens;


import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wisekrakr.firstgame.Box2dBodyCreator;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.SpriteHelper;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;
import com.wisekrakr.firstgame.input.GamePadControls;
import com.wisekrakr.firstgame.input.InputManager;
import com.wisekrakr.firstgame.overlays.*;
import com.wisekrakr.firstgame.quests.MissionText;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {

    private Box2dBodyCreator box2dBodyCreator;
    private List<Body>bodies = new ArrayList<Body>();

    private InputMultiplexer inputMultiplexer;

    private ScreenHud screenHud;
    private EnemyHud enemyHud;
    private PlayerHud playerHud;

    private SpriteBatch batch;
    private Stage stage;

    private PauseScreenAdapter pauseScreenAdapter;
    private BackgroundStars backgroundStars;

    private MyAssetManager myAssetManager;

    private OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;

    private ClientConnector connector;

    private String mySelf;
    private List<String> players;
    private String first = null;
    private String second = null;

    private Controller controller;
    private InputManager inputManager;

    private Spaceship.SteeringState steering;
    private Spaceship.ThrottleState throttle;
    private Spaceship.ShootingState shootingState;
    private Spaceship.SpecialPowerState powerState;
    private Float mouseAiming;
    private Spaceship.SwitchWeaponState switchWeaponState;

    private PhysicalObjectSnapshot myself;
    /**
     * Stage for labels etc overlayed on the perspective screen, but using a hud-like orientation
     */
    private Stage overlayStage;

    /**
     * Stage for background on the perspective screen, extending Actor class
     */
    private Stage backgroundStage;

    private int chosenWeapon;

    // this list of actors will be removed before the next render cycle
    private List<Actor> volatileActors = new ArrayList<Actor>();

    private Random random = GameHelper.randomGenerator;
    private Float hardSteering;

    private boolean foundMySelf;

    private MissionText missionText;

    private Compass compass;

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

        Pixmap pixmap = new Pixmap(Gdx.files.internal("texture/cursor.png"));
        Cursor customCursor = Gdx.graphics.newCursor(pixmap, pixmap.getWidth() - pixmap.getWidth() / 2,
                pixmap.getHeight() - pixmap.getHeight() / 2);
        Gdx.graphics.setCursor(customCursor);
        //Gdx.input.setCursorCatched(true);

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        stage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera), batch);
        //stage = new Stage(new ScalingViewport(Scaling.stretch, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));


        backgroundStage = new Stage();
        Texture backgroundTexture = myAssetManager.assetManager.get("background/bg1.png", Texture.class);
        backgroundStars = new BackgroundStars(backgroundTexture);
        backgroundTexture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        backgroundStars.setSpeed(0.05f);

        /**
         * InputManager set as inputprocessor in show method
         */
        inputManager = new InputManager();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputManager);
        controllerInput();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        screenHud = new ScreenHud(myAssetManager);

        enemyHud = new EnemyHud(camera);

        playerHud = new PlayerHud(camera, inputMultiplexer);

        createOverlayHud();

        pauseScreenAdapter = new PauseScreenAdapter(inputMultiplexer);

        missionText = new MissionText(myAssetManager);

        compass = new Compass();

        box2dBodyCreator = new Box2dBodyCreator();
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

                    }

                    if (buttonCode == GamePadControls.BUTTON_BACK) {

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
            Gdx.app.exit();
        }


        /*
         * If space is pressed, a minimap is created. No enemyhud, just enemies and Player.
         */

        if (inputManager.isKeyDown(Input.Keys.SPACE)) {
            camera.zoom = 0.6f;
            if (enemyHud.enableEnemyHud()) {
                enemyHud.disableEnemyHud();
            }
            if (enemyHud.enableMetaData()) {
                enemyHud.disableMetaData();
            }

        } else {
            camera.zoom = 2.5f;
        }
        if (inputManager.isKeyReleased(Input.Keys.SPACE)) {
            enemyHud.enableEnemyHud();
            enemyHud.enableMetaData();
        } else if (inputManager.isKeyPressed(Input.Keys.COMMA)) {
            enemyHud.disableMetaData();
        }


        //Mouse input
        inputManager.getTouchState(0);

        if (inputManager.isTouchPressed(0)) {
            System.out.println("PRESSED");
            System.out.println("Touch coordinates: " + inputManager.touchCoordX(0) + ", " + inputManager.touchCoordY(0));
            //System.out.println("Touch displacement" + inputManager.touchDisplacementX(0) + ", " + inputManager.touchDisplacementY(0));
            //System.out.println("Mouse aim: " + mouseAiming);


        }

        if (inputManager.isTouchDown(0)) {
            //System.out.println("DOWN");
            float mouseX = inputManager.touchCoordX(0);
            float mouseY = inputManager.touchCoordY(0);

            this.mouseAiming = (float) Math.atan2(mouseY, mouseX) - MathUtils.PI / 2;
        }

        if (inputManager.isTouchReleased(0)) {
            System.out.println("RELEASED");
            this.mouseAiming = null;
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
            } else if (inputManager.isKeyDown(Input.Keys.S)) {
                throttle = Spaceship.ThrottleState.REVERSE;
            } else if (inputManager.isKeyDown(Input.Keys.R)) {
                throttle = Spaceship.ThrottleState.FULL_STOP;
            }
        }

        steering = Spaceship.SteeringState.CENTER;

        //Gamepad/Controller

        if (controller != null) {
            if (controller.getAxis(GamePadControls.AXIS_LEFT_X) > 0.2f) {
                steering = Spaceship.SteeringState.LEFT;
            } else if (controller.getAxis(GamePadControls.AXIS_LEFT_X) < -0.2f) {
                steering = Spaceship.SteeringState.RIGHT;
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

        if (inputManager.isKeyPressed(Input.Keys.F)) {
            setChosenWeapon(getChosenWeapon() + 1);
            if (getChosenWeapon() >= 4) {
                setChosenWeapon(0);
            }
        }

        //Keyboard

        if (shootingState == Spaceship.ShootingState.PACIFIST) {
            if (getChosenWeapon() == 1) {
                if (inputManager.isKeyDown(Input.Keys.C)) {
                    shootingState = Spaceship.ShootingState.FIRING;
                }
            }
            if (getChosenWeapon() == 2) {
                if (inputManager.isKeyDown(Input.Keys.C)) {
                    shootingState = Spaceship.ShootingState.MISSILE_FIRING;
                }
            }
            if (getChosenWeapon() == 3) {
                if (inputManager.isKeyDown(Input.Keys.C)) {
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
        if (inputManager.isKeyPressed(Input.Keys.P)) {
            connector.setPaused(true);
        }

        if (inputManager.isKeyPressed(Input.Keys.R)) {
            connector.setPaused(false);
        }
        if (inputManager.isKeyPressed(Input.Keys.TAB)) {

        }

        connector.controlSpaceship(target, throttle, steering, powerState, shootingState, mouseAiming, switchWeaponState, hardSteering);

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

    private void registerVolatileActor(Actor actor) {
        volatileActors.add(actor);
    }

    private void shapeRendererMode() {
        if (inputManager.isKeyDown(Input.Keys.PERIOD)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        }
    }

    private void renderGameObjects(float delta) {
        for (Actor actor : volatileActors) {
            actor.remove();
        }

        volatileActors.clear();

        SpaceSnapshot snapshot = connector.latestSnapshot();


        foundMySelf = false;
        for (PhysicalObjectSnapshot object : snapshot.getPhysicalObjects()) {
            if (mySelf.equals(object.getName())) {
                myself = object;

                /**
                 * With camera.position.clamp(0.6f, 2f); we get a whole new different kind of game. Something where you have to stay
                 * within the borders or you die. The camera follow slightly, so you have to zoom out. The Spaceship moves freely now,
                 * without the camera following (only slightly). This can be a new GAMEMODE for this game.
                 */
                camera.position.set(object.getPosition().x, object.getPosition().y, 100);
                camera.up.set(1, 0, 0);
                camera.rotate(object.getOrientation() * 180 / (float) Math.PI, 0, 0, 1);
                camera.update();

                foundMySelf = true;

                Label playerLabel = playerHud.nameLabel(object);
                ProgressBar bar = playerHud.healthBar(object);
                overlayStage.addActor(playerLabel);
                overlayStage.addActor(bar);
                registerVolatileActor(playerLabel);
                registerVolatileActor(bar);

                Label playerPosition = playerHud.positionLabel(object);
                overlayStage.addActor(playerPosition);
                registerVolatileActor(playerPosition);

                Label playerOrientation = playerHud.orientationLabel(object);
                overlayStage.addActor(playerOrientation);
                registerVolatileActor(playerOrientation);

                Label playerSpeed = playerHud.speedLabel(object);
                overlayStage.addActor(playerSpeed);
                registerVolatileActor(playerSpeed);

                Label playerDirection = playerHud.directionLabel(object);
                overlayStage.addActor(playerDirection);
                registerVolatileActor(playerDirection);


                break;
            }
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRendererMode();


        batch.begin();


        try {
            //System.out.println(snapshot.getPhysicalObjects().size() + " physical objects");

            for (PhysicalObjectSnapshot physicalObject : snapshot.getPhysicalObjects()) {
                float x = physicalObject.getPosition().x;
                float y = physicalObject.getPosition().y;
                Number radiusRaw = ((Number) physicalObject.getExtra().get("radius"));

                if (radiusRaw == null) {
                    radiusRaw = 3f;
                }

                float radius = radiusRaw.floatValue();


                switch (physicalObject.getVisualization()) {

                    case SPACESHIP:
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(physicalObject.getPosition().x, physicalObject.getPosition().y, radius); //5f is default radius
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(physicalObject.getPosition().x + 4f * (float) Math.cos(physicalObject.getOrientation()),
                                physicalObject.getPosition().y + 4f * (float) Math.sin(physicalObject.getOrientation()),
                                (radius / 2f));


                        Body shipBody = box2dBodyCreator.addDynamicBodyToPhysicalObject(physicalObject, 0.5f, 0.5f, 0.5f);
                        bodies.add(shipBody);

                        if (throttle == Spaceship.ThrottleState.FORWARDS) {
                            SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/spaceship_fly.png", physicalObject, batch, null);
                        } else if (powerState == Spaceship.SpecialPowerState.BOOSTING) {
                            SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/spaceship_boost.png", physicalObject, batch, null);
                        } else {
                            SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/spaceship.png", physicalObject, batch, null);
                        }

                        break;
                    case ENEMY:

                        shapeRenderer.setColor(chooseRandomColor(SPACESHIP_COLORS));
                        shapeRenderer.circle(physicalObject.getPosition().x, physicalObject.getPosition().y, radius); //5f is default radius
                        shapeRenderer.setColor(chooseRandomColor(SPACESHIP_COLORS));
                        shapeRenderer.circle(physicalObject.getPosition().x + (radius / 2) * (float) Math.cos(physicalObject.getOrientation()),
                                physicalObject.getPosition().y + (radius / 2) * (float) Math.sin(physicalObject.getOrientation()),
                                (radius / 2));

                        break;
                    case REAR_BOOSTER:
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.DARK_GRAY);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(physicalObject.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(physicalObject.getOrientation()), (radius / 2));
                        break;
                    case COCKPIT:
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(physicalObject.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(physicalObject.getOrientation()), (radius / 2));

                        break;
                    case BOULDER:
                        shapeRenderer.setColor(Color.BROWN);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(physicalObject.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(physicalObject.getOrientation()), (radius / 2));
                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/asteroid_small.png", object, batch, null);

                        //Body assBody = box2dBodyCreator.addDynamicBodyToPhysicalObject(physicalObject, 0.5f, 0.5f, 0.5f);
                        //bodies.add(assBody);
                        break;
                    case EXPLOSION:
                        Color debrisColor = chooseRandomColor(DEBRIS_COLORS);
                        shapeRenderer.setColor(debrisColor);
                        shapeRenderer.circle(x, y, radius);
                        break;
                    case TEST:
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(physicalObject.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(physicalObject.getOrientation()), (radius / 2));


                        //Body testBody = box2dBodyCreator.addDynamicBodyToPhysicalObject(physicalObject, 0.5f, 0.5f, 0.5f);
                        //bodies.add(testBody);

/*
                        Label chaserPosition = enemyHud.positionLabel(physicalObject);
                        overlayStage.addActor(chaserPosition);
                        registerVolatileActor(chaserPosition);

                        Label chaserOrientation = enemyHud.orientationLabel(physicalObject);
                        overlayStage.addActor(chaserOrientation);
                        registerVolatileActor(chaserOrientation);

                        Label chaserSpeed = enemyHud.speedLabel(physicalObject);
                        overlayStage.addActor(chaserSpeed);
                        registerVolatileActor(chaserSpeed);

                        Label chaserDirection = enemyHud.directionLabel(physicalObject);
                        overlayStage.addActor(chaserDirection);
                        registerVolatileActor(chaserDirection);
*/
                        break;
                    case LEFT_CANNON:
                        shapeRenderer.setColor(Color.CYAN);
                        shapeRenderer.circle(x, y, radius);
                        //Body bulletBody = box2dBodyCreator.addDynamicBodyToPhysicalObject(physicalObject, 0.5f, 0.5f, 0.5f);
                        //bodies.add(bulletBody);
                        break;
                    case RIGHT_CANNON:
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, radius);
                        //Body misBody = box2dBodyCreator.addDynamicBodyToPhysicalObject(physicalObject, 0.5f, 0.5f, 0.5f);
                        //bodies.add(misBody);
                        break;

                    default:
                        System.out.println("Unsupported visualization: " + physicalObject.getVisualization());
                }
            }

        } finally {

            batch.end();
        }

        shapeRenderer.end();

        // Game Stage Border
/*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(EngineConstants.MIN_X, EngineConstants.MIN_Y, EngineConstants.ENGINE_WIDTH, EngineConstants.ENGINE_HEIGHT);
        shapeRenderer.end();
*/



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

    private static final Color[] SPACESHIP_COLORS = {
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.CORAL,
            Color.CHARTREUSE,
            Color.WHITE,
            Color.CYAN,
            Color.FOREST
    };


    private Color chooseRandomColor(Color[] randomColors) {
        return randomColors[random.nextInt(randomColors.length)];
    }


    private void addBackground() {

        //backgroundStage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

        backgroundStage.getBatch().begin();
        backgroundStage.getBatch().disableBlending();
        backgroundStars.draw(backgroundStage.getBatch(), 10);
        backgroundStage.getBatch().end();

        backgroundStage.addActor(backgroundStars);

    }

    private void setPauseScreen() {
        pauseScreenAdapter.render();

    }


    private void createOverlayHud() {
        BitmapFont font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);
        overlayStage = new Stage();
    }

    private void updateOverlay() {
        overlayStage.draw();
    }

    private void updateHud(PhysicalObjectSnapshot myself, float delta) {
        screenHud.update(myself, delta);
        screenHud.stage.draw();
    }

    @Override
    public void render(float delta) {
        handleInput();
        addSoundEffects();

        stage.act();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        addBackground();

        renderGameObjects(delta);

        stage.draw();
        updateHud(myself, delta);

        updateOverlay();
        playerHud.update();

        compass.updateCompass(myself);

        box2dBodyCreator.elapseTime(camera, delta);

        for (Body body: bodies) {
            while (body.getFixtureList().size > 0) {
                body.destroyFixture(body.getFixtureList().first());
            }
        }

    }

    private void setSwitchWeaponState(Spaceship.SwitchWeaponState switchWeaponState) {
        this.switchWeaponState = switchWeaponState;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void dispose() {

        backgroundStage.dispose();
        stage.dispose();
        overlayStage.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        myAssetManager.dispose();
    }


}