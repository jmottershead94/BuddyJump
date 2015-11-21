package com.example.app.jason.ragerelease.app.Framework;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    private int playerImage = 0;
    private int enemyImage = 0;
    private Vector<AnimatedSprite> objects = null;
    private Resources resources = null;
    private Level level = null;

    public LevelGenerator(final Resources gameResources, Level gameLevel, final int gamePlayerImage, final int gameEnemyImage)
    {
        // Setting the local level parameters.
        resources = gameResources;
        playerImage = gamePlayerImage;
        enemyImage = gameEnemyImage;
        level = gameLevel;
        objects = new Vector<AnimatedSprite>();             // Initialising the vector of level objects.
    }

    public void buildLevel()
    {
        // Creating all of the level objects.
        createGround();
        createBackground();
        createPlayer();
        createEnemy();
    }

    private void createGround()
    {
        StaticBody groundFloor = new StaticBody(resources, ObjectID.GROUND);
        groundY = resources.getScreenHeight() - 190.0f;
        groundFloor.setTexture(R.drawable.sprite_sheet, new Vector2(0.0f, 0.0f), new Vector2(0.125f, 0.0625f));
        groundFloor.bodyInit(new Vector2(0.0f, groundY), new Vector2(resources.getScreenWidth(), 80.0f), 0.0f);
        objects.add(groundFloor);
    }

    private void createPlayer()
    {
        DynamicBody player = new DynamicBody(resources, ObjectID.PLAYER);
        player.setAnimationFrames(8);
        player.setTexture(playerImage, new Vector2(0.0f, 0.0f), new Vector2(1.0f, 1.0f));
        player.bodyInit(new Vector2(resources.getScreenWidth() * 0.25f, resources.getScreenHeight() * 0.5f), new Vector2(resources.getScreenWidth() * 0.125f, resources.getScreenWidth() * 0.125f), 0.0f);
        objects.add(player);
    }

    private void createEnemy()
    {
        AnimatedSprite enemy = new AnimatedSprite(resources, ObjectID.ENEMY);
        enemy.init(new Vector2(resources.getScreenWidth() * 0.5f, resources.getScreenHeight() * 0.5f), new Vector2(resources.getScreenWidth() * 0.125f, resources.getScreenWidth() * 0.125f), 0.0f);
        enemy.setTexture(enemyImage, new Vector2(0.0f, 0.0f), new Vector2(1.0f, 1.0f));
        enemy.setAnimationFrames(8);
        objects.add(enemy);
    }

    private void createBackground()
    {
        AnimatedSprite backGround = new AnimatedSprite(resources, ObjectID.SPRITE);
        backGround.init(new Vector2(0.0f, resources.getScreenHeight() - 230.0f), new Vector2(resources.getScreenWidth(), 80.0f), 0.0f);
        backGround.setTexture(R.drawable.sprite_sheet, new Vector2(0.0f, 0.0625f), new Vector2(0.125f, 0.0625f));
        backGround.setAnimationFrames(8);
        objects.add(backGround);
    }

    public void addToView()
    {
        if (resources.getBackground() != null)
        {
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
