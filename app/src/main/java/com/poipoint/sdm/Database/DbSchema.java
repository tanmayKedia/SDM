package com.poipoint.sdm.Database;


public class DbSchema {

    public static final class MainCategoryTable
    {
        public static final String NAME="poi_1_sdm";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String PL_NAME="pl_name";
            public static final String UK_NAME="uk_name";
            public static final String DE_NAME="de_name";
            public static final String ES_NAME="es_name";
            public static final String ORDER_ID="order_id";
            public static final String ICON="icon";
        }
    }

    public static final class SubCategoryTable
    {
        public static final String NAME="poi_2_sdm";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String CAT_ID="cat1id";
            public static final String PL_NAME="pl_name";
            public static final String UK_NAME="uk_name";
            public static final String DE_NAME="de_name";
            public static final String ES_NAME="es_name";
            public static final String ICON="icon";
            public static final String ORDER_ID="order_id";
        }
    }

    public static final class LocationDetailTable
    {
        public static final String NAME="baza_sdm";

        public static final class Cols
        {
            public static final String ID="id";
            public static final String CITY="miasto";
            public static final String STREET="ad_ulica";
            public static final String DESCRIPTION="ad_opis";
            public static final String LONGITUDE="ad_long";
            public static final String LATITUDE="ad_lat";
            public static final String DATE="date";
            public static final String CHECKED="checked";
            public static final String POSTAL_CODE="kod";
            public static final String COUNTRY="kraj";
            public static final String SUBCAT_ID="catid";
            public static final String USER_ID="userid";
        }
    }

}
