package com.wisekrakr.firstgame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.Objects.BaseObject;
import com.wisekrakr.firstgame.Screens.PlayScreen;

import java.util.ArrayList;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;


public class GameDSMS extends Game {

    private Player1 player1;

    private ArrayList<BaseObject> gameObjects;

    @Override
    public void create() {
        GameDSMS gameDSMS = new GameDSMS();
        gameObjects();

        setScreen(new PlayScreen(gameDSMS));


    }

    public void gameObjects() {

        Random randomSpawnPoint = new Random();
        Random randomAngle = new Random();

/*		Vector2 playerSpawnPoint = new Vector2(randomSpawnPoint.nextInt(Constants.WORLD_WIDTH),
				randomSpawnPoint.nextInt(Constants.WORLD_HEIGHT));
				*/
        Vector2 playerSpawnPoint = new Vector2(0, 0);

        Vector2 playerVelocity = new Vector2(playerSpawnPoint.x + 1, playerSpawnPoint.y + 1);

        player1 = new Player1("David", playerSpawnPoint, new Vector2());

        ChaserEnemy enemy1 = new ChaserEnemy("Chaser1", new Vector2(playerSpawnPoint.x + 30, playerSpawnPoint.y + 30), new Vector2(randomAngle.nextInt(Constants.WORLD_WIDTH), randomAngle.nextInt(Constants.WORLD_HEIGHT)));
        ChaserEnemy enemy2 = new ChaserEnemy("Chaser2", new Vector2(randomSpawnPoint.nextInt(Constants.WORLD_WIDTH),
                randomSpawnPoint.nextInt(Constants.WORLD_HEIGHT)), new Vector2(randomAngle.nextInt(Constants.WORLD_WIDTH), randomAngle.nextInt(Constants.WORLD_HEIGHT)));

        gameObjects = new ArrayList<BaseObject>();
        gameObjects.add(enemy1);
        gameObjects.add(enemy2);
        gameObjects.add(player1);


    }

    public ArrayList<BaseObject> getGameObjects() {

        return gameObjects;
    }

    public Player1 getPlayer1() {
        return player1;
    }


    public enum SteeringState {
        LEFT, CENTER, RIGHT
    }

    public enum ThrottleState {
        REVERSE, STATUSQUO, FORWARDS
    }

    public class Player1 extends BaseObject {
        private ThrottleState throttle = ThrottleState.STATUSQUO;
        private SteeringState steering = SteeringState.CENTER;

        private static final float DEFAULT_PLAYER_SPEED = 40;
        private static final float DEFAULT_PLAYER_ROTATE_SPEED = 2;

        public float rotationRadians;

        private Player1(String name, Vector2 position, Vector2 velocity) {
            super(name, position, velocity);

            init();
        }

        @Override
        public float collisionDetector(BaseObject target) {
            return 0;
        }

        @Override
        public void attack(float delta) {
        }


        @Override
        public void init() {
            System.out.println("Player Draw = " + getName() + " with position: " + getPosition());
        }

        @Override
        public void movement(float deltaTime) {
            Vector2 newPosition = getPosition();
            Vector2 direction = new Vector2(newPosition.x - getPosition().x, newPosition.y - getPosition().y);

			/*
			float playerX = getPosition().x;
			float angle = direction.angleRad();

			Vector2 prevPosition = new Vector2();
			Vector2 rotationVector = new Vector2(getPosition().x - prevPosition.x, getPosition().y - prevPosition.y);
			rotationRadians = rotationVector.angle();
			rotationDegrees = rotationRadians * MathUtils.radiansToDegrees;
			*/
        }

        @Override
        public void update(float delta) {
            processElapsedTime(delta);
            handleInput();
        }

        @Override
        public void render(ShapeRenderer renderer) {
        }

