package com.poipoint.sdm.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poipoint.sdm.Models.LanguageList;
import com.poipoint.sdm.Models.SubCategoryItem;

/**
 * Created by Tanmay on 5/29/2016.
 */
public class SubCategoryCursorWrapper extends CursorWrapper{

    public SubCategoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public SubCategoryItem getSubCategoryItem(String language)
    {
        String languageColumn="";
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

        int ID=getInt(getColumnIndex(DbSchema.SubCategoryTable.Cols.ID));
        String name=getString(getColumnIndex(languageColumn));
        int order_id=getInt(getColumnIndex(DbSchema.SubCategoryTable.Cols.ORDER_ID));
        String icon=getString(getColumnIndex(DbSchema.SubCategoryTable.Cols.ICON));

        return new SubCategoryItem(ID,name,order_id,icon);
    }

    public String[] getSubCateNameAndCatId(String language)
    {

        String languageColumn="";
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
        String [] resultArray=new String[2];
        resultArray[0]=getString(getColumnIndex(languageColumn));
        resultArray[1]=new Integer(getInt(getColumnIndex(DbSchema.SubCategoryTable.Cols.CAT_ID))).toString();

        return resultArray;
    }
}
