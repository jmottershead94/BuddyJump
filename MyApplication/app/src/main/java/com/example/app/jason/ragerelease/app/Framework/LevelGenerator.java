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

    public LevelGenerator(final Resources gameResources, int columns, int numberOfPlatforms, int enemies)
    {
        // Setting the local level parameters.
        resources = gameResources;
        numberOfColumns = columns;
        numberOfPlayerSquares = numberOfColumns - 1;
        numberOfTargetSquares = numberOfPlayerSquares;
        numberOfPlatformsPerColumn = numberOfPlatforms;
        numberOfEnemies = enemies;
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
        createColumns(objectPosition);

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

    private void createColumns(Vector2 objectPosition)
    {
        final int columnGap = 180;
        int loopCount = 0;

        // Build all of the columns needed.
        for(int currentColumn = 0; currentColumn < numberOfColumns; currentColumn++)
        {
            // Creating a new column only when it is needed.
            StaticBody column = new StaticBody(resources, ObjectID.COLUMN);

            // Calculating the position of the current column.
            // Will set within a balanced ratio of the device screen, and increase along the x axis.
            objectPosition.set(((((float) resources.getScreenWidth() / (float) numberOfColumns) - (columnGap * 0.5f)) + (columnGap * (float) currentColumn)), 10.0f);

            // Setting the colour/texture of the column.
            column.setColour(255, 0, 0, 255);
            //column.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.25f, 0.0f), new Vector2(0.125f, 1.0f));

            // Initialising the body of the column.
            column.bodyInit(new Vector2(objectPosition.getX(), objectPosition.getY()), new Vector2(((float) resources.getScreenWidth() / 15.0f), groundY - objectPosition.getY()), 0.0f);

            // Adding the column into the level objects vector.
            objects.add(column);

            // Create player squares here!
            createPlayerSquares(column, columnGap, loopCount);

            // Create target squares here, similar to the player.
            createTargetSquares(column, columnGap, loopCount);

            // Create moveable platforms here!
            createMoveablePlatforms(column, columnGap, loopCount);

            // Creating breakable platforms here.
            createBreakablePlatforms(column, columnGap, loopCount);

            loopCount++;
        }

        // Clearing the object position for use on the next level object.
        objectPosition.set(0.0f, 0.0f);
    }

    private void createPlayerSquares(StaticBody column, final int columnGap, int loopCount)
    {
        // To make sure that the number of player squares does not exceed the number of columns.
        if(loopCount < numberOfPlayerSquares)
        {
            DynamicBody playerSquare = new DynamicBody(resources, ObjectID.PLAYERSQUARE);
            playerSquare.setAnimationFrames(8);
            playerSquare.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.0f, 0.0f), new Vector2(0.125f, 1.0f));
            playerSquare.bodyInit(new Vector2(column.getSpriteLeft() + column.getSpriteWidth() + 40.0f, column.getSpriteTop() + 80.0f), new Vector2(70.0f, 70.0f), 0.0f);
            objects.add(playerSquare);

            StaticBody spawnPlatform = new StaticBody(resources, ObjectID.MOVEABLEPLATFORM);
            spawnPlatform.setColour(255, 0, 255, 0);
            //spawnPlatform.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.5f, 0.0f), new Vector2(0.125f, 1.0f));
            spawnPlatform.bodyInit(new Vector2(column.getSpriteLeft() + column.getSpriteWidth(), playerSquare.getSpriteTop() + (playerSquare.getSpriteDimensions().getY() * 2.0f)), new Vector2(((float) columnGap * 0.8f), 30.0f), 0.0f);
            objects.add(spawnPlatform);
        }
    }

    private void createTargetSquares(StaticBody column, final int columnGap, int loopCount)
    {
        if(loopCount < numberOfTargetSquares)
        {
            StaticBody targetSquare = new StaticBody(resources, ObjectID.TARGETSQUARE);
            targetSquare.setAnimationFrames(8);
            targetSquare.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.0f, 0.0f), new Vector2(0.125f, 1.0f));
            targetSquare.bodyInit(new Vector2(column.getSpriteLeft() + column.getSpriteWidth(), groundY - 80.0f), new Vector2((float) columnGap - ((float) columnGap * 0.2f), 80.0f), 0.0f);
            objects.add(targetSquare);
        }
    }

    private void createMoveablePlatforms(StaticBody column, final int columnGap, int loopCount)
    {
        // numberOfColumns - 1 makes sure that there are no extra moveable platforms on the last column.
        if(loopCount < (numberOfColumns - 1))
        {
            for (int currentPlatform = 0; currentPlatform < numberOfPlatformsPerColumn; currentPlatform++)
            {
                Random random = new Random();
                int bottomLimit = (int)groundY - 130;

                // Random y coordinate for the platforms, bottom of the screen, above the ground, and above the target square but below the player square.
                int platformY = random.nextInt(bottomLimit - topLimit) + topLimit;

                StaticBody platform = new StaticBody(resources, ObjectID.MOVEABLEPLATFORM);
                platform.setColour(255, 255, 0, 0);
                //platform.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.5f, 0.0f), new Vector2(0.125f, 1.0f));
                platform.bodyInit(new Vector2(column.getSpriteLeft() + column.getSpriteWidth(), (float) platformY), new Vector2(((float) columnGap), 30.0f), 0.0f);

                // If there is another object in the surrounding area for this platform.
                if(checkSurroundingArea(platform))
                {
                    // Make the platform invisible.
                    platform.setColour(0, 0, 0, 0);

                    // Destroy the current platform body.
                    //platform.destroyBody();
                    platform.body.getWorld().destroyBody(platform.body);
                }
                // Otherwise, there are no objects in the surrounding area for the new platform.
                else
                {
                    // So add that object into the level objects vector.
                    objects.add(platform);
                }
            }
        }
    }

    private void createBreakablePlatforms(StaticBody column, final int columnGap, int loopCount)
    {
        // numberOfColumns - 1 makes sure that there are no extra breakable platforms on the last column.
        if(loopCount < (numberOfColumns - 1))
        {
            for (int currentPlatform = 0; currentPlatform < numberOfPlatformsPerColumn; currentPlatform++)
            {
                Random random = new Random();
                int bottomLimit = (int)groundY - 130;

                // Random y coordinate for the platforms, bottom of the screen, above the ground, and above the target square but below the player square.
                int platformY = random.nextInt(bottomLimit - topLimit) + topLimit;

                StaticBody platform = new StaticBody(resources, ObjectID.BREAKABLEPLATFORM);
                platform.setTexture(R.drawable.animated_spritesheet_test, new Vector2(0.5f, 0.0f), new Vector2(0.125f, 1.0f));
                platform.bodyInit(new Vector2(column.getSpriteLeft() + column.getSpriteWidth(), (float) platformY), new Vector2((float) columnGap, 30.0f), 0.0f);

                if(checkSurroundingArea(platform))
                {
                    platform.removeTexture();
                    platform.body.getWorld().destroyBody(platform.body);
                }
                else
                {
                    objects.add(platform);
                }
            }
        }
    }

    // This will loop through all of the current objects in the level.
    // And check to see if the new position for the new object overlaps with any current level object positions.
    public boolean checkSurroundingArea(AnimatedSprite newObject)
    {
        float bottomSpriteMultiplier = 2.0f;   // Used to adjust offset of sprites.

        for(AnimatedSprite object : getObjects())
        {
            // Only testing objects that would ever intersect.
            // Add new ID tests as necessary.
            if((object.getID() == ObjectID.MOVEABLEPLATFORM)
                || (object.getID() == ObjectID.BREAKABLEPLATFORM)
                || (object.getID() == ObjectID.STATICENEMY)
                || (object.getID() == ObjectID.DYNAMICENEMY))
            {
                // If the objects are on the same column.
                if(object.getSpriteLeft() == newObject.getSpriteLeft())
                {
                    float bottomLimit = (object.getSpriteBottom() + (object.getSpriteHeight() * bottomSpriteMultiplier));
                    float topLimit = (object.getSpriteTop() - (object.getSpriteHeight() * bottomSpriteMultiplier));

                    // If the bottom of the new object is inside of an existing object.
                    if ((newObject.getSpriteBottom() < bottomLimit)
                            && (newObject.getSpriteBottom() > topLimit))
                    {
                        return true;
                    }
                    // Otherwise, if the top of the new object intersects with an existing object.
                    else if ((newObject.getSpriteTop() < bottomLimit)
                            && (newObject.getSpriteTop() > topLimit))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
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
                }
            }

            // Remove all of the objects from the list.
            objects.clear();
        }
    }

    public Vector<AnimatedSprite> getObjects()  { return objects; }
    public int getNumberOfColumns()             { return numberOfColumns; }
    public int getNumberOfPlayerSquares()       { return numberOfPlayerSquares; }
    public int getNumberOfTargetSquares()       { return numberOfTargetSquares; }
    public int getNumberOfPlatforms()           { return numberOfPlatformsPerColumn; }

    public void reduceNumberOfPlayerSquares()   { numberOfPlayerSquares--; }
}
