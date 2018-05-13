package com.wisekrakr.firstgame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class InputManager implements InputProcessor {

    private Array<KeyState> keyStates = new Array<KeyState>();
    private Array<TouchState> touchStates = new Array<TouchState>();

    public InputManager() {
        for (int i = 0; i < 256; i++) {
            keyStates.add(new KeyState(i));
        }

        touchStates.add(new TouchState(0, 0, 0, 0));
    }

    private class InputState {

        public boolean pressed = false;
        public boolean down = false;
        public boolean released = false;
    }

    private class KeyState extends InputState{

        public int key;
        public KeyState(int key){
            this.key = key;
        }
    }

    public class TouchState extends InputState{

        public int pointer;
        public float coordX;
        public float coordY;
        public int button;
        public float lastPositionX;
        public float lastPositionY;
        public float displacementX;
        public float displacementY;

        public TouchState(float coordX, float coordY, int pointer, int button){
            this.pointer = pointer;
            this.coordX = coordX;
            this.coordY = coordY;
            this.button = button;

            lastPositionX = 0;
            lastPositionY = 0;

            displacementX = lastPositionX;
            displacementY = lastPositionY;
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

    private int coordinateX (float screenX) {
        return (int) (screenX - Gdx.graphics.getWidth()/2);
    }
    private int coordinateY (float screenY) {
        return (int) (Gdx.graphics.getHeight()/2 - screenY);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean pointerFound = false;

        //get altered coordinates
        float newCoordinateX = coordinateX(screenX);
        float newCoordinateY = coordinateY(screenY);

        //set the state of all touch state events
        for (int i = 0; i < touchStates.size; i++) {
            TouchState touchState = touchStates.get(i);
            if (touchState.pointer == pointer) {
                touchState.down = true;
                touchState.pressed = true;

                //store the coordinates of this touch event
                touchState.coordX = newCoordinateX;
                touchState.coordY = newCoordinateY;

                touchState.button = button;

                //recording last position for displacement values
                touchState.lastPositionX = newCoordinateX;
                touchState.lastPositionY = newCoordinateY;

                //this pointer exists, don't add a new one.
                pointerFound = true;
            }
        }

        //this pointer doesn't exist yet, add it to touchStates and initialize it. (FOR ANDROID/TOUCHSCREEN USE)
        if (!pointerFound) {
            touchStates.add(new TouchState(newCoordinateX, newCoordinateY, pointer, button));
            TouchState touchState = touchStates.get(pointer);

            touchState.down = true;
            touchState.pressed = true;

            touchState.lastPositionX = newCoordinateX;
            touchState.lastPositionY = newCoordinateY;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        TouchState touchState = touchStates.get(pointer);
        touchState.down = false;
        touchState.released = true;

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        //get altered coordinates
        float newCoordinateX = coordinateX(screenX);
        float newCoordinateY = coordinateY(screenY);

        TouchState touchState = touchStates.get(pointer);

        //set coordinates of this touchstate
        touchState.coordX = newCoordinateX;
        touchState.coordY = newCoordinateY;

        //calculate the displacement of this touchstate based on
        //the information from the last frame's position
        touchState.displacementX = newCoordinateX - touchState.lastPositionX;
        touchState.displacementY = newCoordinateY - touchState.lastPositionY;

        //store the current position into last position for next frame.
        touchState.lastPositionX = newCoordinateX;
        touchState.lastPositionY = newCoordinateY;

        return false;
    }

    //check states of supplied touch
    public boolean isTouchPressed(int pointer){
        return touchStates.get(pointer).pressed;
    }

    public boolean isTouchDown(int pointer){
        return touchStates.get(pointer).down;
    }

    public boolean isTouchReleased(int pointer){
        return touchStates.get(pointer).released;
    }

    public float touchCoordX(int pointer){
        return touchStates.get(pointer).coordX;
    }
    public float touchCoordY(int pointer){
        return touchStates.get(pointer).coordY;
    }
    public float touchDisplacementX(int pointer){
        return touchStates.get(pointer).displacementX;
    }
    public float touchDisplacementY(int pointer){
        return touchStates.get(pointer).displacementY;
    }

    public TouchState getTouchState(int pointer){
        if (touchStates.size > pointer) {
            return touchStates.get(pointer);
        } else {
            return null;
        }
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void update(){

        for (int i = 0; i < 256; i++) {
            KeyState keyState = keyStates.get(i);
            keyState.pressed = false;
            keyState.released = false;
        }

        for (int i = 0; i < touchStates.size; i++) {
            TouchState touchState = touchStates.get(i);

            touchState.pressed = false;
            touchState.released = false;

            touchState.displacementX = 0;
            touchState.displacementY = 0;
        }

    }


}
