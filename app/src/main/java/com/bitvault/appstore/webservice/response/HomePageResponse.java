package com.bitvault.appstore.webservice.response;

import java.util.List;

/**
 * Created Dheeraj Bansal root on 2/5/17.
 * version 1.0.0
 * Dashboard response like banner, apps category wise
 */

public class HomePageResponse {
    private List<banner> banner;
    private List<category> category;
    private List<appList> appList;

    public List<HomePageResponse.banner> getBanner() {
        return banner;
    }

    public List<HomePageResponse.category> getCategory() {
        return category;
    }

    public List<HomePageResponse.appList> getAppList() {
        return appList;
    }

    public static class banner {
        private int banner_id;
        private String banner_url;
        private String package_name;

        public int getBanner_id() {
            return banner_id;
        }

        public String getBanner_url() {
            return banner_url;
        }

        public String getPackage_name() {
            return package_name;
        }
    }

    public static class category {
        private int id;
        private String name;
        private String category_icon_url;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCategory_icon_url() {
            return category_icon_url;
        }

    }

    public static class appList {
        private String package_name;
        private String app_icon_url;
        private String app_name;
        private String price_type;
        private int app_price;
        private int category_id;
        private String category_name;
        private double rating;

        public String getPackage_name() {
            return package_name;
        }

        public String getApp_icon_url() {
            return app_icon_url;
        }

        public String getApp_name() {
            return app_name;
        }

        public String getPrice_type() {
            return price_type;
        }

        public int getApp_price() {
            return app_price;
        }

        public int getCategory_id() {
            return category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public double getRating() {
            return rating;
        }

    }

}
