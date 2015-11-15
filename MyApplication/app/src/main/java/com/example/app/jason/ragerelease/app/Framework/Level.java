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
    public Player player = null;
    private static final String TAG = "TKT";
    private int levelNumber = 1;
    public Game game = null;
    public LevelGenerator levelGenerator = null;
    private Resources resources = null;

    public void init(final Resources gameResources, final Game gameView)
    {
        // Initialising local variables.
        resources = gameResources;
        game = gameView;
        player = new Player(resources, this);
        levelGenerator = new LevelGenerator(resources, 2, 4, 1);
        levelGenerator.buildLevel();    // Builds the first level.
        levelGenerator.addToView();
    }

    //////////////////////////////////////////////////////////////
    //      Third level seems to not work correctly?            //
    //      The first player square of the second level does    //
    //      not collide properly with target square.            //
    //      CHECK THIS OUT.                                     //
    //////////////////////////////////////////////////////////////

    public void newLevel()
    {
        // Clear the current level.
        levelGenerator.clearLevel();
        levelNumber++;

        // Decide what levels I want here.
        if(levelNumber == 2)
        {
            // Generate a new level.
            levelGenerator.generateNewLevel(2, 3, 1);
        }
        else if(levelNumber == 3)
        {
            levelGenerator.generateNewLevel(2, 5, 1);
        }
        else if(levelNumber == 4)
        {
            levelGenerator.generateNewLevel(2, 5, 2);
        }
        else if(levelNumber == 5)
        {
            levelGenerator.generateNewLevel(3, 5, 2);
        }
        else
        {
            levelGenerator.generateNewLevel(3, 5, 0);
        }

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
//            case ObjectID.SPAWNGATE:
//            {
//                StaticBody spawnGate = (StaticBody) object.body.getUserData();
//                //object.removeTexture();
//                spawnGate.setColour(255, 0, 0, 255);
////                spawnGate.setDimensions(0.0f, 0.0f);
////                spawnGate.resetBoxDimensions(world);
//
//                //object.translateFramework(new Vector2((float)screenWidth, (float)screenHeight));
//                //object.body.destroyShape(object.body.getShapeList());
//                //getLevelObjects().remove(object);
//
//                break;
//            }
            // If the player square needs to be removed.
            case ObjectID.PLAYERSQUARE:
            {
                // Makes the texture blank to remove the previous texture from the screen.
                object.removeTexture();

                // Removes the actual body of the object from the world.
                //object.body.destroyShape(object.body.getShapeList());
                //world.destroyBody(object.body);
                object.body.destroyFixture(object.body.getFixtureList());
                object.body.getWorld().destroyBody(object.body);

                // Reset the boolean flag.
                object.remove = false;

                // Removes the object from the level objects vector.
                getLevelObjects().remove(object);

                levelGenerator.reduceNumberOfPlayerSquares();

                break;
            }
            // If a target square needs to be removed.
            case ObjectID.TARGETSQUARE:
            {
                object.removeTexture();
                //object.body.destroyShape(object.body.getShapeList());
                //world.destroyBody(object.body);
                object.body.destroyFixture(object.body.getFixtureList());
                object.body.getWorld().destroyBody(object.body);
                object.remove = false;
                getLevelObjects().remove(object);
                Log.d(TAG, "Removed the target square.");

                if(levelGenerator.getNumberOfPlayerSquares() == 0)
                {
                    newLevel();
                }

                break;
            }
            case ObjectID.BREAKABLEPLATFORM:
            {
                object.removeTexture();
                object.setColour(0, 0, 0, 0);
                //object.body.destroyShape(object.body.getShapeList());
                object.body.destroyFixture(object.body.getFixtureList());
                object.body.getWorld().destroyBody(object.body);
                object.remove = false;
                getLevelObjects().remove(object);

                break;
            }
            // The default case for any removal situations if none of the above cases are met.
            default:
            {
                break;
            }
        }
    }

    private void handleLevelObjects()
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
                if (object.getID() == ObjectID.PLAYERSQUARE)
                {
                    DynamicBody playerSquare = (DynamicBody) object.body.getUserData();
                    playerSquare.updateBody();
                    playerSquare.animateSprite(0.002f);

                    // If a player square needs to respawn.
                    if(playerSquare.respawn)
                    {
                        // Set the player square at the spawn location.
                        playerSquare.translateFramework(object.getSpawnLocation());
                        playerSquare.respawn = false;
                    }

                    if(player.beingTouched)
                    {
                        playerSquare.translateFramework(player.touchPosition);
                    }
                }

                // Animates all of the target squares.
                if (object.getID() == ObjectID.TARGETSQUARE)
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
            // Get the colliding bodies.
            Body bodyA = contact.getFixtureA().getBody();
            Body bodyB = contact.getFixtureB().getBody();

            // Converting the two colliding bodies into objects that we can work with.
            AnimatedSprite gameObject = (AnimatedSprite) bodyA.getUserData();
            AnimatedSprite gameObjectB = (AnimatedSprite) bodyB.getUserData();

            // Collision response here.
            // If the collisions are not columns, platforms, or breakable platforms.
            if((gameObject.getID() != ObjectID.COLUMN && gameObjectB.getID() != ObjectID.COLUMN)
                && (gameObject.getID() != ObjectID.PLATFORM && gameObjectB.getID() != ObjectID.PLATFORM)
                && (gameObject.getID() != ObjectID.BREAKABLEPLATFORM && gameObjectB.getID() != ObjectID.BREAKABLEPLATFORM))
            {
                // Then check the rest of the important collisions.
                // If the player collides with a bullet.

                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                // FOR SOME REASON ObjectID.PLAYERSQUARE and ObjectID.IDNAME do not work, actual numbers do though... //
                //////////////////////////////////////////////////////////////////////////////////////////////////////////

                if ((gameObject.getID() == 7) && (gameObjectB.getID() == 12))
                {
                    Log.d(TAG, "Player square has matched up with the target square.");

                    // Make the player respawn.
                    gameObject.respawn = true;
                }
                // Otherwise, if the player collides with a target square.
                else if ((gameObject.getID() == 7) && (gameObjectB.getID() == 8))
                {
                    Log.d(TAG, "Player square has matched up with the target square.");

                    // If the player square and the target square have matching texture coordinates.
                    if ((gameObject.getTextureCoordinates().getX() == gameObjectB.getTextureCoordinates().getX())
                            && (gameObject.getTextureCoordinates().getY() == gameObjectB.getTextureCoordinates().getY()))
                    {
                        Log.d(TAG, "Player square has matched up with the target square.");

                        // Remove them from the level.
                        gameObject.remove = true;
                        gameObjectB.remove = true;
                    }
                    // Otherwise, the player square and the target square do not have matching texture coordinates.
                    else
                    {
                        // Make the player square respawn.
                        gameObject.respawn = true;
                    }
                }
            }

            // Get the next contact point.
            contact = contact.getNext();
        }
    }

    public void update(float dt)
    {
        // Updating local variables.
        player.update();

        // Local function calls.
        handleLevelObjects();

        // Check any collisions in the level.
        checkCollisions();
    }

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
