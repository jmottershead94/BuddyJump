// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

/**
 * Created by Jason Mottershead on 08/07/2015.
 */

// MainThread IS A Thread, therefore inherits from it.
public class MainThread extends Thread
{
    // Attributes.
    // Private attributes.
    // Standard library attributes.
    private static long FPS = 0;                // Used to keep track of the FPS from the game activity.
    private static final String TAG = "TKT";    // Used for debugging.
    private boolean isRunning = false;          // To check to see if the thread is currently running or not.

    // My framework attributes.
    private Game gameView = null;               // Gives access to the game activity.

    // Methods.
    //////////////////////////////////////////////////////////
    //======================================================//
    //					    Constructor						//
    //======================================================//
    // This will set up the main thread attributes, some    //
    // passed down from the game activity.                  //
    //////////////////////////////////////////////////////////
    public MainThread(final Game game, final long desiredFPS)
    {
        // Setting up the main thread constructor.
        super();

        // Initialising local attributes.
        gameView = game;
        FPS = desiredFPS;
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //					        run					        //
    //======================================================//
    // This will update the game every frame and be used to //
    // keep a steady frame rate. Hopefully keep around      //
    // 60FPS.                                               //
    //////////////////////////////////////////////////////////
    @Override
    public void run()
    {
        // Setting up attributes to make the main thread work correctly.
        float dt = 0.0f;
        int skipTicks = 1000 / (int) FPS;
        int maxFrameSkip = 10;
        long nextGameTick = System.currentTimeMillis();
        int loops = 0;

        // While the game thread is currently running.
        while (isRunning)
        {
            loops = 0;

            // While the current time is greater than the next game frame/tick.
            // AND the number of loops is less than the maximum number of frames skipped.
            while ((System.currentTimeMillis() > nextGameTick) && (loops < maxFrameSkip))
            {
                // Update the game.
                gameView.update(dt);

                // Add on any skipped frames.
                nextGameTick += skipTicks;

                // Increment the loop.
                loops++;
            }

            // Used to work out any disposition in the skipped frames.
            dt = ((float) ((System.currentTimeMillis() + skipTicks) - nextGameTick) / (float) skipTicks);
        }
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //					    setRunning  			        //
    //======================================================//
    // This function will set the private running attribute //
    // of this thread.                                      //
    //////////////////////////////////////////////////////////
    public void setRunning(final boolean running) {
        isRunning = running;
    }
}