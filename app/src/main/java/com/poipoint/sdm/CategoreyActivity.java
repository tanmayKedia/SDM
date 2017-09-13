package com.poipoint.sdm;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.poipoint.sdm.CustomTextView.HeavyTextView;
import com.poipoint.sdm.CustomTextView.MediumTextView;
import com.poipoint.sdm.Database.CategoryCursorWrapper;
import com.poipoint.sdm.Database.DbSchema;
import com.poipoint.sdm.Database.LocationDatabaseHelper;
import com.poipoint.sdm.Models.CategoryItem;
import com.poipoint.sdm.Models.LanguageList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 5/15/2016.
 */
public class CategoreyActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    private final String PREF_LANG="lang_pref_key";
    private final int DIALOG_LANG=001;
    public int radioButtonPosition=-1;
    public RadioButton lastCheckedRadioButton=null;
    private RecyclerView mCategoryRecyclerView;
    CategoryAdapter mCategoryAdapter;
    private List<CategoryItem> itemList=new ArrayList<CategoryItem>(1);
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteDatabase=new LocationDatabaseHelper(this).getWritableDatabase();
        setContentView(R.layout.navigation_drawer_layout);
        setupActionBar();

        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(PREF_LANG,null)==null)
        {
            showLanguageDialog();
        }
        mCategoryRecyclerView =(RecyclerView)findViewById(R.id.category_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadCategories(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(PREF_LANG, null));
        mCategoryAdapter=new CategoryAdapter(itemList);
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryRecyclerView.setVerticalScrollBarEnabled(true);
        mCategoryRecyclerView.setScrollbarFadingEnabled(false);
    }


    private void setupActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view=getLayoutInflater().inflate(R.layout.action_bar_title,null);
        HeavyTextView title=(HeavyTextView)view.findViewById(R.id.mytext);
        title.setText("Categories");
        actionBar.setCustomView(view);
        String[] list = {"Select Language", "Show All Locations", "About us"};
        ListView drawer = (ListView)findViewById(R.id.drawer_listview);
        drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_drawer_list_item, list));
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_menu_white_24dp, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.setDrawerListener(toggle);
        drawer.setOnItemClickListener(new DrawerItemClickListener());
        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_menu_white_24dp);
        menuIcon.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menuIcon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
        private void selectItem(int position) {

            if (position==0)
            {
                drawerLayout.closeDrawer(Gravity.LEFT);
                showLanguageDialog();
            }
          else if(position==1)
            {
                Intent i=new Intent(getBaseContext(),AllLocationsActivity.class);
                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(i);

            }

            else if(position==2)
            {
                Intent i=new Intent(getBaseContext(),AboutActivity.class);
                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(i);
            }


        }
    }

    private void loadCategories(String language)
    {
        if(language==null)
        {
            itemList.clear();
           loadItemListFromCursor(LanguageList.getPolishString(),loadCategoryCursorWrapperFromLanguage(LanguageList.getPolishString()));
        }
        else
        {
            itemList.clear();
            loadItemListFromCursor(language,loadCategoryCursorWrapperFromLanguage(language));
        }
    }

    private void loadItemListFromCursor(String language,CategoryCursorWrapper categoryCursorWrapper)
    {

        try
        {
            categoryCursorWrapper.moveToFirst();
            while (!categoryCursorWrapper.isAfterLast())
            {
                itemList.add(categoryCursorWrapper.getCategoryItem(language));
                categoryCursorWrapper.moveToNext();
            }
        }
        finally
        {
            categoryCursorWrapper.close();
        }

    }

    private CategoryCursorWrapper loadCategoryCursorWrapperFromLanguage(String language)
    {     String languageColumn="";

        if(language.equals(LanguageList.getPolishString()))
        {
            languageColumn= DbSchema.MainCategoryTable.Cols.PL_NAME;
        }
        else if(language.equals(LanguageList.getEnglishString()))
        {
            languageColumn=DbSchema.MainCategoryTable.Cols.UK_NAME;
        }
        else if(language.equals(LanguageList.getGermanString()))
        {
            languageColumn= DbSchema.MainCategoryTable.Cols.DE_NAME;
        }
        else if(language.equals(LanguageList.getSpanishString()))
        {
            languageColumn= DbSchema.MainCategoryTable.Cols.ES_NAME;
        }

        Cursor cursor=sqLiteDatabase.query(
                DbSchema.MainCategoryTable.NAME,
                new String[]{DbSchema.MainCategoryTable.Cols.ID, languageColumn, DbSchema.MainCategoryTable.Cols.ORDER_ID, DbSchema.MainCategoryTable.Cols.ICON},
                null,null,null,null,null,null);
        return new CategoryCursorWrapper(cursor);
    }

    class CategoryAdapter extends  RecyclerView.Adapter<CategoryHolder>
    {   List<CategoryItem> categoryList;
        public CategoryAdapter(List<CategoryItem> categoryList)
        {
            this.categoryList=categoryList;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {

            LayoutInflater layoutInflater=getLayoutInflater();
            View view=layoutInflater.inflate(R.layout.category_list_item,viewGroup,false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryHolder categoryHolder, int i)
        {
            categoryHolder.textView.setText(categoryList.get(i).getName());
            categoryHolder.id=categoryList.get(i).getId();
            Picasso
                    .with(getBaseContext())
                    .load(BaseUrl.baseUrl+categoryList.get(i).getIcon())
                    .placeholder(R.drawable.placeholder_icon)
                    .into(categoryHolder.icon);
        }

        @Override
        public int getItemCount()
        {
            return categoryList.size();
        }
    }
    class CategoryHolder extends RecyclerView.ViewHolder
    {   MediumTextView textView;
        int id;
        ImageView icon;
        public CategoryHolder(View itemView) {
            super(itemView);
            textView=(MediumTextView)itemView.findViewById(R.id.category_list_item_text_view);
            icon=(ImageView)itemView.findViewById(R.id.icon_cat_list_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=SubcategoryActivity.makeIntent(CategoreyActivity.this,id,textView.getText()+"");
                    startActivity(intent);
                }
            });
        }
    }
    private void showLanguageDialog() {
        //Show dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.setTitle("Choose Language");
        dialog.show();
        RecyclerView mRecyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CustomAdapter(new String[]{"PL","UK","DE","ES"}, this));

        Button mDialogOkayButton = (Button) dialog.findViewById(R.id.dialog_okay_button);
        mDialogOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonPosition != -1 && lastCheckedRadioButton != null)
                {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(PREF_LANG, LanguageList.getLanguageArray()[radioButtonPosition]).apply();
                    lastCheckedRadioButton = null;
                    radioButtonPosition = -1;
                    loadCategories(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(PREF_LANG,null));
                    mCategoryAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }
}
