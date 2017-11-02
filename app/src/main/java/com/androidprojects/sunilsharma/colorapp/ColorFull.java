package com.androidprojects.sunilsharma.colorapp;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by sunil sharma on 11/1/2017.
 */

public class ColorFull
{
    private Bitmap bitmap;
    private float redColorValue;
    private float greenColorValue;
    private float blueColorValue;


    public ColorFull(Bitmap bitmap, float redColorValue, float greenColorValue, float blueColorValue)
    {
        this.bitmap = bitmap;
        setRedColorValue(redColorValue);
        setGreenColorValue(greenColorValue);
        setBlueColorValue(blueColorValue);
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getRedColorValue() {
        return redColorValue;
    }

    public void setRedColorValue(float redColorValue)
    {
        if(redColorValue >= 0 && redColorValue <= 1)
        {
            this.redColorValue = redColorValue;
        }

    }

    public float getGreenColorValue() {
        return greenColorValue;
    }

    public void setGreenColorValue(float greenColorValue)
    {

        if(greenColorValue >= 0 && greenColorValue <= 1)
        {
            this.greenColorValue = greenColorValue;
        }
    }

    public float getBlueColorValue()
    {
        return blueColorValue;
    }

    public void setBlueColorValue(float blueColorValue)
    {
        if(blueColorValue >= 0 && blueColorValue <= 1)
        {
            this.blueColorValue = blueColorValue;
        }
    }

    public Bitmap returnTheColorizedBitmap()
    {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        Bitmap.Config bitmapConfig = bitmap.getConfig();

        Bitmap localBitmap = Bitmap.createBitmap(bitmapWidth , bitmapHeight , bitmapConfig);

        /** We Know Image consists a pixel so have to take a each pixel */
        for(int row = 0 ; row < bitmapWidth ; row++)
        {
            for(int column = 0 ; column < bitmapHeight ; column++)
            {
                /**Here we get the color of each pixel*/
                int pixelColor = bitmap.getPixel(row , column);

                /** now we want to change the color of each pixel*/
                pixelColor = Color.argb(Color.alpha(pixelColor) ,
                        (int) redColorValue * Color.red(pixelColor) ,
                        (int) greenColorValue * Color.green(pixelColor) ,
                        (int) blueColorValue * Color.blue(pixelColor));

                localBitmap.setPixel(row , column , pixelColor);
            }
        }

        return localBitmap;

    }



}
