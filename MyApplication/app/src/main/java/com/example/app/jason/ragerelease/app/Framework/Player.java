package com.example.app.jason.ragerelease.app.Framework;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.app.Framework.Physics.StaticBody;

import org.jbox2d.common.Vec2;

/**
 * Created by Win8 on 16/07/2015.
 */

public class Player implements View.OnTouchListener
{
    // Attributes.
    private static final String TAG = "TKT";
    private static boolean paused = false;
    public boolean beingTouched = false;
    public Vector2 touchPosition = null;
    private Level level = null;
    private Resources resources = null;

    // Methods.
    //////////////////////////////////////////////////////////
    //======================================================//
    //					Constructor							//
    //======================================================//
    // This will initialise the player object, which will	//
    // call the dynamic body initialiser, and will then		//
    // call the sprite initialiser.			                //
    // Local variables will be initialised here.            //
    //////////////////////////////////////////////////////////
    public Player(final Resources gameResources, final Level gameLevel)
    {
        // Declaring local variables.
        resources = gameResources;
        touchPosition = new Vector2(0.0f, 0.0f);
        level = gameLevel;

        // TRY COLLISIONS WITH THIS THING..................... hue hue hue
        //resources.getWorld().setContactListener(new PlayerContactListener(resources.getWorld(), gameLevel, this));
        resources.getBackground().setOnTouchListener(this);
    }

