package com.example.app.jason.ragerelease.app.Framework;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.app.Framework.Physics.StaticBody;
import com.example.app.jason.ragerelease.app.GameStates.Game;
import com.example.app.jason.ragerelease.app.Framework.Physics.DynamicBody;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.Vector;

/**
 * Created by Win8 on 17/07/2015.
 */
public class Level implements View.OnTouchListener
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
        player.distanceText.bringToFront();

        // Listen out for touches in the level.
        resources.getBackground().setOnTouchListener(this);
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

    // Checking to see if a touch is within certain bounds.
    private boolean touchCollisionTest(AnimatedSprite object)
    {
        if(((player.touchPosition.getX() > object.getSpriteLeft()) && (player.touchPosition.getX() < object.getSpriteRight()))
                && ((player.touchPosition.getY() > object.getSpriteTop()) && (player.touchPosition.getY() < object.getSpriteBottom())))
        {
            return true;
        }

        return false;
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        // Touch response here.
        player.touchPosition.set(event.getX(), event.getY());
        int eventAction = event.getAction();

        switch (eventAction)
        {
            // If the player touches the screen.
            case MotionEvent.ACTION_DOWN:
            {
                if(!player.isPaused())
                {
                    for (AnimatedSprite object : getLevelObjects())
                    {
                        if ((object.getID() == ObjectID.PLAYER) || (object.getID() == ObjectID.ENEMY))
                        {
                            if (touchCollisionTest(object))
                            {
                                player.tap = true;

                                // Change to a jumping animation.
                                object.changeTexture(new Vector2((5.0f / 7.0f), (1.0f / 3.0f)));
                                object.setAnimationFrames(2);

                                // Make the object jump.
                                object.body.applyLinearImpulse(new Vec2(0.0f, 4.0f), object.body.getWorldCenter());
                            }
                        }
                    }
                }

                break;
            }

            // If the player keeps holding the touch on the screen.
            case MotionEvent.ACTION_MOVE:
            {
                //beingTouched = true;
                break;
            }

            // If the player releases their touch on the screen.
            case MotionEvent.ACTION_UP:
            {
                player.beingTouched = false;

                if(!player.isPaused())
                {
                    if (player.tap)
                    {
                        for (AnimatedSprite object : getLevelObjects())
                        {
                            if ((object.getID() == ObjectID.PLAYER) || (object.getID() == ObjectID.ENEMY))
                            {
                                object.changeTexture(new Vector2(0.0f, 0.0f));
                                object.setAnimationFrames(6);
                            }
                        }

                        player.tap = false;
                    }
                }

                break;
            }

            // If none of the above cases are met, then just default to not being touched.
            default:
            {
                player.beingTouched = false;
                player.tap = false;
                break;
            }
        }

        return true;
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

                if(object.getID() == ObjectID.ANIMATEDSPRITE)
                {
                    object.animateSprite(0.01f * dt);
                }

                // Animates all of the player squares.
                if (object.getID() == ObjectID.PLAYER)
                {
                    DynamicBody playerSprite = (DynamicBody) object.body.getUserData();
                    playerSprite.updateBody();
                    playerSprite.animateSprite(0.01f * dt);

                    // If a player square needs to respawn.
                    if(playerSprite.respawn)
                    {
                        // Set the player square at the spawn location.
                        playerSprite.translateFramework(object.getSpawnLocation());
                        playerSprite.respawn = false;
                    }
                }

                // Animates all of the target squares.
                if (object.getID() == ObjectID.OBSTACLE)
                {
                    StaticBody enemySprite = (StaticBody) object.body.getUserData();

                    if(enemySprite.getSpriteRight() < 10.0f)
                    {
                        enemySprite.translateFramework(enemySprite.getSpawnLocation());
                    }
                    else
                    {
                        enemySprite.updateBody(new Vector2(-0.05f, 0.0f));
                    }
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
            if(contact.isTouching())
            {
                // Get the colliding bodies.
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                // Converting the two colliding bodies into objects that we can work with.
                AnimatedSprite gameObjectA = (AnimatedSprite) bodyA.getUserData();
                AnimatedSprite gameObjectB = (AnimatedSprite) bodyB.getUserData();

                // Collision test.
                // If the player is in contact with the ground.
                if((gameObjectA.getID() == ObjectID.PLAYER && gameObjectB.getID() == ObjectID.OBSTACLE))
                {
                    // Do collision response here...
                    // Change back to the hurt animation.
                    gameObjectA.changeTexture(new Vector2((6.0f / 7.0f), 0.0f));
                    gameObjectA.setAnimationFrames(0);
                    player.setGameOver(true);
                }

                // Get the next contact point.
                contact = contact.getNext();
            }
        }
    }

    public void update(float dt)
    {
        // Incrementing player distance.
        player.distance++;
        updatePlayerScore();
        //player.distanceText.setText("Distance: " + player.distance);

        // Local function calls.
        handleLevelObjects(dt);

        // Check any collisions in the level.
        checkCollisions();
    }

    private void updatePlayerScore()
    {
        // Creating a new thread.
        new Thread()
        {
            // When this new thread runs.
            @Override
            public void run()
            {
                // Try to...
                try
                {
                    // Make sure that the new level objects are added to the background on the correct thread.
                    resources.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the player score.
                            player.distanceText.setText("Distance: " + player.distance);
                        }
                    });

                    // Slight loading time, without this, below catch gives an error.
                    Thread.sleep(500);
                }
                // Catch any expections with this thread.
                catch (InterruptedException e)
                {
                    // Print a stack trace so we know where we went wrong.
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // Getters.
    public Vector<AnimatedSprite> getLevelObjects() { return levelGenerator.getObjects(); }
}
