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
public class CompanionSelection extends CharacterSelection
{
    // Attributes.
    private int currentCompanionImageIndex = 0;
    private int[] companionImages =                      // Getting access to the images from the drawable folder.
    {
            R.drawable.p1_front, R.drawable.p2_front,
            R.drawable.p3_front, R.drawable.p4_front,
            R.drawable.p5_front, R.drawable.p6_front,
            R.drawable.p7_front, R.drawable.p8_front
    };

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        // Initialise the selection screen.
        init("companion", "mcompanionImageIndex", "mcompanionImage");
        applyOptions(this, companionImages, false);

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentCompanionImageIndex = gameSettings.getInt("mcompanionImageIndex", 0);

        Toast.makeText(this, "Companion currently using " + currentCompanionImageIndex, Toast.LENGTH_SHORT).show();
    }
}
