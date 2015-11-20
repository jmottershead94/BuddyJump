// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.ImageAdapter;
import com.example.app.jason.ragerelease.app.Framework.Level;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Resources;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.w3c.dom.Text;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Game IS AN Activity, therefore inherits from it.
public class SelectionScreen extends Activity
{
    // Attributes.


    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

        // Initialising variables.
        final Button playerSelectionButton = (Button) findViewById(R.id.playerSelectionButton);
        final Button enemySelectionButton = (Button) findViewById(R.id.enemySelectionButton);
        final Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        final Button playGameButton = (Button) findViewById(R.id.playGameButton);
        final NavigationButton button = new NavigationButton();

        // Getting the stored image arrays from player and enemy sprite selection.
        //int playerImages[] = getIntent().getIntArrayExtra("playerImages");
        //int enemyImages[] = getIntent().getIntArrayExtra("enemyImages");

        // Passing the images along to the next class.
        //Intent intent = new Intent(this, Game.class);
        //intent.putExtra("playerImages", playerImages);
        //intent.putExtra("enemyImages", enemyImages);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(playerSelectionButton, this, PlayerSelection.class);
        button.isPressed(enemySelectionButton, this, EnemySelection.class);
        button.isPressed(mainMenuButton, this, MainMenu.class);
        button.isPressed(playGameButton, this, Game.class);
    }
}
