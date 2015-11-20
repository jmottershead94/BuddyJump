package com.example.app.jason.ragerelease.app.Framework;

import android.util.Log;

import com.example.app.jason.ragerelease.app.GameStates.Game;
import com.example.app.jason.ragerelease.app.Framework.Physics.DynamicBody;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.Vector;

/**
 * Created by Win8 on 17/07/2015.
 */
public class Level
{
    // Attributes.
    private static final String TAG = "TKT";
    private int levelNumber = 1;
    public Game game = null;
    public LevelGenerator levelGenerator = null;
    public Player player = null;
    private Resources resources = null;

    public void init(final Resources gameResources, final Game gameView, final int gamePlayerImage, final int gameEnemyImage)
    {
        // Initialising local variables.
        resources = gameResources;
        game = gameView;
        player = new Player(resources, this);
        levelGenerator = new LevelGenerator(resources, this, gamePlayerImage, gameEnemyImage);
        levelGenerator.buildLevel();    // Builds the first level.
        levelGenerator.addToView();
    }

    public void newLevel()
    {
        // Clear the current level.
        levelGenerator.clearLevel();
        //levelNumber++;

        // Builds the new level.
        levelGenerator.buildLevel();

        // Updates the UI thread to add all of the new level objects to the screen.
        game.render();
    }

    public void objectRemoveResponses(AnimatedSprite object)
    {
        // Switch between the ID of the objects.
        switch(object.getID())
        {
            // The default case for any removal situations if none of the above cases are met.
            default:
            {
                break;
            }
        }
    }

    private void handleLevelObjects(float dt)
    {
        for (AnimatedSprite object : getLevelObjects())
        {
            if (object != null)
            {
                if (object.remove)
                {
                    objectRemoveResponses(object);
                    break;
                }

                // Animates all of the player squares.
                if (object.getID() == ObjectID.PLAYER)
                {
                    DynamicBody playerSprite = (DynamicBody) object.body.getUserData();
                    playerSprite.updateBody();
                    playerSprite.animateSprite(0.002f);

                    // If a player square needs to respawn.
                    if(playerSprite.respawn)
                    {
                        // Set the player square at the spawn location.
                        playerSprite.translateFramework(object.getSpawnLocation());
                        playerSprite.respawn = false;
                    }

                    if(player.beingTouched)
                    {
                        playerSprite.translateFramework(player.touchPosition);
                    }
                }

                // Animates all of the target squares.
                if (object.getID() == ObjectID.ENEMY)
                {
                    object.animateSprite(0.003f);
                }
            }
        }
    }

    public void addToView()
    {
        levelGenerator.addToView();
    }

    private void checkCollisions()
    {
        // Get the head of the contact list.
        Contact contact = resources.getWorld().getContactList();

        // Get the number of contacts in this world.
        int contactCount = resources.getWorld().getContactCount();

        // Cycle through the contacts.
        for(int contactNumber = 0; contactNumber < contactCount; contactNumber++)
        {
            //if(contact.isTouching())
            //{
                // Get the colliding bodies.
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                // Converting the two colliding bodies into objects that we can work with.
                AnimatedSprite gameObjectA = (AnimatedSprite) bodyA.getUserData();
                AnimatedSprite gameObjectB = (AnimatedSprite) bodyB.getUserData();

                // Collision test.
                //if(gameObjectA.getID() == ObjectID.PLAYER && gameObjectB.getID == ObjectID.ENEMY)
                //{
                //  // Do collision response here...
                //}

                // Get the next contact point.
                contact = contact.getNext();
            //}
        }
    }

    public void update(float dt)
    {
        // Local function calls.
        handleLevelObjects(dt);

        // Check any collisions in the level.
        checkCollisions();
    }

    // Getters.
    public Vector<AnimatedSprite> getLevelObjects() { return levelGenerator.getObjects(); }

//    // Otherwise, if the bottom of the sprite is inside of an existing object.
//    if (((newObject.getSpriteBottom() + (newObject.getSpriteHeight() * bottomSpriteMultiplier)) < (object.getSpriteBottom() + (object.getSpriteHeight() * bottomSpriteMultiplier)))
//        && ((newObject.getSpriteBottom() + (newObject.getSpriteHeight() * bottomSpriteMultiplier)) > (object.getSpriteTop() - (newObject.getSpriteHeight() * bottomSpriteMultiplier))))
//    {
//        return true;
//    }
//    // If the top of the new object intersects with an existing object.
//    else if (((newObject.getSpriteTop() - (newObject.getSpriteHeight() * bottomSpriteMultiplier)) < (object.getSpriteBottom() + (object.getSpriteHeight() * bottomSpriteMultiplier)))
//        && ((newObject.getSpriteTop() - (newObject.getSpriteHeight() * bottomSpriteMultiplier)) > (object.getSpriteTop() - (object.getSpriteHeight() * bottomSpriteMultiplier))))
//    {
//        return true;
//    }
//    else
//    {
//        return false;
//    }

//    // If the bottom of the new object is inside of an existing object.
//    if ((newObject.getSpriteBottom() < bottomLimit)
//        && (newObject.getSpriteBottom() > topLimit))
//    {
//        return true;
//    }
//    // Otherwise, if the top of the new object intersects with an existing object.
//    else if ((newObject.getSpriteTop() < bottomLimit)
//        && (newObject.getSpriteTop() > topLimit))
//    {
//        return true;
//    }
//    else
//    {
//        return false;
//    }
}
