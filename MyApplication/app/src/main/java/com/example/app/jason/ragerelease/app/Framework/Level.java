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

import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private int interval = 1;                           // 3 seconds.
    private Timer timer = null;
    private ScheduledExecutorService scheduler = null;
    private ScheduledFuture<?> future = null;

    public void init(final Resources gameResources, final Game gameView, final int gamePlayerImage, final int gameEnemyImage)
    {
        // Initialising local variables.
        resources = gameResources;
        game = gameView;
        player = new Player(resources, this);
        levelGenerator = new LevelGenerator(resources, this, gamePlayerImage, gameEnemyImage);
        levelGenerator.buildLevel(1, 1);    // Builds the first level.
        levelGenerator.addToView();
        player.distanceText.bringToFront();
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // This schedules respawning an obstacle once it has gone off screen.
        // Running on a new thread.
        future = scheduler.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                if(!player.isPaused())
                {
                    player.distance++;
                }
            }
            // Executing at a regular interval of 1 second.
        }, interval, interval, TimeUnit.SECONDS);

        // Listen out for touches in the level.
        resources.getBackground().setOnTouchListener(this);
    }

    public void newLevel()
    {
        // Resetting the player's distance score.
        player.distance = 0;

        // Clear the current level.
        levelGenerator.clearLevel();

        // Increment the current level number.
        levelNumber++;

        if(levelNumber == 2)
        {
            // Builds the new level.
            levelGenerator.buildLevel(2, 1);
        }
        else if(levelNumber == 3)
        {
            // Builds the new level.
            levelGenerator.buildLevel(2, 2);
        }

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
                        if ((object.getID() == ObjectID.CHARACTERONE) || (object.getID() == ObjectID.CHARACTERTWO))
                        {
                            if (touchCollisionTest(object))
                            {
                                player.tap = true;

                                if(!object.isUsingCameraImage())
                                {
                                    // Change to a jumping animation.
                                    object.changeTexture(new Vector2((5.0f / 7.0f), (1.0f / 3.0f)));
                                    object.setAnimationFrames(2);
                                }
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
                            if ((object.getID() == ObjectID.CHARACTERONE) || (object.getID() == ObjectID.CHARACTERONE))
                            {
                                if(!object.isUsingCameraImage())
                                {
                                    object.changeTexture(new Vector2(0.0f, 0.0f));
                                    object.setAnimationFrames(6);
                                }
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

    private void handleLevelObjects(float dt)
    {
        for (AnimatedSprite object : getLevelObjects())
        {
            if (object != null)
            {
                if(object.getID() == ObjectID.ANIMATEDSPRITE)
                {
                    object.animateSprite(0.01f * dt);
                }

                // Animates and moves all of the player characters.
                if (object.getID() == ObjectID.CHARACTERONE || object.getID() == ObjectID.CHARACTERTWO)
                {
                    DynamicBody playerSprite = (DynamicBody) object.body.getUserData();
                    playerSprite.updateBody();

                    if(!playerSprite.isUsingCameraImage())
                    {
                        playerSprite.animateSprite(0.01f * dt);
                    }

                    // If a player square needs to respawn.
                    if(playerSprite.respawn)
                    {
                        // Set the player square at the spawn location.
                        playerSprite.translateFramework(object.getSpawnLocation());
                        playerSprite.respawn = false;
                    }
                }

                // Moves all of the obstacles.
                if (object.getID() == ObjectID.OBSTACLE)
                {
                    StaticBody enemySprite = (StaticBody) object.body.getUserData();

                    if(enemySprite.getSpriteRight() > 0.0f)
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
                if((gameObjectA.getID() == ObjectID.CHARACTERONE && gameObjectB.getID() == ObjectID.OBSTACLE)
                    || (gameObjectA.getID() == ObjectID.CHARACTERTWO) && gameObjectB.getID() == ObjectID.OBSTACLE)
                {
                    // Do collision response here...
                    // Change back to the hurt animation.
                    if(!gameObjectA.isUsingCameraImage())
                    {
                        gameObjectA.changeTexture(new Vector2((6.0f / 7.0f), 0.0f));
                        gameObjectA.setAnimationFrames(0);
                    }

                    player.setGameOver(true);
                }

                // Get the next contact point.
                contact = contact.getNext();
            }
        }
    }

    public void update(final float dt)
    {
        // If the level has been completed.
        finishedLevel();

        // Incrementing player distance.
        updatePlayerDistance();

        // Local function calls.
        handleLevelObjects(dt);

        // Check any collisions in the level.
        checkCollisions();
    }

    private void updatePlayerDistance()
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
                    resources.getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(!player.isGameOver())
                            {
                                // Update the player score.
                                player.distanceText.setText("Distance: " + player.distance);
                            }
                            else
                            {
                                future.cancel(true);
                            }
                        }
                    });

                    // Slight loading time, without this, below catch gives an error.
                    Thread.sleep(300);
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

    private void finishedLevel()
    {
        if(levelNumber == 1)
        {
            if (player.distance == 10)
            {
                newLevel();
            }
        }
        else if(levelNumber >= 2)
        {
            if(player.distance == 15)
            {
                newLevel();
            }
        }
    }

    // Getters.
    public Vector<AnimatedSprite> getLevelObjects() { return levelGenerator.getObjects(); }
}
