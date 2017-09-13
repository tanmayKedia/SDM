package com.poipoint.sdm.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poipoint.sdm.Models.LocationItem;

/**
 * Created by Tanmay on 5/30/2016.
 */
public class LocationsCursorWrapper extends CursorWrapper {

    public LocationsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LocationItem getLocationItemFromCursor()
    {
        int ID=getInt(getColumnIndex(DbSchema.LocationDetailTable.Cols.ID));
        String city=getString(getColumnIndex(DbSchema.LocationDetailTable.Cols.CITY));
        String  street=getString(getColumnIndex(DbSchema.LocationDetailTable.Cols.STREET));
        double lat=Double.parseDouble(getString(getColumnIndex(DbSchema.LocationDetailTable.Cols.LATITUDE)));
        double lon=Double.parseDouble(getString(getColumnIndex(DbSchema.LocationDetailTable.Cols.LONGITUDE)));
        String desc=getString(getColumnIndex(DbSchema.LocationDetailTable.Cols.DESCRIPTION));
        String subCatId=getString(getColumnIndex(DbSchema.LocationDetailTable.Cols.SUBCAT_ID));
        return new LocationItem(ID,city,street,lat,lon,desc,subCatId);
    }
}
