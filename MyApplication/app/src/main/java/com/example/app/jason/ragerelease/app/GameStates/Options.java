// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Options IS AN Activity, therefore inherits from it.
// This class will provide access to game options, and save them.
public class Options extends Activity
{
    // Attributes.
    private static final String PREFS_NAME = "MyPrefsFile";
    private final int duration = Toast.LENGTH_SHORT;
    private RelativeLayout background;
    private Boolean playerCameraCheckedStatus = false;
    private Switch optionOne = null;
    private RadioGroup optionTwo = null;

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // Setting up the button to go back to the main menu.
        final Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        final NavigationButton button = new NavigationButton();
        background = (RelativeLayout) findViewById(R.id.optionsBackground);
        optionOne = (Switch) findViewById(R.id.optionOne);
        optionTwo = (RadioGroup) findViewById(R.id.skyOptions);

        // If there is no saved instance state.
        // There is no current state for the options menu.
        if(savedInstanceState != null)
        {
            // Get the previously assigned options values.
            playerCameraCheckedStatus = savedInstanceState.getBoolean("moptionOneCheckedStatus");
        }

        // Saving options for repeated use.
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        playerCameraCheckedStatus = gameSettings.getBoolean("moptionOneCheckedStatus", optionOne.isChecked());

        // Setting each option to its' corresponding checked status.
        optionOne.setChecked(playerCameraCheckedStatus);

        // Handling all of the option responses.
        OptionResponses();

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(mainMenuButton, this, MainMenu.class);
    }

    private void OptionResponses()
    {
        // Local attributes.
        final Context context = getApplicationContext();
        final CharSequence optionOneOnText = "Players will take camera images for sprites.";
        final CharSequence optionOneOffText = "Players will use sprite images.";
        final CharSequence optionTwoOnText = "Companions will use camera images for sprites.";
        final CharSequence optionTwoOffText = "Companions will use sprite images";
        final CharSequence optionThreeOnText = "Option 3 is now on.";
        final CharSequence optionThreeOffText = "Option 3 is now off.";

        // What happens when option one has been checked.
        optionOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // If the switch is on.
                if (isChecked)
                {
                    // Checked response code here...
                    // Set the options background colour to blue.
                    playerCameraCheckedStatus = true;
                    Toast.makeText(context, optionOneOnText, duration).show();
                }
                // Otherwise, the switch is off.
                else
                {
                    playerCameraCheckedStatus = false;
                    Toast.makeText(context, optionOneOffText, duration).show();
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        final CharSequence saveMessage = "Jasons_App options saved.";

        // Save UI changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putBoolean("moptionOneCheckedStatus", playerCameraCheckedStatus);

        super.onSaveInstanceState(savedInstanceState);

        Toast.makeText(this, saveMessage, duration).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Just in case the application is killed off.
        super.onRestoreInstanceState(savedInstanceState);

        playerCameraCheckedStatus = savedInstanceState.getBoolean("moptionOneCheckedStatus");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        final CharSequence saveMessage = "Jasons_App options saved.";

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Toast.makeText(this, saveMessage, duration).show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        editor.putBoolean("moptionOneCheckedStatus", optionOne.isChecked());

        editor.apply();
    }
}
