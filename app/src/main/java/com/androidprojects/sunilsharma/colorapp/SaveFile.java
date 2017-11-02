package com.androidprojects.sunilsharma.colorapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by sunil sharma on 11/2/2017.
 */

public class SaveFile
{
    public static File saveFile(Activity myActivity , Bitmap bitmap) throws IOException
    {
        /** First thing that we have to know whether user device have a external Storage ot NOT.*/
        String externalStorageStage = Environment.getExternalStorageState();
        /** Now we have to save file into the Storage*/
        File myFile = null;

        /** If user device have the External Storage than this condition will be execute */
        if(externalStorageStage.equals(Environment.MEDIA_MOUNTED))
        {
            /** Now We have to Create a Directory to hold our image*/
            File pictureDirectory = myActivity.getExternalFilesDir("ColorAppPictures");

            /** now we wnat to save Unique Image each time */
            Date currentDate = new Date();
            long elapsedTime = SystemClock.elapsedRealtime();

            /** Now we have to give unique Image name each time*/
            String uniqueImageName = "/" + currentDate + "_" + elapsedTime + ".png";

            myFile = new File(pictureDirectory + uniqueImageName);

            /** Now we have to save this File into Our Directory
             * For that First We Hav to check Whether there is s Space or NOT in External Storage*/
            long remainingSpace = pictureDirectory.getFreeSpace();

            /** After this we have to find out how much space we need to save our File*/
            long requiredSpace = bitmap.getByteCount();

            if(requiredSpace * 1.8 < remainingSpace)
            {
                try
                {
                    FileOutputStream fileOutputStream = new FileOutputStream(myFile);
                    boolean isImageSaveSuccessfully = bitmap.compress(Bitmap.CompressFormat.PNG ,
                            100 ,
                            fileOutputStream);

                    if(isImageSaveSuccessfully)
                    {
                        return myFile;
                    }
                    else
                    {
                        throw new IOException("The Image is not saved Successfully to External Storage");
                    }
                }
                catch(Exception e)
                {
                    throw new IOException("The Operation of saving the Image into " +
                            "The External Storage Went Wrong");
                }
            }
            else
            {
                throw new IOException("There is no Enough Space in order " +
                        "To save Image to External Storage");
            }
        }
        else
        {
            throw new IOException("This Device Does NOT Have EXTERNAL STORAGE");
        }
    }
}
