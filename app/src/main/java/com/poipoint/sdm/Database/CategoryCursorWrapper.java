package com.poipoint.sdm.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poipoint.sdm.Models.CategoryItem;
import com.poipoint.sdm.Models.LanguageList;

/**
 * Created by Tanmay on 5/26/2016.
 */
public class CategoryCursorWrapper extends CursorWrapper {

    public CategoryCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public CategoryItem getCategoryItem(String language)
    {
        String languageColumn="";
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

        int ID=getInt(getColumnIndex(DbSchema.MainCategoryTable.Cols.ID));
        String name=getString(getColumnIndex(languageColumn));
        int order_id=getInt(getColumnIndex(DbSchema.MainCategoryTable.Cols.ORDER_ID));
        String icon=getString(getColumnIndex(DbSchema.MainCategoryTable.Cols.ICON));

        return new CategoryItem(ID,name,order_id,icon);
    }

    public String getNameFromCategory(String language)
    {
        String languageColumn="";
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

        String name=getString(getColumnIndex(languageColumn));
        return name;
    }
}
