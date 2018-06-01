package com.wisekrakr.firstgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Shape;


import java.util.Random;

public class GameObjectRenderer {

    private Random random;
    private ShapeRenderer shaperenderer;
    private Color color;
    private Shape shape;
    private ShapeRenderer.ShapeType shapeType;

    public GameObjectRenderer(ShapeRenderer shapeRenderer) {
        this.shaperenderer = shapeRenderer;
        random = new Random();
    }

    public void drawObject(Color color, ShapeRenderer.ShapeType shapeType, Shape shape){
        this.shapeType = ShapeRenderer.ShapeType.Filled;
        this.color = color;
        this.shape = shape;
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


    private Color chooseColor(Color[] colors) {
        return colors[random.nextInt(colors.length)];
    }

    public ShapeRenderer getShaperenderer() {
        return shaperenderer;
    }

    public void setShaperenderer(ShapeRenderer shaperenderer) {
        this.shaperenderer = shaperenderer;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public static Color[] getSporeColors() {
        return SPORE_COLORS;
    }

    public static Color[] getDebrisColors() {
        return DEBRIS_COLORS;
    }

    public static Color[] getExhaustColors() {
        return EXHAUST_COLORS;
    }

    public static Color[] getBulletColors() {
        return BULLET_COLORS;
    }

    public ShapeRenderer.ShapeType getShapeType() {
        return shapeType;
    }
}
