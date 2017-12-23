package com.wisekrakr.firstgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.GameDSMS;
import com.wisekrakr.firstgame.Objects.BaseObject;

/**
 * Created by David on 11/23/2017.
 */
public class PlayScreen implements Screen {

    private static final String TAG = PlayScreen.class.getName();

    private GameDSMS gameDSMS;

    private Hud hud;

    private SpriteBatch batch;

    private OrthographicCamera camera;
    private OrthographicCamera playerCam;

    private ShapeRenderer shapeRenderer;
    private ShapeRenderer playerRenderer;

    private long startTime = 0;



    public PlayScreen(GameDSMS gameDSMS) {

        this.gameDSMS = gameDSMS;
        this.gameDSMS.gameObjects();

        Gdx.app.log(TAG, "PlayScreen Created");

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


    @Override
    public void show() {  }


    @Override
    public void render(float delta) {
        gameDSMS.getPlayer1().update(delta);
        gameDSMS.getGameObjects();
        for(BaseObject object: gameDSMS.getGameObjects()) {
            object.update(delta);
        }

//        camera.position.set(0, 0, 0);
//        camera.lookAt(1, 0, 0);

        camera.position.set(gameDSMS.getPlayer1().getPosition().x, gameDSMS.getPlayer1().getPosition().y, 100);
        camera.up.set(1, 0, 0);
        camera.rotate(gameDSMS.getPlayer1().getOrientation() * 180 / (float) Math.PI  , 0, 0, 1);
/*
        Vector3 playerVector = new Vector3(gameDSMS.getPlayer1().getPosition().x, gameDSMS.getPlayer1().getPosition().y,0);

        camera.up.x = MathUtils.cos(gameDSMS.getPlayer1().getVelocity().angle());
        camera.up.y = MathUtils.sin(gameDSMS.getPlayer1().getVelocity().angle());

        camera.position.x = playerVector.x;
        camera.position.y = playerVector.y;
*/
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        renderObjects(shapeRenderer);

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.update(delta);
        hud.stage.draw();




/*
        long startTime = TimeUtils.nanoTime();

        if(TimeUtils.timeSinceNanos(startTime) > 1000000000){

            for(BaseObject object: gameDSMS.getGameObjects()) {
                System.out.println(object.getName() + " is at " + object.getPosition());

            }

        }


*/
    }

    public void renderObjects(ShapeRenderer renderer){
        System.out.println("Player 1 is at " + gameDSMS.getPlayer1().getPosition() + ", with an orientation of: " + gameDSMS.getPlayer1().getOrientation() * 180 / Math.PI);


        this.shapeRenderer = renderer;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(BaseObject object: gameDSMS.getGameObjects()) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 6);

            shapeRenderer.setColor(Color.PINK);
            shapeRenderer.cone(object.getPosition().x , object.getPosition().y + 2, 0, 3, 0);
            object.render(shapeRenderer);

            if(object == gameDSMS.getPlayer1()){
                shapeRenderer.setColor(Color.GOLD);
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.circle(object.getPosition().x, object.getPosition().y, 10);

                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.cone(object.getPosition().x , object.getPosition().y + 4, 0, 5, 0);
                object.render(shapeRenderer);
            }
        }

        shapeRenderer.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        hud.dispose();

    }



}
