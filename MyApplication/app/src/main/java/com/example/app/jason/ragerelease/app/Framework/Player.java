package com.example.app.jason.ragerelease.app.Framework;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
    }

    public void uiControls(final Activity gameView, final Button buttonPause)
    {
        // Checking any button click events.
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click response code here...
                // Display Pause Menu.
                paused = true;
                buttonPause.setVisibility(View.INVISIBLE);
                pauseMenu(resources.getBackground(), gameView, buttonPause);
            }
        });
    }

    private void pauseMenu(final RelativeLayout gameBackground, final Activity gameView, final Button buttonPause)
    {
        final Button buttonResume = new Button(gameView);

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

    // Checking to see if a touch is within certain bounds.
    private boolean touchCollisionTest(AnimatedSprite object)
    {
        if(((touchPosition.getX() > object.getSpriteLeft()) && (touchPosition.getX() < object.getSpriteRight()))
            && ((touchPosition.getY() > object.getSpriteTop()) && (touchPosition.getY() < object.getSpriteBottom())))
        {
            return true;
        }

        return false;
    }

//    public void checkInsideLevelBorder()
//    {
//        for(AnimatedSprite object : level.getLevelObjects())
//        {
//            if(object.getID() == ObjectID.PLAYER)
//            {
//                // If the player is to the left of the first column,
//                // or to the right of the last column.
//                if ((object.getSpriteLeft() < 0.0f)
//                    || (object.getSpriteRight() > resources.getScreenWidth())
//                    || (object.getSpriteTop() < 0.0f)
//                    || (object.getSpriteBottom() > resources.getScreenHeight()))
//                {
//                    object.respawn = true;
//                }
//            }
//        }
//    }

    public void update(float dt)
    {
        // Normal player updates.

        // Checking to see if the player squares are still within the level borders.
        //checkInsideLevelBorder();
    }

    public static boolean isPaused() { return paused; }
}
