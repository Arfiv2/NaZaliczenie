package com.example.artur.nazaliczenie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaActivity extends SQLiteOpenHelper
{
    SQLiteDatabase mSqliteDatabase;

    public static final String TABLE_NAME = "trening_table_0";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "DATE2";
    public static final String COL_4 = "TIME";
    public static final String COL_5 = "KM";
    public static final String COL_6 = "KCAL";
    public static final String COL_7 = "PHOTO";


    public BazaActivity(Context context)
    {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_table = "CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT, " + COL_6 + " TEXT, " + COL_7 + " TEXT)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String date, String date_time, String time, String km, String kcal, String photo)
    {
        mSqliteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, date);
        contentValues.put(COL_3, date_time);
        contentValues.put(COL_4, time);
        contentValues.put(COL_5, km);
        contentValues.put(COL_6, kcal);
        contentValues.put(COL_7, photo);

        long result = mSqliteDatabase.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getAllData()
    {
        mSqliteDatabase = this.getWritableDatabase();
        Cursor res = mSqliteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public Cursor getOneData(String numer)
    {
        mSqliteDatabase = this.getWritableDatabase();
        Cursor res = mSqliteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + numer + "'", null);
        return res;
    }
}
