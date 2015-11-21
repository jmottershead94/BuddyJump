package com.example.app.jason.ragerelease.app.Framework.Physics;

import com.example.app.jason.ragerelease.app.Framework.AnimatedSprite;
import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.app.Framework.Resources;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Created by Win8 on 06/07/2015.
 */
public class DynamicBody extends AnimatedSprite
{
    protected boolean grow = false;
    private BodyDef bodyDef = null;
    private static final String TAG = "TKT";

    public DynamicBody(final Resources gameResources, int objectID)
    {
        super(gameResources, objectID);
    }

    public void bodyInit(Vector2 positions, Vector2 dimensions, float angle)
    {
        // Initialising the sprite.
        init(positions, dimensions, angle);
        spawnLocation = new Vector2(positions.getX(), positions.getY());

        // Setting up the body definition.
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(new Vec2(getBox2DXPosition(getSpriteLeft()), getBox2DYPosition(getSpriteTop())));
        bodyDef.angle = (angle * ((float) Math.PI / 180.0f));
        body = new Body(bodyDef, resources.getWorld());
        body = resources.getWorld().createBody(bodyDef);
        body.setFixedRotation(true);
        body.setTransform(new Vec2(getBox2DXPosition(getSpriteLeft()), getBox2DYPosition(getSpriteTop())), bodyDef.angle);

        PolygonShape box = new PolygonShape();
        box.setAsBox(getBox2DSize(dimensions.getX()) * box2DDynamicBodyXOffset, getBox2DSize(dimensions.getY()) * box2DDynamicBodyYOffset);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.2f;
        body.createFixture(fixtureDef);

        // Setting the connection between sprites and the body.
        body.setUserData(this);
    }

    public void updateBody()
    {
        float newX = body.getPosition().x;
        float newY = body.getPosition().y;

        setPosition(getFrameworkXPosition(newX), getFrameworkYPosition(newY));

        //setAngle(angle);
        //setAngle(rotationTest++);
    }
}
