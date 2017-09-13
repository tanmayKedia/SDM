package com.poipoint.sdm.Models;

/**
 * Created by Tanmay on 5/16/2016.
 */
public class LanguageList {
    //If changing order of languages reflect the changes in the get<Language>String methods of this class

    //Spelling changes don't require any change

    //if adding a new language take care of the order and add relevent code through out the project
    private static String[] languageArray={"PL","UK","DE","ES"};

    public static String[] getLanguageArray() {
        return languageArray;
    }

    public static String getPolishString()
    {
        return languageArray[0];
    }

    public static String getEnglishString()
    {
        return languageArray[1];
    }

    public static String getGermanString()
    {
        return languageArray[2];
    }

    public static String getSpanishString()
    {
        return languageArray[3];
    }
}
