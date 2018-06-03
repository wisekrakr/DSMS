package com.wisekrakr.firstgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

public class HealthBar extends ProgressBar {

       public HealthBar(float width, float height) {
            super(0f, 1f, 0.01f, false, new ProgressBarStyle());
            getStyle().background = HealthBarUtils.getColoredDrawable(width, height, Color.RED);
            getStyle().knob = HealthBarUtils.getColoredDrawable(0, height, Color.GREEN);
            getStyle().knobBefore = HealthBarUtils.getColoredDrawable(width, height, Color.GREEN);

            setWidth(width);
            setHeight(height);

            setAnimateDuration(0.0f);
            setValue(1f);

            setAnimateDuration(0.25f);
        }
    }