        private void handleInput() {
            if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                setPosition(new Vector2(0, 0));
                speed = 0;
                angle = (float) Math.PI / 2;
            }


            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                throttle = ThrottleState.FORWARDS;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                throttle = ThrottleState.REVERSE;
            } else {
                throttle = ThrottleState.STATUSQUO;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                steering = SteeringState.LEFT;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                steering = SteeringState.RIGHT;
            } else {
                steering = SteeringState.CENTER;
            }
        }

        private float speed = 0;
        private float angle = (float) Math.PI / 2;

        private void processElapsedTime(float delta) {
            switch (steering) {
                case LEFT:
                    angle = angle + delta * 1f;
                    break;
                case RIGHT:
                    angle = angle - delta * 1f;
                    break;
            }

            float oldSpeed = speed;

            switch (throttle) {
                case FORWARDS:
                    speed = Math.min(speed + delta * 25f, 100);
                    break;
                case REVERSE:
                    speed = Math.max(speed - delta * 25f, -100);
                    break;
            }

            if (Math.signum(oldSpeed) == -Math.signum(speed)) {
                speed = 0;
            }

            setPosition(new Vector2(
                    getPosition().x + delta * speed * (float) Math.cos(angle),
                    getPosition().y + delta * speed * (float) Math.sin(angle)
            ));

            setOrientation(angle);

			/*
			Vector2 position = getPosition();
			Vector2 velocity = getVelocity();
			Vector2 movement = new Vector2();
			Vector2 direction = new Vector2();

			degrees += getPosition().angleRad();
			position.set(getPosition());
			direction.set(MathUtils.cos(degrees * MathUtils.radiansToDegrees),
					MathUtils.sin(degrees * MathUtils.radiansToDegrees)).nor();
			setVelocity(direction);
			movement.set(velocity).scl(delta);
			position.add(movement);
*/



			/*
							newPosition.y += DEFAULT_PLAYER_SPEED * delta;

				newPosition.y -= DEFAULT_PLAYER_SPEED * delta;


			 */
				/*
				//newPosition.x -= DEFAULT_PLAYER_SPEED * delta;
				//newPosition.rotateRad(rotationRadians * delta);
				//newPosition.rotateRad(MathUtils.sin(rotationRadians) * DEFAULT_PLAYER_ROTATE_SPEED);
				//newPosition.rotateRad((rotation * DEFAULT_PLAYER_ROTATE_SPEED) * delta);
				playerRotation(delta, (rotation * DEFAULT_PLAYER_ROTATE_SPEED) * delta);
				//newPosition.x += DEFAULT_PLAYER_SPEED * delta;
				//newPosition.rotateRad(-rotationRadians * delta);
				//newPosition.rotateRad(MathUtils.cos(-rotationRadians) * DEFAULT_PLAYER_ROTATE_SPEED);
				//newPosition.rotateRad((-rotation * DEFAULT_PLAYER_ROTATE_SPEED) * delta);
				playerRotation(delta, (-rotation * DEFAULT_PLAYER_ROTATE_SPEED) * delta);


			Vector2 newPosition = getPosition();

			rotationRadians = MathUtils.atan2(getPosition().y - getVelocity().y, getPosition().x - getVelocity().x);

			float velocityX = MathUtils.cos(rotationRadians);
			float velocityY = MathUtils.sin(rotationRadians);
			float rotation = velocityX * velocityY;
*/
        }


        public float distanceTravelled(float delta) {

            float travelledDistance = 0;


            return travelledDistance;
        }


    }


    private class ChaserEnemy extends BaseObject {

        private static final float DEFAULT_ENEMY_SPEED = 20;
        private static final float AGRO_DISTANCE = 150;
        private static final int CHANGE_DIRECTION_TIME = 3000;


        private ChaserEnemy(String name, Vector2 position, Vector2 velocity) {
            super(name, position, velocity);

        }

        @Override
        public float collisionDetector(BaseObject target) {

            float distanceBetweenX = (this.getPosition().x - target.getPosition().x);
            float distanceBetweenY = (this.getPosition().y - target.getPosition().y);
            float distanceBetweenObjectAndTarget = (float) Math.hypot(distanceBetweenX, distanceBetweenY); //a*a = b*b + c*c


            if (distanceBetweenObjectAndTarget <= AGRO_DISTANCE) {

                System.out.println("Collision imminent between: ");
                System.out.println(this.getName() + " and " + target.getName());
                System.out.println(distanceBetweenObjectAndTarget);
                System.out.println("X = " + distanceBetweenX + " Y = " + distanceBetweenY + " squared = " + distanceBetweenX * distanceBetweenX + " + " + distanceBetweenY * distanceBetweenY +
                        " = " + distanceBetweenObjectAndTarget * distanceBetweenObjectAndTarget);

            }

            return distanceBetweenObjectAndTarget;
        }

        @Override
        public void attack(float delta) {

            Vector2 newPosition = getPosition();
            Vector2 direction = new Vector2();
            Vector2 movingPosition = new Vector2();

            direction.set(getPlayer1().getPosition()).sub(newPosition).nor();
            setVelocity(direction.scl(DEFAULT_ENEMY_SPEED));
            movingPosition.set(getVelocity()).scl(delta);
            if (newPosition.dst2(getPlayer1().getPosition()) > movingPosition.len2()) {
                newPosition.add(movingPosition);

            } else {
                newPosition.set(getPlayer1().getPosition());
            }


        }

        @Override
        public void init() {

            System.out.println("Enemy Draw = " + getName() + " with position: " + getPosition() + " and an Angle of: ");

        }

        @Override
        public void movement(float delta) {

            Vector2 newPosition = getPosition();
            Vector2 direction = new Vector2();

            Random randomPositionMaker = new Random(500);

            Vector2 randomPosition = new Vector2();
            Vector2 movingPosition = new Vector2();

            direction.set(newPosition.sub(randomPosition)).nor();
            setVelocity(direction.scl(DEFAULT_ENEMY_SPEED));
            movingPosition.set(getVelocity()).scl(delta);
            newPosition.add(movingPosition);


        }


        @Override
        public void update(float delta) {
/*
			if(collisionDetector(getPlayer1()) < AGRO_DISTANCE){
				attack(delta);
			}else {
				movement(delta);

			}
*/

        }

        @Override
        public void render(ShapeRenderer renderer) {
        }


    }


    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    @Override
    public Screen getScreen() {
        return super.getScreen();
    }


}
