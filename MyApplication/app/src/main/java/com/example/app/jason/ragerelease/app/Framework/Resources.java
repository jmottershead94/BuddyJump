package com.example.app.jason.ragerelease.app.Framework;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import org.jbox2d.dynamics.World;

/**
 * Created by Win8 on 17/10/2015.
 */
public class Resources
{
    // Attributes.
    private Activity activity = null;
    private Context context = null;
    private RelativeLayout background = null;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private World world;

    // Methods.
    public Resources(final Activity gameActivity, final Context gameContext, final RelativeLayout gameBackground, final int gameScreenWidth, final int gameScreenHeight, final World gameWorld)
    {
        // Setting up the local variables.
        activity = gameActivity;
        context = gameContext;
        background = gameBackground;
        screenWidth = gameScreenWidth;
        screenHeight = gameScreenHeight;
        world = gameWorld;
    }

    // Setters.
    public void setBackground(final RelativeLayout gameBackground) { background = gameBackground; }

    // Getters.
    // This will return the current game activity.
    public Activity getActivity()       { return activity; }

    // This function will return the game context.
    public Context getContext()             { return context; }

    // This function will return the current background that we are working with.
    public RelativeLayout getBackground()   { return background; }

    // This function will return the device screen width.
    public int getScreenWidth()             { return screenWidth; }

    // This function will return the device screen height.
    public int getScreenHeight()            { return screenHeight; }

    // This function will return the current Box2D world we are using.
    public World getWorld() { return world; }
}
