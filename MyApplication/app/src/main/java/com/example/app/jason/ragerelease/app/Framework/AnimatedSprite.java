package com.example.app.jason.ragerelease.app.Framework;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.util.Random;

/**
 * Created by Win8 on 30/06/2015.
 */

public class AnimatedSprite extends Sprite
{
    // Box2D conversion attributes.
    protected static float box2DFrameworkScale = 100.0f;
    protected static float box2DFrameworkOffsetX = 0.0f;
    protected static float box2DFrameworkOffsetY = 0.0f;
    protected static float box2DStaticXOffset = 0.5f;
    protected static float box2DStaticYOffset = 0.25f;
    protected static float box2DDynamicBodyXOffset = 0.25f;
    protected static float box2DDynamicBodyYOffset = 0.75f;

    // Animated sprite attributes.
    protected Body body = null;
    protected int id = 0;
    protected boolean shouldMove = false;
    protected boolean respawn = false;
    protected Vector2 spawnLocation = null;
    private float animationFrameDuration = 0.1f;
    private float animationTime = 0.0f;
    private int numberOfFrames = 0;
    protected Resources resources = null;

    public AnimatedSprite(final Resources gameResources, int objectID)
    {
        super(gameResources.getContext());
        resources = gameResources;
        id = objectID;
    }

    public void setAnimationFrames(int animationFrames)
    {
        numberOfFrames = animationFrames;
    }

    public void animateSprite(float ticks)
    {
        // Add the time passed since the last update to the animation timer.
        //animationTime += ticks;
        animationTime += ticks;

        // If the animation timer has passed the length of time for one frame (in seconds).
        // Move to the next frame.
        if(animationTime > animationFrameDuration)
        {
            // Reset the animation timer.
            animationTime -= animationFrameDuration;

            // Move the texture coordinates to the next sprite on the sprite sheet.
            textureCoordinates.setX(textureCoordinates.getX() + textureDimensions.getX());

            // If we have reached the end of the animation.
            if(textureCoordinates.getX() > (numberOfFrames - 1) * textureDimensions.getX())
            {
                textureCoordinates.setX(0.0f);
            }

            // Redraws the new sprite.
            postInvalidate();
        }
    }

    // This will reset the body and the sprite to the correct position.
    // Using Box2D coordinates.
    protected void translateBox2D(Vector2 box2DPosition)
    {
        setPosition(getFrameworkXPosition(box2DPosition.getX()), getFrameworkYPosition(box2DPosition.getY()));
        body.setTransform(new Vec2(box2DPosition.getX(), box2DPosition.getY()), 0.0f);
    }

    // Using framework coordinates.
    protected void translateFramework(Vector2 frameworkPosition)
    {
        setPosition(frameworkPosition.getX(), frameworkPosition.getY());
        body.setTransform(new Vec2(getBox2DXPosition(frameworkPosition.getX()), getBox2DYPosition(frameworkPosition.getY())), 0.0f);
    }

    // GETTERS.
    // Getting FRAMEWORK COORDINATES.
    // Convert Box2D coordinates into local framework coordinates.
    public float getFrameworkXPosition(float box2DX)   { return ((box2DX * box2DFrameworkScale) + box2DFrameworkOffsetX); }
    public float getFrameworkYPosition(float box2DY)   { return (resources.getScreenHeight() - (box2DY * box2DFrameworkScale + box2DFrameworkOffsetY)); }
    public float getFrameworkSize(float dimension)     { return (dimension * box2DFrameworkScale); }

    // Getting BOX2D COORDINATES.
    // Convert local framework coordinates into Box2D coordinates.
    public float getBox2DXPosition(float frameworkX)   { return ((frameworkX - box2DFrameworkOffsetX) / box2DFrameworkScale); }
    public float getBox2DYPosition(float frameworkY)   { return ((frameworkY - resources.getScreenHeight() + box2DFrameworkOffsetY) / -box2DFrameworkScale); }
    public float getBox2DSize(float dimension)         { return (dimension / box2DFrameworkScale); }

    // Normal getters.
    public int getID()                  { return id; }
    public Vector2 getSpawnLocation()   { return spawnLocation; }
}
