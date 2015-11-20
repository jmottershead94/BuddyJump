// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.CharacterSelection;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Player Selection IS A Character Selection, therefore inherits from it.
public class PlayerSelection extends CharacterSelection
{
    // Attributes.
    private int currentPlayerImageIndex = 0;
    private int[] playerImages =                      // Getting access to the images from the drawable folder.
    {
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        // Initialise the character selection screen.
        // What the message should say, and what the preference file attribute should be called.
        init("player", "mplayerImageIndex", "mplayerImage");
        applyOptions(this, playerImages, true);

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentPlayerImageIndex = gameSettings.getInt("mplayerImageIndex", 0);

        Toast.makeText(this, "Player currently using " + currentPlayerImageIndex, Toast.LENGTH_SHORT).show();
    }
}
