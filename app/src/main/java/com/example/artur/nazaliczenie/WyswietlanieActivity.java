package com.example.artur.nazaliczenie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class WyswietlanieActivity extends Activity
{
    Context context;
    Intent intent;
    BazaActivity myDb;
    TextView  textView2, kmTextView2, textViewKcal2, textViewData2, textViewCzas2;
    String numer;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyswietlanie);
        myDb = new BazaActivity(this);
        FullScreencall();

        textView2 = (TextView) findViewById(R.id.textView2);
        kmTextView2 = (TextView) findViewById(R.id.kmTextView2);
        textViewKcal2 = (TextView) findViewById(R.id.textViewKcal2);
        textViewData2 = (TextView) findViewById(R.id.textViewData2);
        textViewCzas2 = (TextView) findViewById(R.id.textViewCzas2);
        imageView = (ImageView) findViewById(R.id.imageView);

        numer = getIntent().getStringExtra("numer");

        viewAll();
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

    public void viewAll()
    {
        Cursor res = myDb.getOneData(numer);

        if(res.getCount() == 0)
        {
            return;
        }

        while (res.moveToNext())
        {
            textViewData2.setText(res.getString(1));
            textViewCzas2.setText(res.getString(2));
            textView2.setText(res.getString(3));
            kmTextView2.setText(res.getString(4));
            textViewKcal2.setText(res.getString(5));
            File imagesFolder = new File(res.getString(6));
            Uri uriSavedImage = Uri.fromFile(imagesFolder);
            imageView.setImageURI(uriSavedImage);
        }
    }

    public void back(View view)
    {
        context = getApplicationContext();
        intent = new Intent(context,HistoriaActivity.class);
        startActivity(intent);
    }
}