package com.wisekrakr.firstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.wisekrakr.firstgame.PopUps.PauseScreen;
import com.wisekrakr.firstgame.Screens.PlayerPerspectiveScreen;
import com.wisekrakr.firstgame.Screens.StartScreen;
import com.wisekrakr.firstgame.client.ClientConnector;
import com.wisekrakr.firstgame.engine.MyAssetManager;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;


public class SpaceGameContainer extends Game {

    @Override
    public void create() {

        setScreen(new StartScreen(this));

    }

}
