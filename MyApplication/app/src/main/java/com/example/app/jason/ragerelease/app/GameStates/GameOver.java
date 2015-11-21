// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

/**
 * Created by Jason Mottershead on 21/11/2015.
 */

// GameOver IS AN Activity, therefore inherits from it.
// This class will provide credit where it is due.
public class GameOver extends Activity
{
    // Attributes.
    private static final String PREFS_NAME = "MyPrefsFile";                     // Getting access to the player distance.

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Load in options here...
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Setting up the button to go back to the main menu.
        final Button mainMenuButton = (Button) findViewById(R.id.gameOverMainMenuButton);
        final NavigationButton button = new NavigationButton();
        final TextView distanceText = (TextView) findViewById(R.id.gameOverDistanceText);

        // Getting the final distance score for the player.
        distanceText.setText("Final Distance: " + gameSettings.getInt("mplayerDistance", 0));

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(mainMenuButton, this, MainMenu.class);
    }
}