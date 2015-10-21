package com.example.app.jason.ragerelease.app.Framework;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by Win8 on 17/10/2015.
 */
public class Resources
{
    // Attributes.
    private Context context = null;
    private RelativeLayout background = null;
    private int screenWidth = 0;
    private int screenHeight = 0;

    // Methods.
    public Resources(final Context gameContext, final RelativeLayout gameBackground, final int gameScreenWidth, final int gameScreenHeight)
    {
        // Setting up the local variables.
        context = gameContext;
        background = gameBackground;
        screenWidth = gameScreenWidth;
        screenHeight = gameScreenHeight;
    }

    // Getters.
    // This function will return the game context.
    public Context getContext()             { return context; }

    // This function will return the current background that we are working with.
    public RelativeLayout getBackground()   { return background; }

    // This function will return the device screen width.
    public int getScreenWidth()             { return screenWidth; }

    // This function will return the device screen height.
    public int getScreenHeight()            { return screenHeight; }
}
