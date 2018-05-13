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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wisekrakr.firstgame.*;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;
import com.wisekrakr.firstgame.input.GamePadControls;
import com.wisekrakr.firstgame.input.InputManager;
import com.wisekrakr.firstgame.popups.PauseScreenAdapter;


import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by David on 11/23/2017.
 */
public class PlayerPerspectiveScreen extends ScreenAdapter {

    private Label myselfLabel;
    private Label damageLabel;
    private Label enemyLabel;

    private Hud hud;

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
    private boolean up,down,left,right;

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

    /**
     * Stage for labels etc overlayed on the perspective screen, but using a hud-like orientation
     */
    private Stage overlayStage;

    /**
     * Stage for background on the perspective screen, extending Actor class
     */
    private Stage backgroundStage;
    private BitmapFont font;
    private int chosenWeapon;
    private int chosenNumber;


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

        hud = new Hud(myAssetManager);

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
                        setChosenWeapon(getChosenWeapon()+1);
                        if (getChosenWeapon() >= 4){
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

        //Mouse input
        inputManager.getTouchState(0);

        if (inputManager.isTouchPressed(0)) {
            System.out.println("PRESSED");
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

        if (throttle == Spaceship.ThrottleState.STATUSQUO){
            if (inputManager.isKeyDown(Input.Keys.W)){
                throttle = Spaceship.ThrottleState.FORWARDS;
                System.out.println("go");
            }else if (inputManager.isKeyDown(Input.Keys.S)) {
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
                if (getChosenWeapon() >= 4){
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

        //Mouse
/*
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            setChosenWeapon(getChosenWeapon() + 1);
            if (getChosenWeapon() >= 4) {
                setChosenWeapon(0);
            }
        }
*/
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

        connector.controlSpaceship(target, throttle, steering, powerState, shootingState, aimingState, switchWeaponState);
    }

    private void addSoundEffects(){
        if(shootingState == Spaceship.ShootingState.FIRING){
            Sound pew = myAssetManager.assetManager.get("sound/photon1.wav", Sound.class);
            pew.play(0.1f);
        }
        if(shootingState == Spaceship.ShootingState.MISSILE_FIRING){
            Sound pew = myAssetManager.assetManager.get("sound/photon2.wav", Sound.class);
            pew.play(0.1f);
        }
        if(shootingState == Spaceship.ShootingState.PLACE_MINE){
            Sound boom = myAssetManager.assetManager.get("sound/mine_blowup.mp3", Sound.class);
            //boom.play();
            boom.play(0.1f);
        }
        if(powerState == Spaceship.SpecialPowerState.BOOSTING){
            Sound acc = myAssetManager.assetManager.get("sound/acc1.mp3", Sound.class);
            acc.play(0.1f, 1.6f, 2f);
        }
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
            enemyLabel.setText("Bastard");
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        myself = null;
        enemy = null;
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

                } else if ("SpaceMinePlayer".equals(object.getType())) {
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                    Random random = new Random();
                    int randomNumber = random.nextInt(4) + 1;
                    Color blinkingColor = new Color();

                    if (randomNumber == 1) {
                        blinkingColor.set(Color.RED);
                    }
                    if (randomNumber == 2) {
                        blinkingColor.set(Color.WHITE);
                    }
                    if (randomNumber == 3) {
                        blinkingColor.set(Color.WHITE);
                    }
                    if (randomNumber == 4) {
                        blinkingColor.set(Color.RED);
                    }
                    shapeRenderer.setColor(blinkingColor);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius/2);

                    Boolean isDestruct = (Boolean) object.randomProperties().get("isDestruct");
                    if (isDestruct){
                        Sound boom = myAssetManager.assetManager.get("sound/mine_blowup.mp3", Sound.class);
                        boom.play(1f);
                    }

                }else if ("SpaceMineEnemy".equals(object.getType())) {
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                    Random random = new Random();
                    int randomNumber = random.nextInt(4) + 1;
                    Color blinkingColor = new Color();

                    if (randomNumber == 1) {
                        blinkingColor.set(Color.RED);
                    }
                    if (randomNumber == 2) {
                        blinkingColor.set(Color.WHITE);
                    }
                    if (randomNumber == 3) {
                        blinkingColor.set(Color.WHITE);
                    }
                    if (randomNumber == 4) {
                        blinkingColor.set(Color.RED);
                    }
                    shapeRenderer.setColor(blinkingColor);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius/2);

                } else if ("BulletPlayer".equals(object.getType())) {
                    shapeRenderer.setColor(Color.CYAN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                } else if ("BulletMisc".equals(object.getType())) {
                    shapeRenderer.setColor(Color.CYAN);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);


                }else if ("BulletEnemy".equals(object.getType())) {

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
                    shapeRenderer.setColor(Color.PURPLE);
                    shapeRenderer.circle(object.getPosition().x - (radius / 4) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y - (radius / 4) * (float) Math.sin(object.getOrientation()), (radius / 4));

                }else if ("Rotunda".equals(object.getType())) {
                    shapeRenderer.setColor(Color.YELLOW);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    Float radius = (Float) object.extraProperties().get("radius");
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                } else if ("EnemyChaser".equals(object.getType())) {
                    enemy = object;

                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");
                    Integer health = (Integer) object.healthProperties().get("health");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                    enemyLabel.setVisible(true);
                    enemyLabel.setPosition(object.getPosition().x, object.getPosition().y + 30, Align.center);




                }else if ("EnemyEls".equals(object.getType())) {
                    shapeRenderer.setColor(Color.SKY);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));

                }else if ("EnemyFlyby".equals(object.getType())) {
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.YELLOW);
                    shapeRenderer.circle(object.getPosition().x + (radius / 3) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 3) * (float) Math.sin(object.getOrientation()), (radius / 3));

                }else if ("EnemyGang".equals(object.getType())) {
                    shapeRenderer.setColor(Color.GOLD);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.PURPLE);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));


                }else if ("EnemyShitter".equals(object.getType())) {
                    shapeRenderer.setColor(Color.LIGHT_GRAY);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.SLATE);
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
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);

                } else if ("PowerUpMissile".equals(object.getType())) {
                    shapeRenderer.setColor(Color.GOLD);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 28);
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 22);
                    shapeRenderer.setColor(Color.GOLD);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 15 / 2);
                } else if ("PowerUpShield".equals(object.getType())) {
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 30);
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 22);
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 15 / 2);
                } else if ("PowerUpMinion".equals(object.getType())) {
                    shapeRenderer.setColor(Color.SKY);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 30);
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 22);
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 15 / 2);
                } else if ("PowerUpHealth".equals(object.getType())) {
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 30);
                    shapeRenderer.setColor(Color.GREEN);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 22);
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 15 / 2);
                } else if ("MinionShooterPlayer".equals(object.getType())) {
                    shapeRenderer.setColor(Color.YELLOW);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.SKY);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));


                } else if ("MinionFighterPlayer".equals(object.getType())) {
                    shapeRenderer.setColor(Color.SKY);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.YELLOW);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));


                }else if ("MinionShooterEnemy".equals(object.getType())) {
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.SKY);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));


                } else if ("MinionFighterEnemy".equals(object.getType())) {
                    shapeRenderer.setColor(Color.SKY);
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                    Float radius = (Float) object.extraProperties().get("radius");

                    shapeRenderer.circle(object.getPosition().x, object.getPosition().y, radius);
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.circle(object.getPosition().x + (radius / 2) * (float) Math.cos(object.getOrientation()),
                            object.getPosition().y + (radius / 2) * (float) Math.sin(object.getOrientation()), (radius / 2));


                } else if ("Shield".equals(object.getType())) {
                    String lightBlue = "8EE2EC";
                    shapeRenderer.setColor(Color.valueOf(lightBlue));
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
                }else if ("Debris".equals(object.getType())) {
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

    private void createOverlayHud(){

        font = myAssetManager.assetManager.get("font/myFont.fnt");
        font.getData().setScale(0.4f);

        overlayStage = new Stage();

        myselfLabel = new Label("Myself", new Label.LabelStyle(font, Color.WHITE));
        myselfLabel.setVisible(false);

        enemyLabel = new Label("Bastard", new Label.LabelStyle(font, Color.RED));
        enemyLabel.setVisible(false);

        overlayStage.addActor(myselfLabel);
        overlayStage.addActor(enemyLabel);
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