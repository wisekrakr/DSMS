package com.wisekrakr.firstgame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class InputManager implements InputProcessor {


    public Array<KeyState> keyStates = new Array<KeyState>();
    public Array<TouchState> touchStates = new Array<TouchState>();

    public InputManager() {
        for (int i = 0; i < 256; i++) {
            keyStates.add(new KeyState(i));
        }

        touchStates.add(new TouchState(0, 0, 0, 0));
    }

    public class InputState {

        public boolean pressed = false;
        public boolean down = false;
        public boolean released = false;
    }

    public class KeyState extends InputState{

        public int key;
        public KeyState(int key){
            this.key = key;
        }
    }

    public class TouchState extends InputState{

        public int pointer;
        public Vector2 coordinates;
        public int button;
        private Vector2 lastPosition;
        public Vector2 displacement;

        public TouchState(int coord_x, int coord_y, int pointer, int button){
            this.pointer = pointer;
            coordinates = new Vector2(coord_x, coord_y);
            this.button = button;

            lastPosition = new Vector2(0, 0);
            displacement = new Vector2(lastPosition.x, lastPosition.y);
        }
    }

    public boolean isKeyPressed(int key){
        return keyStates.get(key).pressed;
    }
    public boolean isKeyDown(int key){
        return keyStates.get(key).down;
    }
    public boolean isKeyReleased(int key){
        return keyStates.get(key).released;
    }

    public void update(){
        for (int i = 0; i < 256; i++) {
            KeyState k = keyStates.get(i);
            k.pressed = false;
            k.released = false;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keyStates.get(keycode).pressed = true;
        keyStates.get(keycode).down = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyStates.get(keycode).down = false;
        keyStates.get(keycode).released = true;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


}
