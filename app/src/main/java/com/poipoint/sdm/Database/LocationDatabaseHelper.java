package com.poipoint.sdm.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.poipoint.sdm.Database.DbSchema.*;
import com.poipoint.sdm.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LocationDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String DATABASE_NAME="location_db";
    private Context mContext;
    private final String TAG="db_helper";
    public  LocationDatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MainCategoryTable.NAME + "("
                        + MainCategoryTable.Cols.ID + " int(10) NOT NULL PRIMARY KEY,"
                        + MainCategoryTable.Cols.PL_NAME + " varchar(100) NOT NULL,"
                        + MainCategoryTable.Cols.UK_NAME + " varchar(100) NOT NULL,"
                        + MainCategoryTable.Cols.DE_NAME + " varchar(100) NOT NULL,"
                        + MainCategoryTable.Cols.ES_NAME + " varchar(100) NOT NULL,"
                        + MainCategoryTable.Cols.ORDER_ID + " int(10) NOT NULL,"
                        + MainCategoryTable.Cols.ICON + " varchar(150) NOT NULL);"
        );
        InputStream insertsStream = mContext.getResources().openRawResource(R.raw.cat_val);
        insertRows(db, insertsStream, MainCategoryTable.NAME);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SubCategoryTable.NAME + "("
                        + SubCategoryTable.Cols.ID + " int(10) NOT NULL PRIMARY KEY,"
                        + SubCategoryTable.Cols.CAT_ID + " int(10) NOT NULL,"
                        + SubCategoryTable.Cols.PL_NAME + " varchar(100) NOT NULL,"
                        + SubCategoryTable.Cols.UK_NAME + " varchar(100) NOT NULL,"
                        + SubCategoryTable.Cols.DE_NAME + " varchar(100) NOT NULL,"
                        + SubCategoryTable.Cols.ES_NAME + " varchar(100) NOT NULL,"
                        + SubCategoryTable.Cols.ICON + " varchar(150) NOT NULL,"
                        + SubCategoryTable.Cols.ORDER_ID + " int(10) NOT NULL);"
        );
        InputStream subCatStream=mContext.getResources().openRawResource(R.raw.sub_cat_val);
        insertRows(db,subCatStream,SubCategoryTable.NAME);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + LocationDetailTable.NAME + "("
                        + LocationDetailTable.Cols.ID + " int(10) NOT NULL PRIMARY KEY,"
                        + LocationDetailTable.Cols.CITY + " varchar(50) NOT NULL,"
                        + LocationDetailTable.Cols.STREET + " varchar(50) NOT NULL,"
                        + LocationDetailTable.Cols.DESCRIPTION + " varchar(1000) NOT NULL,"
                        + LocationDetailTable.Cols.LONGITUDE + " varchar(20) NOT NULL,"
                        + LocationDetailTable.Cols.LATITUDE + " varchar(20) NOT NULL,"
                        + LocationDetailTable.Cols.DATE + " varchar(20) NOT NULL,"
                        + LocationDetailTable.Cols.CHECKED + " int(2) NOT NULL,"
                        + LocationDetailTable.Cols.POSTAL_CODE + " varchar(20) NOT NULL,"
                        + LocationDetailTable.Cols.COUNTRY + " varchar(20) NOT NULL,"
                        + LocationDetailTable.Cols.SUBCAT_ID + " varchar(20) NOT NULL,"
                        + LocationDetailTable.Cols.USER_ID + " varchar(20) NOT NULL);"
        );

        InputStream locationDeltailStream=mContext.getResources().openRawResource(R.raw.location_detail_val);
        insertRows(db, locationDeltailStream, LocationDetailTable.NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertRows(SQLiteDatabase db, InputStream insertsStream, String tableName)
    {
        int result = 0;
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
        try {
            while (insertReader.ready()) {
                String line=insertReader.readLine();
                String insertStmt ="INSERT INTO "+tableName+" VALUES"+line;
                db.execSQL(insertStmt);
                result++;
            }
            insertReader.close();
        }
        catch (IOException ioe)
        {
            System.out.println(ioe);
        }
        Log.d(TAG, result + " rows inserted");
    }
}
