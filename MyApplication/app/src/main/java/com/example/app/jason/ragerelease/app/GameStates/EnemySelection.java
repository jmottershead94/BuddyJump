// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.os.Bundle;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.CharacterSelection;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Enemy Selection IS A Character Selection, therefore inherits from it.
public class EnemySelection extends CharacterSelection
{
    // Attributes.
    private Integer[] enemyImages =                      // Getting access to the images from the drawable folder.
    {
        R.drawable.splash_screen, R.drawable.splash_screen,
        R.drawable.splash_screen, R.drawable.splash_screen,
        R.drawable.splash_screen, R.drawable.splash_screen,
        R.drawable.splash_screen, R.drawable.splash_screen,
        R.drawable.splash_screen, R.drawable.splash_screen,
        R.drawable.splash_screen, R.drawable.splash_screen,
        R.drawable.splash_screen, R.drawable.splash_screen,
        R.drawable.splash_screen, R.drawable.splash_screen
    };

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        // Initialise the game.
        init("enemy");
        applyOptions(this, enemyImages, false);
    }
}