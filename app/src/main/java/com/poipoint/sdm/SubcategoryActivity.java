package com.poipoint.sdm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.poipoint.sdm.CustomTextView.HeavyTextView;
import com.poipoint.sdm.CustomTextView.MediumTextView;
import com.poipoint.sdm.Database.DbSchema;
import com.poipoint.sdm.Database.LocationDatabaseHelper;
import com.poipoint.sdm.Database.SubCategoryCursorWrapper;
import com.poipoint.sdm.Models.LanguageList;
import com.poipoint.sdm.Models.SubCategoryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Tanmay on 5/18/2016.
 */
public class SubcategoryActivity extends AppCompatActivity{

    private static final String EXTRA_CATEGORY_ID="categor_id_extra";
    private static final String EXTRA_CAT_NAME="cat_name_extra";
    private RecyclerView mRecyclerView;
    private int mCategoryId;
    private String mCategoryName;
    private final String PREF_LANG="lang_pref_key";
    private ArrayList<SubCategoryItem> itemList=new ArrayList<SubCategoryItem>();
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        sqLiteDatabase=new LocationDatabaseHelper(this).getWritableDatabase();
        mCategoryId=getIntent().getIntExtra(EXTRA_CATEGORY_ID,0);
        mCategoryName=getIntent().getStringExtra(EXTRA_CAT_NAME);
        setupActionBar();
        loadSubCategories(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(PREF_LANG, null));
        mRecyclerView=(RecyclerView)findViewById(R.id.category_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SubCategoryAdapter(itemList));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);


    }

    private void setupActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view=getLayoutInflater().inflate(R.layout.action_bar_title,null);
        HeavyTextView title=(HeavyTextView)view.findViewById(R.id.mytext);
        title.setText(mCategoryName);
        actionBar.setCustomView(view);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadSubCategories(String language)
    {
        if(language==null)
        {
            itemList.clear();
            loadItemListFromCursor(LanguageList.getPolishString(),loadSubCategoryCursorWrapperFromLanguage(LanguageList.getPolishString()));
        }
        else
        {
            itemList.clear();
            loadItemListFromCursor(language,loadSubCategoryCursorWrapperFromLanguage(language));
        }
    }

    private void loadItemListFromCursor(String language,SubCategoryCursorWrapper subCategoryCursorWrapper)
    {

        try
        {
            subCategoryCursorWrapper.moveToFirst();
            while (!subCategoryCursorWrapper.isAfterLast())
            {
                itemList.add(subCategoryCursorWrapper.getSubCategoryItem(language));
                subCategoryCursorWrapper.moveToNext();
            }
        }
        finally
        {
            subCategoryCursorWrapper.close();
        }

    }

    private SubCategoryCursorWrapper loadSubCategoryCursorWrapperFromLanguage(String language)
    {     String languageColumn="";

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
                new String[]{DbSchema.SubCategoryTable.Cols.ID, languageColumn, DbSchema.SubCategoryTable.Cols.ORDER_ID, DbSchema.MainCategoryTable.Cols.ICON},
                DbSchema.SubCategoryTable.Cols.CAT_ID+" =?",new String[]{new Integer(mCategoryId).toString()},null,null,null,null);
        return new SubCategoryCursorWrapper(cursor);
    }
    public static Intent makeIntent(Context context,int id,String catName)
    {
        Intent intent=new Intent(context,SubcategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID,id);
        intent.putExtra(EXTRA_CAT_NAME,catName);
        return intent;
    }
    class SubCategoryAdapter extends  RecyclerView.Adapter<SubCategoryHolder>
    {   ArrayList<SubCategoryItem> categoryList;
        public SubCategoryAdapter(ArrayList<SubCategoryItem> categoryList)
        {

            this.categoryList=categoryList;
        }

        @Override
        public SubCategoryHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            LayoutInflater layoutInflater=getLayoutInflater();
            View view=layoutInflater.inflate(R.layout.category_list_item,viewGroup,false);
            return new SubCategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(SubCategoryHolder categoryHolder, int i) {
            categoryHolder.textView.setText(categoryList.get(i).getName());
            categoryHolder.id=categoryList.get(i).getId();
            Picasso.with(getBaseContext())
                    .load(BaseUrl.baseUrl+categoryList.get(i).getIcon())
                    .placeholder(R.drawable.placeholder_icon)
                    .into(categoryHolder.icon);
    }

        @Override
        public int getItemCount() {
        return categoryList.size();
    }
    }
    class SubCategoryHolder extends RecyclerView.ViewHolder
    {   MediumTextView textView;
        int id;
        ImageView icon;
        public SubCategoryHolder(View itemView)
        {
            super(itemView);
            textView=(MediumTextView)itemView.findViewById(R.id.category_list_item_text_view);
            icon=(ImageView)itemView.findViewById(R.id.icon_cat_list_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=LocationsActivity.makeIntent(SubcategoryActivity.this,id,mCategoryName+">"+textView.getText());
                    startActivity(intent);
                }
            });
        }
    }
}
