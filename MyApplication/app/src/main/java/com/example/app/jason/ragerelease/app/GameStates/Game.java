// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.ImageAdapter;
import com.example.app.jason.ragerelease.app.Framework.Level;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Player;
import com.example.app.jason.ragerelease.app.Framework.Resources;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Game IS AN Activity, therefore inherits from it.
public class Game extends Activity
{
    // Attributes.
    // Private attributes.
    // Standard library attributes.
    private static final long desiredFPS = 60;                                  // The desired frame rate for the game.
    private static final String PREFS_NAME = "MyPrefsFile";                     // Where the options will be saved to, whether they are true or false.
    private static int playerImageIndex = 0;                                    // What image the player currently wants to use for their character in the game.
    private static int enemyImageIndex = 0;                                     // What image the player current wants to use for their enemy in the game.
    private int playerImage = 0;
    private int enemyImage = 0;

    // Android attributes.
    private RelativeLayout background = null;                                   // Gives access to the relative layout background for the game.
    private Button pauseButton = null;                                          // Gives access to the pause button for the player.

    // JBox2D attributes.
    private World world;                                                        // Gives access to the jbox2D physics engine.
    private Vec2 gravity = null;                                                // Will be applied to dynamic objects.

    // My framework attributes.
    private MainThread gameThread = null;                                       // The main game thread that the game will be running mostly on.
    private Level level = null;                                                 // Provides levels for the player to play in.
    private Resources resources = null;                                         // Gives access to certain repeated resources (context, the game background, screen width , screen height, and the world), and narrows down parameters passed down.

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Initialise the game.
        init();

        // Setting the game thread to run.
        gameThread.setRunning(true);
        gameThread.start();
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //					    createWorld						//
    //======================================================//
    // This will create the jbox2D world, so that the       //
    // jbox2D library can be applied to this project.       //
    // This will also create gravity for any dynamic        //
    // objects.                                             //
    //////////////////////////////////////////////////////////
    public void createWorld()
    {
        gravity = new Vec2(0.0f, -10.0f);
        world = new World(gravity, false);
    }

    // An initialisation function for setting up the game.
    private void init()
    {
        // Load in options here...
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        playerImageIndex = gameSettings.getInt("mplayerImageIndex", 0);
        enemyImageIndex = gameSettings.getInt("menemyImageIndex", 0);
        playerImage = gameSettings.getInt("mplayerImage", 0);
        enemyImage = gameSettings.getInt("menemyImage", 0);

        // Setting up the screen dimensions.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        // Setting up JBox2D.
        createWorld();

        // Initialising variables.
        background = (RelativeLayout) findViewById(R.id.gameBackground);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        level = new Level();
        gameThread = new MainThread(this, desiredFPS);
        resources = new Resources(this, getApplicationContext(), background, displayMetrics.widthPixels, displayMetrics.heightPixels, world);

        // Initialising the level.
        //level.init(resources, this, playerImages[playerImageIndex], enemyImages[enemyImageIndex]);
        level.init(resources, this, playerImage, enemyImage);
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //				        update   						//
    //======================================================//
    // This function will be called every frame.            //
    // All update calls will be processed here.             //
    //////////////////////////////////////////////////////////
    public void update(float dt)
    {
        // Setting up the time step and iterations for jbox2D.
        float timeStep = 1.0f / desiredFPS;
        int velocityIterations = 6;
        int positionIterations = 4;

        checkGameOver();

        // If the player has not paused the game.
        if(!level.player.isPaused() && !level.player.isGameOver())
        {
            // Update the physics engine.
            resources.getWorld().step(timeStep, velocityIterations, positionIterations);

            // All other update calls here.
            // Update the level.
            level.update(dt);
        }

        // If the game is paused, the player should be able to unpause the game.
        // They can still control the pause menu even if the game is paused.
        level.player.uiControls(pauseButton);
    }

    private void checkGameOver()
    {
        if(level.player.isGameOver())
        {
            // Clear the current level.
            level.levelGenerator.clearLevel();
            level.player.setGameOver(false);
            level.player.setPaused(false);

            // Return to the main menu.
            Intent intent = new Intent(this, GameOver.class);
            startActivity(intent);
        }
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //				        render   						//
    //======================================================//
    // This function will update the game canvas when the   //
    // screen updates, when the screen is "dirty".          //
    // We do this on the UI thread so that there are no     //
    // conflicts with the original thread that added the    //
    // views in the first place.                            //
    // If this were to run normally without running on      //
    // the UI thread, errors would occur.                   //
    //////////////////////////////////////////////////////////
    public void render()
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
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Add all of the randomly generated objects whenever I want to the level.
                            level.addToView();
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

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        final CharSequence saveMessage = "Distance score saved.";

        // Save UI changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putInt("mplayerDistance", level.player.distance);

        super.onSaveInstanceState(savedInstanceState);

        Toast.makeText(this, saveMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        editor.putInt("mplayerDistance", level.player.distance);

        editor.apply();
    }
}
