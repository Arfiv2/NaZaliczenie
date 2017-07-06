package com.example.artur.nazaliczenie;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;

    Context context;
    Intent intent;

    List<Double> dlugosc = new ArrayList<Double>();
    List<Double> szerokosc = new ArrayList<Double>();
    double odleglosc = 0;
    float[] result = new float[1];
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    long millis;
    int zamiana, seconds, minutes, numer = 0;
    String przekaz,przekaz1,przekaz2;

    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");

    TextView timerTextView;
    TextView kmTextView;
    long startTime = 0;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;

            if(seconds%2==0)
            {
                jakasFunkcja();
            }

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    public void kilometry(int kolejnosc)
    {
        if(dlugosc.size() > 1)
        {
            Location.distanceBetween(
                    szerokosc.get(kolejnosc-1),dlugosc.get(kolejnosc-1),
                    szerokosc.get(kolejnosc), dlugosc.get(kolejnosc), result);
            odleglosc = odleglosc + result[0];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        kmTextView = (TextView) findViewById(R.id.kmTextView);

        Button b = (Button) findViewById(R.id.button1);
        b.setText("Rozpocznij trening");
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("Zakończ trening"))
                {
                    timerHandler.removeCallbacks(timerRunnable);
                    odleglosc = Math.round(odleglosc);
                    zamiana = (int)odleglosc;
                    przekaz = String.valueOf(zamiana);
                    przekaz1 = String.valueOf(minutes);
                    przekaz2 = String.valueOf(seconds);

                    context = getApplicationContext();
                    intent = new Intent(context, PoTreninguActivity.class);
                    intent.putExtra("dystans", przekaz);
                    intent.putExtra("minuty", przekaz1);
                    intent.putExtra("sekundy", przekaz2);
                    startActivity(intent);
                }
                else
                {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("Zakończ trening");
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        FullScreencall();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Tutaj"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.2f));
    }

    public void FullScreencall()
    {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void jakasFunkcja()
    {
        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dlugosc.add(longitude);
        szerokosc.add(latitude);
        kilometry(numer);
        numer++;
        kmTextView.setText(REAL_FORMATTER.format(odleglosc));
    }
}
