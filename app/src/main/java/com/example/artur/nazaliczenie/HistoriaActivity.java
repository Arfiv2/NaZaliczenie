package com.example.artur.nazaliczenie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HistoriaActivity extends Activity
{
    Context context;
    String[] fromFiledNames;
    Intent intent;
    BazaActivity myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);
        myDb = new BazaActivity(this);

        FullScreencall();
        populateListViewFromDB();
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

    public void populateListViewFromDB()
    {
        Cursor cursor = myDb.getAllData();
        fromFiledNames = new String[] {BazaActivity.COL_1, BazaActivity. COL_2, BazaActivity.COL_3};
        final int[] toViewIDs = new int[] {R.id.t_id, R.id.t_date, R.id.t_time};
        final SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.activity_historia_row, cursor, fromFiledNames, toViewIDs, 0);
        final ListView myList = (ListView) findViewById(R.id.listViewFromDB);
        myList.setAdapter(myCursorAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id)
            {
                context = getApplicationContext();
                intent = new Intent(context, WyswietlanieActivity.class);
                intent.putExtra("numer", String.valueOf(id));
                startActivity(intent);
            }
        });
    }

    public void back(View view)
    {
        context = getApplicationContext();
        intent = new Intent(context,MainActivity.class);
        startActivity(intent);
    }
}