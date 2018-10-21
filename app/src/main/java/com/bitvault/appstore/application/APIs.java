package com.bitvault.appstore.application;

/**
 * Created Dheeraj Bansal root on 17/5/17.
 * version 1.0.0
 * All Rest API url
 */

public class APIs {

    //For live
    //private static String API_BASE_URL = "http://52.10.154.132:8080/cloud/rest/api/v1/mobile/";

    //For test
    private static String API_BASE_URL = "http://54.201.240.187:8080/cloud/rest/api/v1/mobile/";


    public static String API_DASHBOARD_CATEGORY_APP = API_BASE_URL + "categoryApp";
    public static String API_DASHBOARD_CATEGORY_BANNER = API_BASE_URL + "appCategoryBanner";
    public static String API_SEARCH = API_BASE_URL + "search/app?";
    public static String API_APP_DETAIL = API_BASE_URL + "appDetailByAppId/";
    public static String API_APP_FULL_DESCRIPTION = API_BASE_URL + "getAppFullDesc";
    public static String API_CATEGORY = API_BASE_URL + "publishedApplicationByCategory?";
    public static String API_UPDATE_AVAILABLE = "";
    public static String API_DOWNLOAD_APP = "";
    public static String API_RATING_SUBMIT = "";
    public static String API_ALL_RATING = API_BASE_URL + "getAppAllReviews";
    public static String API_BITVAULT_APK_URL = API_BASE_URL + "getBitVaultApKUrl";
    public static String API_DOWNLOAD_APK = API_BASE_URL + "downloadAPK/";
    public static String API_INSTALL_APK_PERMISSIONS = API_BASE_URL + "appPermisions";
    public static String API_BANNER_APPLICATION = API_BASE_URL + "getBannerApplication/";
    public static String API_ADD_RATE_REVIEW = API_BASE_URL + "saveRateReview";
    public static String API_RATE_REVIEW_LIST = API_BASE_URL + "rateReviewList?";
    public static String API_LATEST_VERSION = API_BASE_URL + "getAppsLatestVersion";
    public static String API_APK_ON_DEVICE_ACTION = API_BASE_URL + "apkOnDeviceAction?";
    public static String API_UNINSTALL_MULTIPLE_APK = API_BASE_URL + "uninstallMultiApk?";
    public static String API_DELETE_RATE_REVIEW = API_BASE_URL + "deleteReview/";
    public static String API_APP_APP_TYPE = API_BASE_URL + "app/appType";
    public static String API_HELP_SUPPORT = API_BASE_URL + "helpSupport";
    public static String API_PAYMENT_ADDRESS = API_BASE_URL + "getPaymentAddress/";
    public static String API_SAVE_MOBILE_PAYMENT = API_BASE_URL + "saveMobilePayment";
    public static String API_EOT_TO_USD = "https://api.coinmarketcap.com/v1/ticker/eot-token/";


}
