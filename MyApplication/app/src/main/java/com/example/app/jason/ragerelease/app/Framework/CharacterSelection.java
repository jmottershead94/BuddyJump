// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.ImageAdapter;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.GameStates.Game;
import com.example.app.jason.ragerelease.app.GameStates.MainMenu;
import com.example.app.jason.ragerelease.app.GameStates.SelectionScreen;

import org.w3c.dom.Text;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Character Selection IS AN Activity, therefore inherits from it.
public class CharacterSelection extends Activity implements AdapterView.OnItemClickListener
{
    // Attributes.
    protected static final String PREFS_NAME = "MyPrefsFile";                   // Where the options will be saved to, whether they are true or false.
    protected Button saveButton;
    protected NavigationButton button;

    private static final String TAG = "TKT";                                    // Used for debugging.
    private boolean optionOneChecked, optionTwoChecked, optionThreeChecked;     // Used for gaining access to the options from the options activity.
    private RelativeLayout background = null;
    private GridView imageSelectionView = null;
    private ImageAdapter imageSelection = null;
    private int currentImageIndex = 0;
    private int[] sprites = null;
    private String gameSettingsName = null;
    private String imageSettingsName = null;

    // Methods.
    // An initialisation function for setting up the game.
    protected void init(String selectionMessage, String settingsName, String imageSettings)
    {
        // Initialising variables.
        final TextView textView = (TextView) findViewById(R.id.characterSelectionTextView);
        saveButton = (Button) findViewById(R.id.saveButton);
        button = new NavigationButton();
        background = (RelativeLayout) findViewById(R.id.characterSelectionBackground);
        imageSelectionView = (GridView) findViewById(R.id.characterImageSelectionView);
        imageSelection = new ImageAdapter(this);
        gameSettingsName = settingsName;
        imageSettingsName = imageSettings;

        // Accessing saved options.
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        optionOneChecked = gameSettings.getBoolean("moptionOneCheckedStatus", false);
        optionTwoChecked = gameSettings.getBoolean("moptionTwoCheckedStatus", false);
        optionThreeChecked = gameSettings.getBoolean("moptionThreeCheckedStatus", false);
        currentImageIndex = gameSettings.getInt(gameSettingsName, 0);

        // Setting the text view for the activity.
        textView.setTextSize(20.0f);
        textView.setText("Select an image for the " + selectionMessage + ":");

        // Telling the grid view to keep listening for any items that have been clicked on.
        imageSelectionView.setOnItemClickListener(this);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(saveButton, this, SelectionScreen.class);
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //				    applyOptions   						//
    //======================================================//
    // This function will check to see the current state of //
    // the options, and provide an appropriate response.    //
    //////////////////////////////////////////////////////////
    protected void applyOptions(final Context context, int[] images, boolean playerSelection)
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
            // Fill the sprites array with the images provided.
            sprites = images;

            // If it is a player selection.
            if(playerSelection)
            {
                // Let the player select a sprite for the player character.
                imageSelection.setImages(images);
                imageSelectionView.setAdapter(imageSelection);
            }
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
        else
        {
            // Fill the sprites array with the images provided.
            sprites = images;

            // If it is an enemy selection.
            if(!playerSelection)
            {
                // Let the player select a sprite for the enemy character.
                imageSelection.setImages(images);
                imageSelectionView.setAdapter(imageSelection);
            }
        }

        // If the third option is ON.
        if(optionThreeChecked)
        {
            // Rotate the background.
            background.setRotation(90.0f);
        }
    }

    public void onItemClick(AdapterView<?> parent, View gridElement, int position, long id)
    {
        currentImageIndex = position;
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        final CharSequence saveMessage = "Image selection saved.";

        // Save UI changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putInt(gameSettingsName, currentImageIndex);
        savedInstanceState.putInt(imageSettingsName, sprites[currentImageIndex]);

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
        currentImageIndex = savedInstanceState.getInt(gameSettingsName);
        sprites[currentImageIndex] = savedInstanceState.getInt(imageSettingsName);
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
        editor.putInt(gameSettingsName, currentImageIndex);
        editor.putInt(imageSettingsName, sprites[currentImageIndex]);

        // Applying the changes.
        editor.apply();
    }
}
