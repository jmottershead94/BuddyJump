package com.example.app.jason.ragerelease.app.Framework;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Physics.DynamicBody;
import com.example.app.jason.ragerelease.app.Framework.Physics.StaticBody;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Win8 on 02/08/2015.
 */
public class LevelGenerator
{
    private static final String TAG = "TKT";
    private static float groundY = 0.0f;
    private static int topLimit = 250;
    private static int numberOfColumns = 0;
    private int numberOfPlayerSquares = 0;
    private int numberOfTargetSquares = 0;
    private int numberOfPlatformsPerColumn = 0;
    private int numberOfEnemies = 0;
    private RelativeLayout background = null;
    private StaticBody groundFloor = null;
    private Vector<AnimatedSprite> objects = null;
    private Resources resources = null;
    private Level level = null;

    public LevelGenerator(final Resources gameResources, int columns, int numberOfPlatforms, int enemies, Level gameLevel)
    {
        // Setting the local level parameters.
        resources = gameResources;
        numberOfColumns = columns;
        numberOfPlayerSquares = numberOfColumns - 1;
        numberOfTargetSquares = numberOfPlayerSquares;
        numberOfPlatformsPerColumn = numberOfPlatforms;
        numberOfEnemies = enemies;
        level = gameLevel;
        objects = new Vector<AnimatedSprite>();             // Initialising the vector of level objects.

        createGround();
    }

    public void generateNewLevel(int columns, int platforms, int enemies)
    {
        numberOfColumns = columns;
        numberOfPlayerSquares = numberOfColumns - 1;
        numberOfTargetSquares = numberOfPlayerSquares;
        numberOfPlatformsPerColumn = platforms;
        numberOfEnemies = enemies;
    }

    public void buildLevel()
    {
        Vector2 objectPosition = new Vector2(0.0f, 0.0f);   // Setting up a 2D vector for level object positions.

        // Creating all of the level objects.
        createGround();
        createPlayer();

        Log.d(TAG, "Number of bodies in the world at START = " + resources.getWorld().getBodyCount());
    }

    private void createGround()
    {
        groundFloor = new StaticBody(resources, ObjectID.ANIMATEDGROUND);
        groundY = resources.getScreenHeight() - 150.0f;
        groundFloor.setAnimationFrames(8);
        groundFloor.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.0f, 0.0f), new Vector2(0.125f, 1.0f));
        groundFloor.bodyInit(new Vector2(0.0f, groundY), new Vector2(resources.getScreenWidth(), 40.0f), 0.0f);
        groundFloor.setVisibility(View.INVISIBLE);
    }

    private void createPlayer()
    {
        DynamicBody playerSquare = new DynamicBody(resources, ObjectID.PLAYER);
        playerSquare.setAnimationFrames(8);
        playerSquare.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.0f, 0.0f), new Vector2(0.125f, 1.0f));
        playerSquare.bodyInit(new Vector2(resources.getScreenWidth() * 0.25f, resources.getScreenHeight() * 0.5f), new Vector2(resources.getScreenWidth() * 0.125f, resources.getScreenWidth() * 0.125f), 0.0f);
        objects.add(playerSquare);
    }

    public void addToView()
    {
        if (resources.getBackground() != null)
        {
            if (groundFloor.getVisibility() != View.VISIBLE)
            {
                resources.getBackground().addView(groundFloor);
                groundFloor.setVisibility(View.VISIBLE);
            }

            for (AnimatedSprite object : getObjects())
            {
                if (object != null)
                {
                    resources.getBackground().addView(object);
                }
            }
        }
    }

    public void clearLevel()
    {
        // If there are objects in the vector.
        if(!objects.isEmpty())
        {
            // Iterate through each of the objects in the level.
            for(AnimatedSprite object : getObjects())
            {
                // If there is an object.
                if(object != null)
                {
                    // Removing all of the sprites from the screen.
                    // If the object does not have a texture, but is using standard colours.
                    if(object.colour != null)
                    {
                        // Set the alpha value to 0 and RGB to 0.
                        object.setColour(0, 0, 0, 0);
                    }
                    // Otherwise, the object has a texture.
                    else if(object.image != null)
                    {
                        // Set the texture to be transparent.
                        object.removeTexture();
                    }

                    // Should destroy the body after the level.
                    object.body.destroyFixture(object.body.getFixtureList());
                    resources.getWorld().destroyBody(object.body);
                }
            }

            // Remove all of the objects from the list.
            objects.clear();
        }
    }

    // Getters.
    public Vector<AnimatedSprite> getObjects()  { return objects; }
}
