// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jason Mottershead on 14/10/2015.
 */

// This class will provide easy access to other activites.
public class NavigationButton
{
    // This will provide a result for when a navigation button has been pressed.
    public void isPressed(final Button button, final Activity currentActivity, final Class<?> nextActivityClass)
    {
        // Set an onClickListener for the button.
        button.setOnClickListener(new View.OnClickListener()
        {
            // When the button has been clicked.
            @Override
            public void onClick(View view)
            {
                // Creating a new intent/activity.
                Intent intent = new Intent(currentActivity, nextActivityClass);

                // Starting the new activity.
                currentActivity.startActivity(intent);
            }
        });
    }
}
