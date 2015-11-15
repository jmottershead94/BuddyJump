// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.ImageAdapter;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

import org.w3c.dom.Text;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Game IS AN Activity, therefore inherits from it.
public class EnemySelection extends Activity
{
    // Attributes.
    private static final String PREFS_NAME = "MyPrefsFile";                     // Where the options will be saved to, whether they are true or false.
    private static final String TAG = "TKT";                                    // Used for debugging.
    private boolean optionOneChecked, optionTwoChecked, optionThreeChecked;     // Used for gaining access to the options from the options activity.
    private RelativeLayout background = null;
    private GridView imageSelectionView = null;
    private TextView textView = null;
    private ImageAdapter enemyImageSelection = null;
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
        init();
        applyOptions();
    }

    // An initialisation function for setting up the game.
    private void init()
    {
        // Initialising variables.
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        final NavigationButton button = new NavigationButton();
        background = (RelativeLayout) findViewById(R.id.characterSelectionBackground);
        textView = (TextView) findViewById(R.id.characterSelectionTextView);

        // Accessing saved options.
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        optionOneChecked = gameSettings.getBoolean("moptionOneCheckedStatus", false);
        optionTwoChecked = gameSettings.getBoolean("moptionTwoCheckedStatus", false);
        optionThreeChecked = gameSettings.getBoolean("moptionThreeCheckedStatus", false);

        // Setting the text view for the activity.
        textView.setTextSize(20.0f);
        textView.setText("Select an image for the enemy:");

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(saveButton, this, Game.class);
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //				    applyOptions   						//
    //======================================================//
    // This function will check to see the current state of //
    // the options, and provide an appropriate response.    //
    //////////////////////////////////////////////////////////
    public void applyOptions()
    {
        // Create different option responses here.
        // If the first option is ON.
        if(optionOneChecked)
        {
            // Let the player take a picture with the camera (user permissions required).
            // DEBUG - Set the background colour to blue.
            background.setBackgroundColor(Color.BLUE);
        }
        // If the first option is OFF.
        else
        {
            // Let the player select a sprite for the player character.
            imageSelectionView = (GridView) findViewById(R.id.characterImageSelectionView);
            enemyImageSelection = new ImageAdapter(this);
            enemyImageSelection.setImages(enemyImages);
            imageSelectionView.setAdapter(enemyImageSelection);
        }

        // If the second option is ON.
        if(optionTwoChecked)
        {
            // Create some text.
            TextView optionTwoText = new TextView(this);
            optionTwoText.setText("Option Two Enabled BIATCH.");

            // Add the text to the screen.
            background.addView(optionTwoText);
        }

        // If the third option is ON.
        if(optionThreeChecked)
        {
            // Rotate the background.
            background.setRotation(90.0f);
        }
    }
}