    public void uiControls(final Activity gameView, final Button buttonPause)
    {
        // Checking any button click events.
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click response code here...
                // Display Pause Menu.
                paused = true;
                buttonPause.setVisibility(View.INVISIBLE);
                pauseMenu(resources.getBackground(), gameView, buttonPause);
            }
        });
    }

    private void pauseMenu(final RelativeLayout gameBackground, final Activity gameView, final Button buttonPause)
    {
        final Button buttonResume = new Button(gameView);

        // Adding the resume button to the display.
        buttonResume.setText("Resume");
        buttonResume.setClickable(true);

        // Only adds in a resume button for the pause menu.
        resources.getBackground().addView(buttonResume);
        //gameBackground.addView(buttonResume);

        // Clicking on the resume button.
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take away pause menu here.
                buttonResume.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
                paused = false;
            }
        });
    }

    public void gameControls(final RelativeLayout gameBackground)
    {
        // Any touch input will be used here?
        // Motion Event or something?
        // Touch input.
//        resources.getBackground().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Touch response here.
//                touchPosition.set(event.getX(), event.getY());
//                int eventAction = event.getAction();
//
//                switch (eventAction) {
//                    // If the player touches the screen.
//                    case MotionEvent.ACTION_DOWN: {
//                        // Iterating through the level objects.
//                        for (AnimatedSprite object : level.getLevelObjects()) {
//                            // If the game has not paused.
//                            if (!isPaused()) {
//                                if ((object.getID() == ObjectID.MOVEABLEPLATFORM)
//                                        || (object.getID() == ObjectID.BREAKABLEPLATFORM)) {
//                                    // If the player is touching an object in the level.
//                                    if (touchCollisionTest(new Vector2(object.getSpriteLeft(), object.getSpriteRight()), new Vector2(object.getSpriteTop(), object.getSpriteBottom()))) {
//                                        // Provide the correct response for each level object being touched.
//                                        touchingLevelObjectResponse(object);
//                                    }
//                                }
//                            }
//                        }
//
//                        break;
//                    }
//
//                    // If the player keeps holding the touch on the screen.
//                    case MotionEvent.ACTION_MOVE: {
//                        for (AnimatedSprite object : level.getLevelObjects()) {
//                            // If the game has not paused.
//                            if (!isPaused()) {
//                                if (object.getID() == ObjectID.PLAYERSQUARE) {
//                                    // If the player has touched within the player sprite.
//                                    if (touchCollisionTest(new Vector2(object.getSpriteLeft(), object.getSpriteRight()), new Vector2(object.getSpriteTop(), object.getSpriteBottom()))) {
//                                        // Set a boolean flag to create an appropriate response.
//                                        // If true, this will move the object around with touch input.
//                                        beingTouched = true;
//                                        //touchingLevelObjectResponse(object);
//                                    }
//                                }
//                            }
//                        }
//
//                        break;
//                    }
//
//                    // If the player releases their touch on the screen.
//                    case MotionEvent.ACTION_UP: {
//                        beingTouched = false;
//                        break;
//                    }
//
//                    // If none of the above cases are met, then just default to not being touched.
//                    default: {
//                        beingTouched = false;
//                        break;
//                    }
//                }
//
//                return true;
//            }
//        });
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        // Touch response here.
        touchPosition.set(event.getX(), event.getY());
        int eventAction = event.getAction();

        switch (eventAction)
        {
            // If the player touches the screen.
            case MotionEvent.ACTION_DOWN:
            {
                // Iterating through the level objects.
                for (AnimatedSprite object : level.getLevelObjects())
                {
                    // If the game has not paused.
                    if (!isPaused())
                    {
                        // If the object in the vector is a moveable/breakable platform.
                        if ((object.getID() == ObjectID.MOVEABLEPLATFORM)
                            || (object.getID() == ObjectID.BREAKABLEPLATFORM))
                        {
                            // Check to see if the player is touching the platform in the level.
                            if (touchCollisionTest(object))
                            {
                                // Provide the correct response for each level object being touched.
                                touchingLevelObjectResponse(object);
                            }
                        }
                    }
                }

                break;
            }

            // If the player keeps holding the touch on the screen.
            case MotionEvent.ACTION_MOVE:
            {
//                for (AnimatedSprite object : level.getLevelObjects())
//                {
//                    // If the game has not paused.
//                    if (!isPaused())
//                    {
//                        if (object.getID() == ObjectID.PLAYERSQUARE)
//                        {
//                            // If the player has touched within the player sprite.
//                            if (touchCollisionTest(object))
//                            {
//                                // Set a boolean flag to create an appropriate response.
//                                // If true, this will move the object around with touch input.
//                                beingTouched = true;
//                            }
//                        }
//                    }
//                }

                break;
            }

            // If the player releases their touch on the screen.
            case MotionEvent.ACTION_UP:
            {
                beingTouched = false;
                break;
            }

            // If none of the above cases are met, then just default to not being touched.
            default:
            {
                beingTouched = false;
                break;
            }
        }

        return true;
    }

    private void touchingLevelObjectResponse(AnimatedSprite object)
    {
        // Casting the animated sprite object into a static body to be able to perform Box2D transformations.
        StaticBody objectA = (StaticBody) object.body.getUserData();
        Vector2 newPosition = null;

        switch (objectA.getID())
        {
            case ObjectID.SPAWNGATE:
            {
                objectA.remove = true;
                break;
            }
            // If the object is a moveable platform.
            case (ObjectID.MOVEABLEPLATFORM):
            {
                // The platform needs to move to the right.
                if(objectA.shouldMove)
                {
                    // Creating a new Vector2 for the new position of the sprite/body.
                    newPosition = new Vector2((objectA.getSpritePosition().getX() + objectA.getSpriteWidth()), objectA.getSpritePosition().getY());

                    // If the new position of the moveable platform is not going to go inside of another playersquare.
                    if(!insidePlayerSquare(new Vector2(newPosition.getX(), newPosition.getX() + objectA.getSpriteWidth()), new Vector2(newPosition.getY(), newPosition.getY() + objectA.getSpriteHeight())))
                    {
                        // Resetting the position of the sprite.
                        objectA.setPosition(newPosition.getX(), newPosition.getY());

                        // Resetting the position of the body of the sprite.
                        objectA.body.setTransform(new Vec2(object.getBox2DXPosition(newPosition.getX()), object.getBox2DYPosition(newPosition.getY())), 0.0f);

                        // Changing the flag for moving the platform.
                        objectA.shouldMove = false;
                    }
                }
                // Otherwise, the platform needs to move left.
                else
                {
                    newPosition = new Vector2((objectA.getSpritePosition().getX() - objectA.getSpriteWidth()), objectA.getSpritePosition().getY());

                    // If the moveable platform is not going to go inside of another playersquare.
                    if(!insidePlayerSquare(new Vector2(newPosition.getX(), newPosition.getX() + objectA.getSpriteWidth()), new Vector2(newPosition.getY(), newPosition.getY() + objectA.getSpriteHeight())))
                    {
                        objectA.setPosition(newPosition.getX(), newPosition.getY());
                        objectA.body.setTransform(new Vec2(object.getBox2DXPosition(newPosition.getX()), object.getBox2DYPosition(newPosition.getY())), 0.0f);
                        objectA.shouldMove = true;
                    }
                }

                break;
            }
            case ObjectID.BREAKABLEPLATFORM:
            {
                // If the object has been tapped less than 3 times.
                if(objectA.numberOfTaps < 3)
                {
                    // Play an animation with the texture of a crumbling platform.
                    objectA.changeTexture(new Vector2(objectA.getTextureCoordinates().getX() + objectA.getTextureDimensions().getX(), 0.0f));
                    //objectA.animateSprite(0.001f);

                    // Reset the dimensions of the object.
//                    objectA.setDimensions(objectA.getSpriteWidth(), (objectA.getSpriteHeight() - (objectA.getSpriteHeight() * 0.25f)));
//                    objectA.resetBoxDimensions(world);
                }
                // If the player has tapped this object 3 times.
                else
                {
                    // Remove the object.
                    //objectA.setDimensions(0.0f, 0.0f);
                    objectA.remove = true;
                }

                objectA.numberOfTaps++;

                break;
            }
            // If none of the above cases have been met.
            default:
            {
                // Do nothing.
                break;
            }
        }
    }

    // Checking to see if a touch is within certain bounds.
    private boolean touchCollisionTest(AnimatedSprite object)
    {
        if(((touchPosition.getX() > object.getSpriteLeft()) && (touchPosition.getX() < object.getSpriteRight()))
            && ((touchPosition.getY() > object.getSpriteTop()) && (touchPosition.getY() < object.getSpriteBottom())))
        {
            return true;
        }

        return false;
    }

    private boolean insidePlayerSquare(Vector2 spriteLeftAndRight, Vector2 spriteTopAndBottom)
    {
        for(AnimatedSprite object : level.getLevelObjects())
        {
            if(object.getID() == ObjectID.PLAYERSQUARE)
            {
                // If the bottom or top of the player will be encased in the object.
                if((object.getSpriteBottom() > spriteTopAndBottom.getX() && object.getSpriteBottom() < spriteTopAndBottom.getY())
                    || (object.getSpriteTop() > spriteTopAndBottom.getX() && object.getSpriteTop() < spriteTopAndBottom.getY()))
                {
                    // If the left or right of the player will hit the sprite.
                    if ((object.getSpriteLeft() > spriteLeftAndRight.getX()) && (object.getSpriteRight() < spriteLeftAndRight.getY()))
                    {
                        return true;
                    }
                }
                else if(object.getSpriteCenter().getX() > spriteLeftAndRight.getX() && object.getSpriteCenter().getX() < spriteLeftAndRight.getY())
                {
                    if(object.getSpriteCenter().getY() > spriteTopAndBottom.getX() && object.getSpriteCenter().getY() < spriteTopAndBottom.getY())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void checkInsideLevelBorder()
    {
        for(AnimatedSprite object : level.getLevelObjects())
        {
            if(object.getID() == ObjectID.PLAYERSQUARE)
            {
                // If the player is to the left of the first column,
                // or to the right of the last column.
                if ((object.getSpriteLeft() < 0.0f)
                    || (object.getSpriteRight() > resources.getScreenWidth())
                    || (object.getSpriteTop() < 0.0f)
                    || (object.getSpriteBottom() > resources.getScreenHeight()))
                {
                    object.respawn = true;
                }
            }
        }
    }

    public void update()
    {
        // Normal player updates.

        // Applying player touch controls.
        //gameControls(gameBackground);

        // Checking to see if the player squares are still within the level borders.
        checkInsideLevelBorder();
    }

    public static boolean isPaused() { return paused; }
}
