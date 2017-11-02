package com.androidprojects.sunilsharma.colorapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button buttonTakeAPicture;
    private Button buttonSavePicture;
    private ImageView imagePhoto;
    private SeekBar redColorSeekBar;
    private SeekBar greenColorSeekBar;
    private SeekBar blueColorSeekBar;
    private TextView textRedColorValue;
    private TextView textGreenColorValue;
    private TextView textBlueColorValue;
    private Button buttonShare;

    private static final int CAMERA_IMAGE_REQUEST_CODE = 1000;
    private Bitmap bitmap;
    private ColorFull colorFull;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTakeAPicture = (Button) findViewById(R.id.buttonTakeAPicture);
        buttonSavePicture = (Button) findViewById(R.id.buttonSavePicture);
        imagePhoto = (ImageView) findViewById(R.id.imagePhoto);
        greenColorSeekBar = (SeekBar) findViewById(R.id.greenColorseekBar);
        redColorSeekBar = (SeekBar) findViewById(R.id.redColorSeekBar);
        blueColorSeekBar = (SeekBar) findViewById(R.id.blueColorseekBar);
        textRedColorValue = (TextView) findViewById(R.id.textRedColorValue);
        textGreenColorValue = (TextView) findViewById(R.id.textGreenColorValue);
        textBlueColorValue = (TextView) findViewById(R.id.textBlueColorValue);
        buttonShare = (Button) findViewById(R.id.buttonShare);

        ColorizedHandler colorizedHandler = new ColorizedHandler();
        redColorSeekBar.setOnSeekBarChangeListener(colorizedHandler);
        greenColorSeekBar.setOnSeekBarChangeListener(colorizedHandler);
        blueColorSeekBar.setOnSeekBarChangeListener(colorizedHandler);


        //Todo : Create a Listener for Button buttonTakeAPicture AND  buttonSavePicture also
        buttonTakeAPicture.setOnClickListener(MainActivity.this);
        buttonSavePicture.setOnClickListener(MainActivity.this);
        buttonShare.setOnClickListener(MainActivity.this);


        buttonSavePicture.setVisibility(View.INVISIBLE);
        redColorSeekBar.setVisibility(View.INVISIBLE);
        greenColorSeekBar.setVisibility(View.INVISIBLE);
        blueColorSeekBar.setVisibility(View.INVISIBLE);
        buttonShare.setVisibility(View.INVISIBLE);
        textRedColorValue.setVisibility(View.INVISIBLE);
        textGreenColorValue.setVisibility(View.INVISIBLE);
        textBlueColorValue.setVisibility(View.INVISIBLE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonTakeAPicture)
        {
            //Todo : Here We Need a USER Permission To Access The Camera.
            int permissionResult = ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.CAMERA);

            /** Here we are checking whether Permission is given to US By the USER*/
            if(permissionResult == PackageManager.PERMISSION_GRANTED)
            {
                /** Now Here we Have to Check Whether USer Device Have a Camera OR NOT*/
                PackageManager packageManager = getPackageManager();
                if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA))
                {
                    /** Now  we have to crete Intent For the Camera.*/
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent , CAMERA_IMAGE_REQUEST_CODE);
                }
                /** Now WHAT If the User Device Does NOT HAVE THE CAMERA.*/
                else
                {
                    Toast.makeText(MainActivity.this ,
                            "Your Device Does NOT HAve The CAMERA" ,
                            Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                /** If User NOT GIVEN The Permission The here we are going to ASK For the Permission*/
                ActivityCompat.requestPermissions(MainActivity.this ,
                        new String[]{Manifest.permission.CAMERA} , 1);
            }
        }
        else if(view.getId() == R.id.buttonSavePicture)
        {
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this ,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if(permissionCheck == PackageManager.PERMISSION_GRANTED)
            {
                try
                {
                    SaveFile.saveFile(MainActivity.this , bitmap);
                    Toast.makeText(MainActivity.this , "The Image is now " +
                            "Successfully Saved to External Storage" , Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                /** If the User dont have the permission then we have to Ask for the permission Here*/
                ActivityCompat.requestPermissions(MainActivity.this ,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} ,
                        2000);
            }
        }
        else if(view.getId() == R.id.buttonShare)
        {
            try
            {
                File myPictureFile = SaveFile.saveFile(MainActivity.this , bitmap);
                Uri myUri = Uri.fromFile(myPictureFile);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT ,
                        "This Picture is Send From ColorApp That I created My Self");
                shareIntent.putExtra(Intent.EXTRA_STREAM , myUri);
                startActivity(Intent.createChooser(shareIntent ,
                        "Let's Share your Picture with Others"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    /** This Method is called after user click a picture and image is put in our ImageView*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Toast.makeText(MainActivity.this ,
                "onActivityResult is Called" ,
                Toast.LENGTH_SHORT).show();
        if(requestCode == CAMERA_IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
        {
            buttonSavePicture.setVisibility(View.VISIBLE);
            redColorSeekBar.setVisibility(View.VISIBLE);
            greenColorSeekBar.setVisibility(View.VISIBLE);
            blueColorSeekBar.setVisibility(View.VISIBLE);
            buttonShare.setVisibility(View.VISIBLE);
            textRedColorValue.setVisibility(View.VISIBLE);
            textGreenColorValue.setVisibility(View.VISIBLE);
            textBlueColorValue.setVisibility(View.VISIBLE);



            /** Now here we have to put a image into a ImageView*/
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            colorFull = new ColorFull(bitmap , 0.0f , 0.0f , 0.0f);
            imagePhoto.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /** Here m Creating a INNER Class*/
    private class ColorizedHandler implements SeekBar.OnSeekBarChangeListener
    {

        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar  The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range min..max where min
         *                 and max were set by {@link ProgressBar#setMin(int)} and
         *                 {@link ProgressBar#setMax(int)}, respectively. (The default values for
         *                 min is 0 and max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            if(fromUser)
            {
                if(seekBar == redColorSeekBar)
                {
                    colorFull.setRedColorValue(progress / 100);
                    redColorSeekBar.setProgress((int)(100 * (colorFull.getRedColorValue())));
                    textRedColorValue.setText(colorFull.getRedColorValue() + "");
                }
                else if(seekBar == greenColorSeekBar)
                {
                    colorFull.setGreenColorValue(progress / 100);
                    greenColorSeekBar.setProgress((int) (100 * (colorFull.getGreenColorValue())));
                    textGreenColorValue.setText(colorFull.getGreenColorValue() + "");
                }
                else if(seekBar == blueColorSeekBar)
                {
                    colorFull.setBlueColorValue(progress / 100);
                    blueColorSeekBar.setProgress((int) (100 * (colorFull.getBlueColorValue())));
                    textBlueColorValue.setText(colorFull.getBlueColorValue() + "");
                }

                bitmap = colorFull.returnTheColorizedBitmap();
                imagePhoto.setImageBitmap(bitmap);
            }
        }

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {

        }

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {

        }
    }
}
