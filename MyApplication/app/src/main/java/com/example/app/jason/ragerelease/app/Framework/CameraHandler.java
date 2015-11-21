package com.example.app.jason.ragerelease.app.Framework;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.app.jason.ragerelease.R;

import java.io.File;

/**
 * Created by Win8 on 21/11/2015.
 */
public class CameraHandler extends Activity
{
    // Attributes.
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public boolean takenPicture = false;
    private Resources resources = null;

    // Methods.
    public CameraHandler(final Activity selectionScreen)
    {
        // An intent that required an image to be captured.
        // Secure means the device is secured with a pin, password etc.
        // Apps responding to this intent must not expose personal content like photos or videos on the device.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);

        // This should go to the camera app.
        selectionScreen.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        onActivityResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, 0, intent);
        takenPicture = true;
        //selectionScreen.onActivityResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, 0, intent);
    }

    public CameraHandler(final Resources gameResources)
    {
        resources = gameResources;
    }

    public Bitmap getLastPicture()
    {
        // Find the last picture
        String[] projection = new String[]
        {
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        };

        final Cursor cursor = resources.getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        // Put it in the image view
        if(cursor != null)
        {
            if (cursor.moveToFirst())
            {
                String imageLocation = cursor.getString(1);
                File imageFile = new File(imageLocation);

                if (imageFile.exists())
                {
                    // TODO: is there a better way to do this?
                    Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                    return bm;
                }
            }

            cursor.close();
        }



        return null;
    }

}
