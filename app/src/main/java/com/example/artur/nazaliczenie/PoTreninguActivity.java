package com.example.artur.nazaliczenie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PoTreninguActivity extends Activity
{
    Context context;
    Intent intent;
    TextView timerTextView, kmTextView, textVievKcal2;
    String mImageFileLocation = "", timeStamp, timeStamp_date, timeStamp_time, imageFileName, dystans, spalanie;
    BazaActivity myDb;
    double odl, kcal, czas, predkosc;
    int min, sek;

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 1;

    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potreningu);
        FullScreencall();

        myDb = new BazaActivity(this);

        kmTextView = (TextView) findViewById(R.id.kmTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        textVievKcal2 = (TextView) findViewById(R.id.textVievKcal2);

        dystans = getIntent().getStringExtra("dystans");
        String minuty = getIntent().getStringExtra("minuty");
        String sekundy = getIntent().getStringExtra("sekundy");

        odl = Double.parseDouble(dystans);
        min = Integer.parseInt(minuty);
        sek = Integer.parseInt(sekundy);

        czas = min+(1/(60/sek));
        odl = odl/1000;
        predkosc = czas / odl;

        if(predkosc >= 8)
        {
            predkosc = 8.1;
        }
        if(predkosc < 8 && predkosc >= 6.5)
        {
            predkosc = 9.9;
        }
        if(predkosc < 6.5 && predkosc > 4)
        {
            predkosc = 12;
        }
        if(predkosc <= 4)
        {
            predkosc = 16;
        }

        if(odl == 0)
        {
            kcal = 0;
        }
        else
        {
            kcal = czas * 0.01666667 * predkosc * 70;
            kcal = Math.round(kcal * 100.0) / 100.0;
        }

        timerTextView.setText(String.format("%d:%02d", min, sek));
        kmTextView.setText(dystans);
        textVievKcal2.setText(REAL_FORMATTER.format(kcal));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        FullScreencall();
    }

    public void FullScreencall()
    {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void takePhoto(View view)
    {
        isDirCreated();
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            callCameraApp();
        }
        else
        {
            requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE_RESULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String [] permissions, int[] grantResults)
    {
        if(requestCode == REQUEST_EXTERNAL_STORAGE_RESULT)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callCameraApp();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void callCameraApp()
    {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try
        {
            photoFile = createImageFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK)
        {
            bazar();
        }
    }

    File createImageFile() throws IOException
    {
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        timeStamp_date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        timeStamp_time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(File.separator + "Zaliczeniowa" + File.separator + "Img");

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }

    void isDirCreated()
    {
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Zaliczeniowa");
        File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + "Zaliczeniowa" + File.separator + "Img");

        if (!f.exists())
        {
            f.mkdirs();
        }
        if (!f2.exists())
        {
            f2.mkdirs();
        }
    }

    public void wyjdz(View view)
    {
        context = getApplicationContext();
        intent = new Intent(context,MainActivity.class);
        startActivity(intent);
    }

    public void bazar()
    {
        spalanie = String.valueOf(kcal);

        boolean insertData = myDb.insertData(timeStamp_date, timeStamp_time, String.format("%d:%02d", min, sek), dystans , spalanie, mImageFileLocation);
        if(insertData)
        {
            Toast.makeText(this, "Dodano do bazy", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Awaria. Nie dodano. Skontaktuj się z autorem", Toast.LENGTH_LONG).show();
        }

        context = getApplicationContext();
        intent = new Intent(context,MainActivity.class);
        startActivity(intent);
    }
}