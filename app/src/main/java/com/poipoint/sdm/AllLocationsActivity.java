package com.poipoint.sdm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.poipoint.sdm.CustomTextView.BookTextView;
import com.poipoint.sdm.CustomTextView.HeavyTextView;
import com.poipoint.sdm.CustomTextView.MediumTextView;
import com.poipoint.sdm.Database.CategoryCursorWrapper;
import com.poipoint.sdm.Database.DbSchema;
import com.poipoint.sdm.Database.LocationDatabaseHelper;
import com.poipoint.sdm.Database.LocationsCursorWrapper;
import com.poipoint.sdm.Database.SubCategoryCursorWrapper;
import com.poipoint.sdm.Models.LanguageList;
import com.poipoint.sdm.Models.LocationItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Tanmay on 6/13/2016.
 */
public class AllLocationsActivity extends AppCompatActivity {
    private static final String EXTRA_SUBCAT_ID="extra_subcat_id";
    private int subCat_id;
    private static final int REQUEST_ERROR=0;
    private GoogleApiClient mClient;
    private LocationRequest locationRequest;
    private RecyclerView mRecyclerView;
    Location mLocation;
    Boolean cancelSelected=false;
    Float mBearing;
    LocationExpandableAdapter mLocationExpandableAdapter;
    List<LocationItem> locationItemList=new ArrayList<LocationItem>(0);
    List<LocationParentModel> parentModels=new ArrayList<LocationParentModel>(0);
    SQLiteDatabase sqLiteDatabase;
    private float mAzimuth = 0; // degree
    private SensorManager mSensorManager = null;
    private boolean isSensorPresent=false;
    private boolean isSorted=false;
    private Sensor mRotationVectorSensor;
    private final String PREF_LANG="lang_pref_key";



    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        float[] orientation = new float[3];
        float[] rMat = new float[9];

        public void onAccuracyChanged( Sensor sensor, int accuracy ) {}

