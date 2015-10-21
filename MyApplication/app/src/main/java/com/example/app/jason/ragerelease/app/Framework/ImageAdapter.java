// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.app.jason.ragerelease.R;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// ImageAdapter IS A BaseAdapter, therefore inherits from it.
public class ImageAdapter extends BaseAdapter
{
    // Attributes.
    private Context context;                        // Getting access to the context for the game.
    private Integer[] images =                      // Getting access to the images from the drawable folder.
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
    public ImageAdapter(Context gameContext)
    {
        context = gameContext;
    }

    // Returns the length of the image array.
    public int getCount()
    {
        return images.length;
    }

    // Returns the drawable at the position.
    public Object getItem(int position)
    {
        // Error checking.
        // If the position is an index that can be accessed.
        if(position >= 0 && position < images.length)
        {
            // Return the object at that position.
            return images[position];
        }

        // Otherwise, the position is not in the array index, therefore return nothing.
        return null;
    }

    // Returns an ID for the item.
    public long getItemId(int position)
    {
        return 0;
    }

    // This function creates a new image view, for each item referenced by the Adapter.
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;

        // If it's not recycled.
        if(convertView == null)
        {
            // Initialise some attributes.
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(275, 275));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);
        }
        // Otherwise, if the image is repeated.
        else
        {
            // Just make the image with the same attributes as the previous image.
            imageView = (ImageView) convertView;
        }

        // Set the image view to the drawable.
        imageView.setImageResource(images[position]);

        // Return the image view for use in the grid view.
        return imageView;
    }
}