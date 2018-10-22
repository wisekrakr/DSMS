package com.wisekrakr.firstgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;

public class Box2dBodyCreator implements Disposable {

    private Box2DDebugRenderer box2DDebugRenderer;
    private Body shipBody;
    private World world;

    public Box2dBodyCreator() {

        world = new World(new Vector2(0,0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    public Body addDynamicBodyToPhysicalObject(PhysicalObjectSnapshot object, float density, float friction, float restitution){

        Number radiusRaw = (Number) object.getExtra().get("radius");

        if (radiusRaw == null) {
            radiusRaw = 5f;
        }

        float radius = radiusRaw.floatValue();

        BodyDef shipBodyDef = new BodyDef();
        shipBodyDef.type = BodyDef.BodyType.DynamicBody;
        shipBodyDef.position.set(object.getPosition().x, object.getPosition().y);
        shipBodyDef.angle = object.getOrientation();

        shipBody = world.createBody(shipBodyDef);

        CircleShape shipCircleShape = new CircleShape();
        shipCircleShape.setRadius(radius);

        FixtureDef shipFixtureDef = new FixtureDef();
        shipFixtureDef.shape = shipCircleShape;
        shipFixtureDef.density = density;
        shipFixtureDef.friction = friction;
        shipFixtureDef.restitution = restitution;

        shipBody.createFixture(shipFixtureDef);

        shipCircleShape.dispose();

        return shipBody;
    }

    public void elapseTime(OrthographicCamera camera, float delta){

        box2DDebugRenderer.render(world, camera.combined);
        world.step(delta, 6, 2);
    }


    @Override
    public void dispose() {
        world.dispose();
        box2DDebugRenderer.dispose();
    }
}