        @Override
        public void onSensorChanged( SensorEvent event ) {
            if( event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR )
            {
                // calculate th rotation matrix
                SensorManager.getRotationMatrixFromVector( rMat, event.values );
                // get the azimuth value (orientation[0]) in degree
                float tempAzimuth=(int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;
                if(tempAzimuth-mAzimuth>5 ||tempAzimuth-mAzimuth<-5)
                {
                    mAzimuth = tempAzimuth;
                    mLocationExpandableAdapter.notifyDataSetChanged();
                }
                Log.d("Voldemort", "new Azimuth is---> " + mAzimuth);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        sqLiteDatabase=new LocationDatabaseHelper(this).getWritableDatabase();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null){
            isSensorPresent=true;
            mRotationVectorSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        mClient=new GoogleApiClient.Builder(getBaseContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getMyLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
        setupActionBar();
        mRecyclerView=(RecyclerView)findViewById(R.id.category_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadLocationsInParentArray();
        mLocationExpandableAdapter=new LocationExpandableAdapter(parentModels,this);
        mLocationExpandableAdapter.onRestoreInstanceState(savedInstanceState);
        mRecyclerView.setAdapter(mLocationExpandableAdapter);
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
    }

    private void getMyLocation()
    {
        locationRequest=LocationRequest.create();
        locationRequest.setFastestInterval(0);
        locationRequest.setInterval(0).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setSmallestDisplacement(1);
        try
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("Voldemort", "Location Recived--->" + location);
                    mLocation=location;
                    if(mLocation!=null && isSorted==false)
                    {
                        isSorted=true;
                        Collections.sort(parentModels);
                        mLocationExpandableAdapter=new LocationExpandableAdapter(parentModels,getBaseContext());
                        mRecyclerView.setAdapter(mLocationExpandableAdapter);
                        mLocationExpandableAdapter.notifyDataSetChanged();
                    }
                    if(mLocationExpandableAdapter!=null)
                    {
                        mLocationExpandableAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        catch (SecurityException se)
        {
            Log.d("Voldemort",se.getMessage());
        }

    }
    private void loadLocationsInParentArray()
    {
        loadLocationsFromDb();
        for(LocationItem li: locationItemList)
        {
            parentModels.add(new LocationParentModel(li.getDescription(),li.getLatitude(),li.getLongitude(),li.getCity(),li.getStreet(),li.getSubCatId()));
        }
    }

    private void loadLocationsFromDb()
    {
        Cursor cursor=sqLiteDatabase.query(
                DbSchema.LocationDetailTable.NAME,
                new String[]{DbSchema.LocationDetailTable.Cols.ID, DbSchema.LocationDetailTable.Cols.CITY, DbSchema.LocationDetailTable.Cols.STREET, DbSchema.LocationDetailTable.Cols.LATITUDE, DbSchema.LocationDetailTable.Cols.LONGITUDE, DbSchema.LocationDetailTable.Cols.DESCRIPTION, DbSchema.LocationDetailTable.Cols.SUBCAT_ID},
                null,null,null,null,null,null);
        LocationsCursorWrapper locationsCursorWrapper= new LocationsCursorWrapper(cursor);

        try
        {
            locationsCursorWrapper.moveToFirst();
            int counter=0;
            while (!locationsCursorWrapper.isAfterLast()&&counter<3000)
            {
                counter++;
                locationItemList.add(locationsCursorWrapper.getLocationItemFromCursor());
                locationsCursorWrapper.moveToNext();
            }
        }
        finally
        {
            locationsCursorWrapper.close();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view=getLayoutInflater().inflate(R.layout.action_bar_title,null);
        HeavyTextView title=(HeavyTextView)view.findViewById(R.id.mytext);
        title.setText("Locations");
        actionBar.setCustomView(view);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((LocationExpandableAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);
    }
    class LocationParentViewHolder extends ParentViewHolder
    {
        public com.poipoint.sdm.CustomTextView.MediumTextView textView;
        ImageView arrowImageView;
        TextView displacement;
        public LocationParentViewHolder(View itemView) {
            super(itemView);
            textView=(MediumTextView)itemView.findViewById(R.id.location_text_view);
            arrowImageView=(ImageView)itemView.findViewById(R.id.pointer);
            displacement=(TextView)itemView.findViewById(R.id.displacement);
        }
    }

    class LocationChildViewHolder extends ChildViewHolder
    {
        BookTextView textView;
        double lat;
        double lon;
        Button showOnMapButton;
        public LocationChildViewHolder(View itemView) {
            super(itemView);
            textView=(BookTextView)itemView.findViewById(R.id.description_text_view);
            showOnMapButton=(Button)itemView.findViewById(R.id.show_on_map);
            showOnMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?&daddr="+lat+","+lon);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                    else
                    {
                        showShortToast("Install google maps!");
                    }
                }
            });
        }
    }

    private String getBreadCrumbsFromSubCatId(String subCatId)
    {   String breadCrumb="";
        String languageColumn="";
        String language= PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(PREF_LANG, null);
        String catId="";
        if(language==null)
        {
            languageColumn= DbSchema.SubCategoryTable.Cols.PL_NAME;
        }
        if(language.equals(LanguageList.getPolishString()))
        {
            languageColumn= DbSchema.SubCategoryTable.Cols.PL_NAME;
        }
        else if(language.equals(LanguageList.getEnglishString()))
        {
            languageColumn=DbSchema.SubCategoryTable.Cols.UK_NAME;
        }
        else if(language.equals(LanguageList.getGermanString()))
        {
            languageColumn= DbSchema.SubCategoryTable.Cols.DE_NAME;
        }
        else if(language.equals(LanguageList.getSpanishString()))
        {
            languageColumn= DbSchema.SubCategoryTable.Cols.ES_NAME;
        }
        Cursor cursor=sqLiteDatabase.query(
                DbSchema.SubCategoryTable.NAME,
                new String[]{DbSchema.SubCategoryTable.Cols.ID, languageColumn, DbSchema.SubCategoryTable.Cols.CAT_ID},
                DbSchema.SubCategoryTable.Cols.ID+" =?",new String[]{subCatId},null,null,null,null);
        SubCategoryCursorWrapper subCategoryCursorWrapper=new SubCategoryCursorWrapper(cursor);
        try
        {
            if(subCategoryCursorWrapper.moveToFirst())
            {
                String[] result = subCategoryCursorWrapper.getSubCateNameAndCatId(language);
                breadCrumb = breadCrumb + result[0];

                Cursor catCursor = sqLiteDatabase.query(
                        DbSchema.MainCategoryTable.NAME,
                        new String[]{languageColumn},
                        DbSchema.MainCategoryTable.Cols.ID + "=?"
                        , new String[]{result[1]}, null, null, null
                );
                CategoryCursorWrapper categoryCursorWrapper = new CategoryCursorWrapper(catCursor);
                try {

                    if(categoryCursorWrapper.moveToFirst())
                    {
                        breadCrumb = categoryCursorWrapper.getNameFromCategory(language).concat("> "+breadCrumb);
                    }

                } finally {
                    categoryCursorWrapper.close();
                }
            }
        }
        finally
        {
            subCategoryCursorWrapper.close();
        }

        return breadCrumb;
    }
    public class LocationParentModel implements ParentListItem,Comparable<LocationParentModel>
    {
        private String address;
        private String breadCrumbds;
        private Location myLocation=new Location("abc");
        private List<LocationChildModel> mChildrenList=new ArrayList<LocationChildModel>();
        //@Override
        public LocationParentModel(String desc,Double lat,Double lon,String city,String street,String subCatId)
        {
            address=street+", "+city;
            myLocation.setLatitude(lat);
            myLocation.setLongitude(lon);
            mChildrenList.add(new LocationChildModel(desc, lat, lon));
            breadCrumbds=getBreadCrumbsFromSubCatId(subCatId);
            address=breadCrumbds.concat(breadCrumbds.isEmpty()?""+address:"> "+address);
        }

        public String getAddress() {
            return address;
        }

        public String getBreadCrumbds() {
            return breadCrumbds;
        }

        @Override
        public List<?> getChildItemList() {
            return mChildrenList;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }

        @Override
        public int compareTo(LocationParentModel another) {
            if(mLocation!=null)
            {
                double lhsDistance=this.myLocation.distanceTo(mLocation);
                double rhsDistance=another.myLocation.distanceTo(mLocation);
                logMe("lhsdis--->"+lhsDistance+" rhs dis--->"+rhsDistance);

                if (lhsDistance > rhsDistance)
                {
                    logMe("about to return 5 for -->"+"lhsdis--->"+lhsDistance+" rhs dis--->"+rhsDistance);
                    return 5;
                }
                if(rhsDistance>lhsDistance)
                {
                    logMe("about to return -53 for -->"+"lhsdis--->"+lhsDistance+" rhs dis--->"+rhsDistance);
                    return -53;
                }
            }
            logMe("about to return 0");
            return 0;
        }
    }

    class LocationChildModel
    {
        private String Description;
        private Double lat;
        private Double lon;

        public LocationChildModel(String description, Double lat, Double lon) {
            Description = description;
            this.lat = lat;
            this.lon = lon;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public String getDescription() {
            return Description;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.locations_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh_button_menu_id:
                mLocationExpandableAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class LocationExpandableAdapter extends ExpandableRecyclerAdapter<LocationParentViewHolder, LocationChildViewHolder>
    {

        Context mcontext;
        LayoutInflater mInflater;

        public LocationExpandableAdapter(List<LocationParentModel> parentItemList, Context mcontext) {
            super(parentItemList);
            this.mcontext = mcontext;
            this.mInflater = LayoutInflater.from(mcontext);
        }

        @Override
        public LocationParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
            View view=mInflater.inflate(R.layout.list_item_parent,viewGroup,false);
            return new LocationParentViewHolder(view);
        }

        @Override
        public LocationChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
            View view=mInflater.inflate(R.layout.list_item_child, viewGroup, false);
            return new LocationChildViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(LocationParentViewHolder locationParentViewHolder, int i, ParentListItem parentListItem)
        {
            LocationParentModel locationParentModel=(LocationParentModel)parentListItem;
            locationParentViewHolder.textView.setText(locationParentModel.getAddress());
            if(mLocation!=null)
            {
                Float bearingToValue=mLocation.bearingTo(locationParentModel.myLocation);
                locationParentViewHolder.displacement.setText(new Integer((int)mLocation.distanceTo(locationParentModel.myLocation)).toString()+"m");
                Log.d("Voldemort","BearingTo value is--->"+bearingToValue.toString());
                Float angle=bearingToValue-mAzimuth;
                if(mLocation!=null) {
                    GeomagneticField geoField = new GeomagneticField(
                            (float) mLocation.getLatitude(),
                            (float) mLocation.getLongitude(),
                            (float) mLocation.getAltitude(),
                            System.currentTimeMillis());
                    float dec=geoField.getDeclination();
                    angle=angle+dec;
                    Log.d("voldemort", "adjusted with declation of-->"+dec);
                }
                if(angle>=0)
                {
                    Picasso.with(getBaseContext())
                            .load(R.drawable.pointer)
                            .rotate(angle)
                            .into(locationParentViewHolder.arrowImageView);
                }
                else
                {
                    Picasso.with(getBaseContext())
                            .load(R.drawable.pointer)
                            .rotate(360.0f+angle)
                            .into(locationParentViewHolder.arrowImageView);
                }
            }
            else
            {Log.d("Voldemort","mLocation is NUll");}
        }

        @Override
        public void onBindChildViewHolder(LocationChildViewHolder locationChildViewHolder, int i, Object o)
        {
            LocationChildModel locationChildModel=(LocationChildModel)o;
            locationChildViewHolder.textView.setText(locationChildModel.getDescription());
            locationChildViewHolder.lat=locationChildModel.getLat();
            locationChildViewHolder.lon=locationChildModel.getLon();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mClient.disconnect();
        locationRequest.setExpirationDuration(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        int errorCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        {
            if(errorCode!= ConnectionResult.SUCCESS)
            {
                Dialog error=GooglePlayServicesUtil.getErrorDialog(errorCode, this, REQUEST_ERROR, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                error.show();
            }
        }

        if (!((LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            if(!cancelSelected)
            {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.location_dialog);
                dialog.setTitle("");
                Button okayButton=(Button)dialog.findViewById(R.id.dialog_okay_button);
                Button cancelButton=(Button)dialog.findViewById(R.id.dialog_cancel_button);
                dialog.show();

                okayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelSelected=true;
                        dialog.dismiss();
                    }
                });
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    public static Intent makeIntent(Context context,int subCat_id)
    {
        Intent intent=new Intent(context,LocationsActivity.class);
        intent.putExtra(EXTRA_SUBCAT_ID, subCat_id);
        return intent;
    }
    private void showShortToast(String message)
    {
        Toast.makeText(AllLocationsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void logMe(String message)
    {
        Log.d("logAlllocationsActivity",message);
    }
}


