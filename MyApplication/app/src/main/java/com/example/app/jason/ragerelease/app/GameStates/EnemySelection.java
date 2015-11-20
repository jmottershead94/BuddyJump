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

// Enemy Selection IS A Character Selection, therefore inherits from it.
public class EnemySelection extends CharacterSelection
{
    // Attributes.
    private int currentEnemyImageIndex = 0;
    private int currentEnemyImage = 0;
    private int[] enemyImages =                      // Getting access to the images from the drawable folder.
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

        // Initialise the selection screen.
        init("enemy", "menemyImageIndex");
        applyOptions(this, enemyImages, false);

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentEnemyImage = gameSettings.getInt("menemyImageIndex", 0);

        Toast.makeText(this, "Enemy currently using " + currentEnemyImageIndex, Toast.LENGTH_SHORT).show();

        button.isPressedAndSendData(saveButton, this, SelectionScreen.class, "enemyImage", enemyImages[currentEnemyImageIndex]);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        final CharSequence saveMessage = "Image selection saved.";

        // Save UI changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putInt("menemyImage", enemyImages[currentEnemyImageIndex]);

        // Save the current state.
        super.onSaveInstanceState(savedInstanceState);

        // Display a saved message.
        Toast.makeText(this, saveMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Just in case the application is killed off.
        super.onRestoreInstanceState(savedInstanceState);

        // Once the activity has been restored, place the previous image index into the current one.
        // So that we have not lost the number for it.
        currentEnemyImage = savedInstanceState.getInt("menemyImage");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        final CharSequence saveMessage = "Image selection saved.";

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Toast.makeText(this, saveMessage, Toast.LENGTH_SHORT).show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        // Placing the int into saved files to be used later.
        editor.putInt("menemyImage", enemyImages[currentEnemyImageIndex]);

        // Applying the changes.
        editor.apply();
    }
}
