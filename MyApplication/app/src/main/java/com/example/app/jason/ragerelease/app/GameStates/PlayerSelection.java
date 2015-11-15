// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private int currentPlayerImage = 0;
    private Integer[] playerImages =                      // Getting access to the images from the drawable folder.
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

        // Initialise the game.
        init("player", "mplayerImageIndex");
        applyOptions(this, playerImages, true);

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentPlayerImage = gameSettings.getInt("mplayerImageIndex", 0);

        Toast.makeText(this, "Player currently using " + currentPlayerImage, Toast.LENGTH_SHORT).show();
    }
}
