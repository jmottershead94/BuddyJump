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

// Enemy Selection IS A Character Selection, therefore inherits from it.
public class EnemySelection extends CharacterSelection
{
    // Attributes.
    private int currentEnemyImage = 0;
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
        init("enemy", "menemyImageIndex");
        applyOptions(this, enemyImages, false);

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentEnemyImage = gameSettings.getInt("menemyImageIndex", 0);

        Toast.makeText(this, "Enemy currently using " + currentEnemyImage, Toast.LENGTH_SHORT).show();
    }
}
