// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// MainMenu IS AN Activity, therefore inherits from it.
// This class will allow access to any of the app features (to the game, options, credits, or back to the title screen).
public class MainMenu extends Activity
{
    // Attributes.


    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Setting up each button to access them from the main menu xml file.
        final Button playButton = (Button) findViewById(R.id.playButton);
        final Button optionsButton = (Button) findViewById(R.id.optionsButton);
        final Button creditsButton = (Button) findViewById(R.id.creditsButton);
        final NavigationButton button = new NavigationButton();

        // If any of the buttons are pressed on the main menu.
        // Take the user to the correct activity depending on the button pressed.
        button.isPressed(playButton, this, Game.class);
        button.isPressed(optionsButton, this, Options.class);
        button.isPressed(creditsButton, this, Credits.class);
    }
}
