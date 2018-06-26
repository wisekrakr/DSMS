package com.wisekrakr.firstgame.popups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.wisekrakr.firstgame.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

public class DialogWindow implements Disposable{

    private OrthographicCamera camera;
    private InputMultiplexer inputMultiplexer;
    private MyAssetManager myAssetManager;
    private Stage stage;
    private Skin skin;

    private Dialog introDialog;

    public DialogWindow(OrthographicCamera camera, InputMultiplexer inputMultiplexer) {
        this.camera = camera;
        this.inputMultiplexer = inputMultiplexer;

        myAssetManager = new MyAssetManager();
        myAssetManager.loadSkins();

        stage = new Stage();
        inputMultiplexer.addProcessor(stage);

        //skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/uiskin.json")));
        skin = myAssetManager.assetManager.get(String.valueOf(Gdx.files.internal("font/flat-earth-ui.json")));

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

    }

    private Vector3 projection(SpaceSnapshot.GameObjectSnapshot object){
        return camera.project(new Vector3(object.getPosition().x, object.getPosition().y, 100));
    }


    public Dialog introDialog(SpaceSnapshot.GameObjectSnapshot object){


        introDialog = new Dialog("Dont get this spaceship shot!", skin);

        introDialog.text("Are you ready to take care of this spaceship for me?");
        TextButton buttonYes = new TextButton("Pff... of course!", skin);
        TextButton buttonNo = new TextButton("Nah, you do it", skin);

        introDialog.button(buttonYes, false);
        introDialog.button(buttonNo, true);
        introDialog.setPosition(projection(object).x, projection(object).y - 30, Align.center);
        introDialog.setScale(0.6f);

        introDialog.show(stage);

        buttonYes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Button Pressed: Yes");
                introDialog.remove();


            }
        });
        buttonNo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        return introDialog;

    }

    public void update() {

        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
