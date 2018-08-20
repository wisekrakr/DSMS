package com.wisekrakr.firstgame.playscreens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.SpriteHelper;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;
import com.wisekrakr.firstgame.input.GamePadControls;
import com.wisekrakr.firstgame.input.InputManager;
import com.wisekrakr.firstgame.overlays.*;
import com.wisekrakr.firstgame.quests.MissionText;
import com.wisekrakr.firstgame.server.EngineConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {

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
    private Spaceship.AimingState aimingState;
    private Spaceship.SwitchWeaponState switchWeaponState;

    private SpaceSnapshot.GameObjectSnapshot myself;

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

    private List<ProgressBar> volatileBars = new ArrayList<ProgressBar>();
    private Random random = GameHelper.randomGenerator;
    private Float hardSteering;

    private boolean foundMySelf;

    private MissionText missionText;


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
                        /*
                        if (paused) {
                            gameState = GameState.RESUME;
                        } else {
                            gameState = GameState.PAUSE;
                        }*/

                        System.out.println("Paused");
                    }

                    if (buttonCode == GamePadControls.BUTTON_BACK) {
/*                        gameState = GameState.STOPPED;
                        System.out.println("Resumed");
                        */
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
            camera.zoom = 2.2f;
            if (enemyHud.enableEnemyHud()) {
                enemyHud.disableEnemyHud();
            }
            if (enemyHud.enableMetaData()){
                enemyHud.disableMetaData();
            }

        } else {
            camera.zoom = 0.8f;
        }
        if (inputManager.isKeyReleased(Input.Keys.SPACE)) {
            enemyHud.enableEnemyHud();
        }else if (inputManager.isKeyPressed(Input.Keys.COMMA)){
            enemyHud.disableMetaData();
        }



        //Mouse input
        inputManager.getTouchState(0);

        if (inputManager.isTouchPressed(0)) {
            System.out.println("PRESSED");

            Vector3 mouseProjection = camera.project(new Vector3(0, 0, 100));
            //float touchCoordinates = (float) Math.sqrt(inputManager.touchCoordX(0) + inputManager.touchCoordY(0));
            //this.hardSteering = (float) Math.atan2(inputManager.touchCoordX(0) - mouseProjection.x, inputManager.touchCoordY(0) - mouseProjection.y);

            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            this.hardSteering = (float) Math.atan2(mouseY - myself.getPosition().y, mouseX - myself.getPosition().y);
            if (this.hardSteering < 0) {
                hardSteering += 360;
            }
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
            } else if (inputManager.isKeyDown(Input.Keys.S)) {
                throttle = Spaceship.ThrottleState.REVERSE;
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

    private void registerVolatileActor(Actor actor) {
        volatileActors.add(actor);
    }

    private void shapeRendererMode(){
        if (inputManager.isKeyDown(Input.Keys.PERIOD)){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        }else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        }
    }

    private void renderGameObjects(float delta) {
        for (Actor actor : volatileActors) {
            actor.remove();
        }

        for (ProgressBar progressBar : volatileBars) {
            progressBar.remove();
        }
        volatileActors.clear();
        volatileBars.clear();


        SpaceSnapshot snapshot = connector.latestSnapshot();

        foundMySelf = false;
        for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
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
                volatileBars.add(bar);

                Label playerOrientation = playerHud.orientationLabel(object);
                overlayStage.addActor(playerOrientation);
                registerVolatileActor(playerOrientation);

                Label playerSpeed = playerHud.speedLabel(object);
                overlayStage.addActor(playerSpeed);
                registerVolatileActor(playerSpeed);

                break;
            }
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
       // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRendererMode();


        batch.begin();


        try {
            for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                Float radius = (Float) object.extraProperties().get("radius");
                Boolean hit = (Boolean) object.extraProperties().get("hit");
                Boolean pickedUp = (Boolean) object.extraProperties().get("pickedUp");

                float x = object.getPosition().x;
                float y = object.getPosition().y;

            /*
            Label swarmWarning = playerHud.swarmWarning();
            overlayStage.addActor(swarmWarning);
            volatileActors.add(swarmWarning);
*/


                switch (object.getType()) {
                    case POWERUP_GENERATOR:
                        // TODO: remove the need for the power up generator

                        break;

                    case SPACESHIP:
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 5f); //5f is default radius
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(object.getPosition().x + 4 * (float) Math.cos(object.getOrientation()),
                                object.getPosition().y + 4 * (float) Math.sin(object.getOrientation()),
                                (5 / 2));

                        if (throttle == Spaceship.ThrottleState.FORWARDS){
                            SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/spaceship_fly.png", object, batch, null);
                        }else if (powerState == Spaceship.SpecialPowerState.BOOSTING){
                            SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/spaceship_boost.png", object, batch, null);
                        }else {
                            SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/spaceship.png", object, batch, null);
                        }



/*
                        if (introDialogOneTime) {
                            if (!(hit)) {
                                introDialogOneTime = true;
                            } else {
                                System.out.println("player hit");
                                dialogWindow.introDialog(object);
                                introDialogOneTime = false;
                                dialogWindow.introDialog(object).hide();
                            }
                        }



*/


                        break;

                    case SPACE_MINE:
                        Color blinkingColor = chooseRandomColor(new Color[]{Color.RED, Color.WHITE, Color.WHITE, Color.RED});
                        shapeRenderer.setColor(blinkingColor);
                        shapeRenderer.circle(x, y, radius);


                        break;
                    case BULLET:

                        shapeRenderer.setColor(Color.CYAN);
                        shapeRenderer.circle(x, y, radius);

                        Label bulletOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(bulletOrientation);
                        registerVolatileActor(bulletOrientation);

                        Label bulletSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(bulletSpeed);
                        registerVolatileActor(bulletSpeed);

                        // SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/bullet_small.png", object, batch, null);


                        break;
                    case ASTEROID:

                        shapeRenderer.setColor(Color.BROWN);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));


                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/asteroid_small.png", object, batch, null);

                        break;
                    case ROTUNDA:
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x, y, radius);
                        break;
                    case ENEMY_CHASER:

                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label chaserNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(chaserNameLabel);
                        registerVolatileActor(chaserNameLabel);

                        ProgressBar chaserHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(chaserHealthBar);
                        volatileBars.add(chaserHealthBar);

                        Label chaserPosition = enemyHud.positionLabel(object);
                        overlayStage.addActor(chaserPosition);
                        registerVolatileActor(chaserPosition);

                        Label chaserOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(chaserOrientation);
                        registerVolatileActor(chaserOrientation);

                        Label chaserSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(chaserSpeed);
                        registerVolatileActor(chaserSpeed);
                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/ssEls.png", object, batch, 0.1f); //this is so that every object gets its own sprite


                        break;

                    case EWM:

                        shapeRenderer.setColor(Color.SKY);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label elsNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(elsNameLabel);
                        registerVolatileActor(elsNameLabel);

                        ProgressBar elsHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(elsHealthBar);
                        volatileBars.add(elsHealthBar);
                        break;
                    case FACE_HUGGER:

                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x + (radius / 3) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 3) * (float) Math.sin(object.getOrientation()), (radius / 3));

                        Label flybyNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(flybyNameLabel);
                        registerVolatileActor(flybyNameLabel);

                        ProgressBar huggerHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(huggerHealthBar);
                        volatileBars.add(huggerHealthBar);
                        break;
                    case SHITTER:

                        shapeRenderer.setColor(Color.LIGHT_GRAY);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.SLATE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label shitterNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(shitterNameLabel);
                        registerVolatileActor(shitterNameLabel);

                        ProgressBar shitterHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(shitterHealthBar);
                        volatileBars.add(shitterHealthBar);

                        Label shitterPosition = enemyHud.positionLabel(object);
                        overlayStage.addActor(shitterPosition);
                        registerVolatileActor(shitterPosition);

                        Label shitterOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(shitterOrientation);
                        registerVolatileActor(shitterOrientation);

                        Label shitterSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(shitterSpeed);
                        registerVolatileActor(shitterSpeed);
                        break;
                    case PEST:

                        shapeRenderer.setColor(Color.FIREBRICK);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label pestNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(pestNameLabel);
                        registerVolatileActor(pestNameLabel);

                        ProgressBar pestHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(pestHealthBar);
                        volatileBars.add(pestHealthBar);

                        Label pestOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(pestOrientation);
                        registerVolatileActor(pestOrientation);

                        Label pestSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(pestSpeed);
                        registerVolatileActor(pestSpeed);
                        break;
                    case BLINKER:

                        shapeRenderer.setColor(Color.GOLDENROD);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label blinkerNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(blinkerNameLabel);
                        registerVolatileActor(blinkerNameLabel);

                        ProgressBar blinkerHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(blinkerHealthBar);
                        volatileBars.add(blinkerHealthBar);

                        Label blinkerOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(blinkerOrientation);
                        registerVolatileActor(blinkerOrientation);

                        Label blinkerSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(blinkerSpeed);
                        registerVolatileActor(blinkerSpeed);
                        break;
                    case LASER_BEAM:
                        Color bulletColor = chooseRandomColor(BULLET_COLORS);
                        shapeRenderer.setColor(bulletColor);
                        shapeRenderer.rectLine(x, y, x + 6.25f * (float) Math.cos(object.getOrientation()),
                                y + 6.25f * (float) Math.sin(object.getOrientation()), 0.5f);
                        break;
                    case MOTHERSHIP:

                        shapeRenderer.setColor(Color.CYAN);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.ORANGE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label motherNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(motherNameLabel);
                        registerVolatileActor(motherNameLabel);

                        ProgressBar motherHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(motherHealthBar);
                        volatileBars.add(motherHealthBar);

                        Label motherPosition = enemyHud.positionLabel(object);
                        overlayStage.addActor(motherPosition);
                        registerVolatileActor(motherPosition);

                        Label motherOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(motherOrientation);
                        registerVolatileActor(motherOrientation);

                        Label motherSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(motherSpeed);
                        registerVolatileActor(motherSpeed);

                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/human_mothership.png", object, batch, 0.7f);

                        break;
                    case DODGER:

                        shapeRenderer.setColor(Color.LIME);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.PINK);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label dodgerNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(dodgerNameLabel);
                        registerVolatileActor(dodgerNameLabel);

                        ProgressBar dodgerHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(dodgerHealthBar);
                        volatileBars.add(dodgerHealthBar);

                        Label dodgerOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(dodgerOrientation);
                        registerVolatileActor(dodgerOrientation);

                        Label dodgerSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(dodgerSpeed);
                        registerVolatileActor(dodgerSpeed);


                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/ssDodger.png", object, batch, 0.1f); //this is so that every object gets its own sprite

                        break;
                    case HOMER:

                        shapeRenderer.setColor(Color.ORANGE);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.VIOLET);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label homerNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(homerNameLabel);
                        registerVolatileActor(homerNameLabel);

                        ProgressBar homerHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(homerHealthBar);
                        volatileBars.add(homerHealthBar);


                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/ssHomer.png", object, batch, 0.3f);

                        break;
                    case MISSILE:

                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, radius);

                        Label missilePosition = enemyHud.positionLabel(object);
                        overlayStage.addActor(missilePosition);
                        registerVolatileActor(missilePosition);

                        Label missileOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(missileOrientation);
                        registerVolatileActor(missileOrientation);

                        Label missileSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(missileSpeed);
                        registerVolatileActor(missileSpeed);
                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/missile_default.png", object, batch, null);

                        break;
                    case MUTATOR:

                        shapeRenderer.setColor(Color.FIREBRICK);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.ORANGE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                        Label mutatorNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(mutatorNameLabel);
                        registerVolatileActor(mutatorNameLabel);

                        ProgressBar mutatorHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(mutatorHealthBar);
                        volatileBars.add(mutatorHealthBar);


                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/ssMutator.png", object, batch, 0.6f);

                        break;
                    case SPORE:
                        Color sporeColor = chooseRandomColor(SPORE_COLORS);
                        shapeRenderer.setColor(sporeColor);
                        shapeRenderer.circle(x, y, radius);
                        break;

                    case SHOTTY:

                        shapeRenderer.setColor(Color.MAROON);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.TEAL);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), radius / 2);

                        Label shottyNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(shottyNameLabel);
                        registerVolatileActor(shottyNameLabel);

                        ProgressBar shottyHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(shottyHealthBar);
                        volatileBars.add(shottyHealthBar);

                        Label shottyOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(shottyOrientation);
                        registerVolatileActor(shottyOrientation);

                        Label shottySpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(shottySpeed);
                        registerVolatileActor(shottySpeed);
                        break;
                    case POWERUP_MISSILE:
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, radius / 2 + radius / 2);
                        shapeRenderer.setColor(Color.GOLD);
                        shapeRenderer.circle(x, y, radius / 2);
                        break;
                    case POWERUP_SHIELD:


                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, radius / 2 + radius / 2);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, radius / 2);


                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/powerup_shield.png", object, batch, 2f);

                        break;
                    case POWERUP_MINION:
                        shapeRenderer.setColor(Color.SKY);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, radius / 2 + radius / 2);
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x, y, radius / 2);
                        break;
                    case POWERUP_HEALTH:


                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.circle(x, y, radius / 2 + radius / 2);
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.circle(x, y, radius / 2);


                        //SpriteHelper.drawSpriteForGameObject(myAssetManager, "sprites/powerup_health.png", object, batch, 2f);

                        break;

                    case MINION:
                        Boolean minionShooter = (Boolean) object.extraProperties().get("minionshooter");
                        Boolean minionFighter = (Boolean) object.extraProperties().get("minionfighter");

                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.SKY);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));
                    /*
                    else if (minionFighter) {
                        objectSnapshot = object;
                        shapeRenderer.setColor(Color.SKY);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));
                    }
                    */
                        break;
                    case SHIELD:
                        String lightBlue = "8EE2EC";
                        shapeRenderer.setColor(Color.valueOf(lightBlue));
                        shapeRenderer.circle(x, y, radius);
                        break;
                    case EXHAUST:
                        Color exhaustColor = chooseRandomColor(EXHAUST_COLORS);
                        shapeRenderer.setColor(exhaustColor);
                        shapeRenderer.circle(x, y, radius);

                        Label aLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(aLabel);
                        registerVolatileActor(aLabel);

                        ProgressBar aHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(aHealthBar);
                        volatileBars.add(aHealthBar);

                        Label aOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(aOrientation);
                        registerVolatileActor(aOrientation);

                        Label aSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(aSpeed);
                        registerVolatileActor(aSpeed);
                        //particleEffectRenderer.exhaustEffect(object);

                        break;
                    case DEBRIS:
                        Color debrisColor = chooseRandomColor(DEBRIS_COLORS);
                        shapeRenderer.setColor(debrisColor);
                        shapeRenderer.circle(x, y, radius);
                        break;
                    case TEST_QUEST:
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.circle(x, y, radius);


                        break;
                    case TEST_NPC:

                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.circle(x, y, radius);
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.circle(x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                                y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

/*
                        float width = (Float) object.extraProperties().get("width");
                        float height = (Float) object.extraProperties().get("height");
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.rect(x, y, width, height);
*/

                        Label testNameLabel = enemyHud.nameLabel(object);
                        overlayStage.addActor(testNameLabel);
                        registerVolatileActor(testNameLabel);

                        ProgressBar testHealthBar = enemyHud.healthBar(object);
                        overlayStage.addActor(testHealthBar);
                        volatileBars.add(testHealthBar);
/*
                        Label testPosition = enemyHud.positionLabel(object);
                        overlayStage.addActor(testPosition);
                        registerVolatileActor(testPosition);
*/
                        Label testOrientation = enemyHud.orientationLabel(object);
                        overlayStage.addActor(testOrientation);
                        registerVolatileActor(testOrientation);

                        Label testSpeed = enemyHud.speedLabel(object);
                        overlayStage.addActor(testSpeed);
                        registerVolatileActor(testSpeed);

                        //statsForObjects.setAllLabels(object);
                        break;

                    case MISSION_END:
                        shapeRenderer.setColor(new Color(255f, 255f, 255f, 50f));
                        shapeRenderer.circle(x, y, radius);
                        break;
                    default:
                        System.out.println("Unknown game object type: " + object.getType());
                }
            }
        } finally {
            batch.end();
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(EngineConstants.MIN_X, EngineConstants.MIN_Y, EngineConstants.ENGINE_WIDTH, EngineConstants.ENGINE_HEIGHT);

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

    private void updateHud(SpaceSnapshot.GameObjectSnapshot myself, float delta) {
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
        missionText.showMission(myself, delta);
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