package com.bitvault.appstore.webservice.response;

import java.util.List;

/**
 * Created Dheeraj Bansal root on 3/5/17.
 * version 1.0.0
 * search response result response
 */

public class SearchResponse {

    private List<AppList> result;

    public List<AppList> getAppList() {
        return result;
    }


//    private List<appList> result;
//
//    public List<SearchResponse.appList> getAppList() {
//        return result;
//    }

    public static class appList {

        private String applicationId;
        private String appName;
        private String packageName;
        private String appIconUrl;
        private String averageRating;
        private String appPrice;

        /**
         * constructer
         * @param appName
         */
        public appList(String appName) {
            this.appName = appName;
        }

        public String getApplicationId() {
            return applicationId;
        }

        public String getAppName() {
            return appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getAppIconUrl() {
            return appIconUrl;
        }

        public String getAverageRating() {
            return averageRating;
        }

        public String getAppPrice() {
            return appPrice;
        }

    }

}
