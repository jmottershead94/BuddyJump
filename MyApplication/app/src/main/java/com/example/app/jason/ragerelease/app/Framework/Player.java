package com.example.app.jason.ragerelease.app.Framework;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.app.Framework.Physics.DynamicBody;
import com.example.app.jason.ragerelease.app.Framework.Physics.StaticBody;

import org.jbox2d.common.Vec2;

/**
 * Created by Win8 on 16/07/2015.
 */

public class Player
{
    // Attributes.
    // Public attributes.
    public boolean beingTouched = false;
    public boolean tap = false;
    public int distance = 0;
    public TextView distanceText = null;
    public Vector2 touchPosition = null;

    // Private attributes.
    private static final String TAG = "TKT";
    private static boolean paused = false;

    private int numberOfPunches = 0;
    private Level level = null;
    private Resources resources = null;

    // Methods.
    //////////////////////////////////////////////////////////
    //======================================================//
    //					Constructor							//
    //======================================================//
    // This will initialise the player object, which will	//
    // call the dynamic body initialiser, and will then		//
    // call the sprite initialiser.			                //
    // Local variables will be initialised here.            //
    //////////////////////////////////////////////////////////
    public Player(final Resources gameResources, final Level gameLevel)
    {
        // Declaring local variables.
        resources = gameResources;
        touchPosition = new Vector2(0.0f, 0.0f);
        level = gameLevel;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((resources.getScreenWidth() - (resources.getScreenWidth() / 3)), (resources.getScreenHeight() / 16), 0, 0);

        distanceText = new TextView(resources.getContext());
        distanceText.setText("Distance: " + distance);
        distanceText.setTextColor(Color.WHITE);
        distanceText.setTextSize(20.0f);
        distanceText.setLayoutParams(layoutParams);
        resources.getBackground().addView(distanceText);
    }

    public void uiControls(final Button buttonPause)
    {
        // Checking any button click events.
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click response code here...
                // Display Pause Menu.
                paused = true;
                buttonPause.setVisibility(View.INVISIBLE);
                pauseMenu(buttonPause);
            }
        });
    }

    private void pauseMenu(final Button buttonPause)
    {
        final Button buttonResume = new Button(resources.getContext());

        // Adding the resume button to the display.
        buttonResume.setText("Resume");
        buttonResume.setClickable(true);

        // Only adds in a resume button for the pause menu.
        resources.getBackground().addView(buttonResume);

        // Clicking on the resume button.
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take away pause menu here.
                buttonResume.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
                paused = false;
            }
        });
    }

    public void update(float dt)
    {
        // Normal player updates.
        //distance++;

        // Checking to see if the player squares are still within the level borders.
        //checkInsideLevelBorder();
    }

    public static boolean isPaused() { return paused; }
}
