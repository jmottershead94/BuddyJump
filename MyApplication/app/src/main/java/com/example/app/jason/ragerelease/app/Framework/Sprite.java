package com.example.app.jason.ragerelease.app.Framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.R;

/**
 * Created by Win8 on 14/06/2015.
 */
public class Sprite extends View
{
    // Attributes.
    // Protected.
    protected boolean remove = false;
    protected float angle = 0.0f;
    protected Bitmap sprite, image;
    protected Paint colour = null;
    protected Vector2 position = null;
    protected Vector2 dimension = null;
    protected Vector2 textureCoordinates = null;
    protected Vector2 textureDimensions = null;

    // Private.
    private Rect test, destRect;
    private final String TAG = "TKT";

    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up the rectangles for the     //
    //  sprite canvas drawing.                      //
    //////////////////////////////////////////////////
    public Sprite(Context context)
    {
        super(context);
        test = new Rect();
        destRect = new Rect();
    }

    //////////////////////////////////////////////////
    //                  Initialise                  //
    //==============================================//
    //  Sets up the position (x and y) of the       //
    //  sprite and the dimensions                   //
    //  (width and height).                         //
    //  Also sets the rotation of the sprite.       //
    //////////////////////////////////////////////////
    public void init(Vector2 positions, Vector2 dimensions, float rotation)
    {
        position = new Vector2(positions.getX(), positions.getY());
        dimension = new Vector2(dimensions.getX(), dimensions.getY());
        setAngle(rotation);
    }

    //////////////////////////////////////////////////
    //              Setting Position                //
    //==============================================//
    //  Sets the vector 2 for the position.
    //////////////////////////////////////////////////
    public void setPosition(float x, float y) {
        position.set(x, y);
        postInvalidate();
//        invalidate();
    }

    public void setDimensions(float width, float height) {
        dimension.set(width, height);
        postInvalidate();
//        invalidate();
    }

    public void setAngle(float rotation)
    {
        angle = rotation;
    }

    public void setColour(int alpha, int red, int green, int blue)
    {
        colour = new Paint();
        colour.setARGB(alpha, red, green, blue);
        colour.setStyle(Paint.Style.FILL);
        postInvalidate();
//        invalidate();
    }

    public void setTexture(final int resourceDrawableID, Vector2 textureCoords, Vector2 textureDimen)
    {
        image = BitmapFactory.decodeResource(getResources(), resourceDrawableID);
        sprite = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight());
        float imageUConversion = image.getWidth();
        float imageVConversion = image.getHeight();

        // Use uv coordinates here...
        // Where the image will start from on the spritesheet.
        textureCoordinates = new Vector2((textureCoords.getX() * imageUConversion), (textureCoords.getY() * imageVConversion));

        // Setting the width and height of each sprite.
        // This should be the same for the whole sprite sheet really.
        // Unless there are exceptionally big sprites/characters/things.
        textureDimensions = new Vector2((textureDimen.getX() * imageUConversion), (textureDimen.getY() * imageVConversion));
    }

    public void removeTexture()
    {
        image = BitmapFactory.decodeResource(getResources(), R.drawable.transparent_sprite);
        sprite = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight());

        postInvalidate();
//        invalidate();
    }

    public void changeTexture(Vector2 textureCoords)
    {
        float imageUConversion = image.getWidth();
        float imageVConversion = image.getHeight();

        textureCoordinates.set(textureCoords.getX() * imageUConversion, textureCoords.getY() * imageVConversion);

        // Need to test this...
        postInvalidate();
//        invalidate();
    }

    @Override
    protected void onDraw(final Canvas gameCanvas)
    {
        super.onDraw(gameCanvas);

        // If there is not a sprite image.
        if(sprite == null)
        {
            // Set a standard rectangle up.
            test.set((int)getSpriteLeft(), (int)getSpriteTop(), (int)getSpriteRight(), (int)getSpriteBottom());

            // Draw the rectangle with a colour.
            gameCanvas.drawRect(test, colour);
        }
        // Otherwise, there is a sprite image.
        else
        {
            // Set up the initial sprite of the image.
            // This is the initial source of the sprite, use uv coordinates for dimensions.
            test.set((int)textureCoordinates.getX(), (int)textureCoordinates.getY(), (int)textureCoordinates.getX() + (int)textureDimensions.getX(), (int)textureCoordinates.getY() + (int)textureDimensions.getY());

            // Use destRect for animations.
            destRect.set((int)getSpriteLeft(), (int)getSpriteTop(), (int)getSpriteRight(), (int)getSpriteBottom());
            gameCanvas.rotate(angle, getSpriteCenter().getX(), getSpriteCenter().getY());
            gameCanvas.drawBitmap(sprite, test, destRect, null);
        }
    }

    public float getSpriteLeft()            { return position.getX(); }
    public float getSpriteTop()             { return position.getY(); }
    public float getSpriteRight()           { return (position.getX() + dimension.getX()); }
    public float getSpriteBottom()          { return (position.getY() + dimension.getY()); }
    public Vector2 getSpriteCenter()        { return (new Vector2(position.getX() + (dimension.getX() * 0.5f), position.getY() + (dimension.getY() * 0.5f))); }
    public Vector2 getSpritePosition()      { return position; }
    public Vector2 getSpriteDimensions()    { return dimension; }
    public float getSpriteWidth()           { return dimension.getX(); }
    public float getSpriteHeight()          { return dimension.getY(); }
    public float getSpriteRotationDegrees() { return angle; }
    public float getSpriteRotationRadians() { return (angle * ((float) Math.PI / 180.0f)); }
    public Vector2 getTextureCoordinates()  { return textureCoordinates; }
    public Vector2 getTextureDimensions()   { return textureDimensions; }
}

//// OpenGL extensions.
//class MyGLSurfaceView extends GLSurfaceView
//{
//    //private final MyGLSurfaceView mRenderer;
//
//    public MyGLSurfaceView(Context context, MyGLSurfaceView renderer)
//    {
//        super(context);
//
//        // Creating OpenGL ES 2.0 context.
//        setEGLContextClientVersion(2);
//
//        renderer = new MyGLRenderer();
//
//        // Set the renderer for drawing on the GLSurfaceView.
//        setRenderer(renderer);
//
//        // Render the view when there is a change in the drawing data.
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//    }
//}

