package com.wisekrakr.firstgame.client;

import java.io.Serializable;

public class PauseUnPauseRequest implements Serializable {
    private boolean pause;

    public PauseUnPauseRequest() {
    }

    public PauseUnPauseRequest(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }
}
