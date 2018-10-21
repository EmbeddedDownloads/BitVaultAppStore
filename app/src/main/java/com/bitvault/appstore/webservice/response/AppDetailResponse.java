package com.bitvault.appstore.webservice.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created Dheeraj Bansal root on 9/5/17.
 * version 1.0.0
 *  Application detail response data
 */

public class AppDetailResponse implements Serializable{

    private Result result;

    public Result getResult() {
        return result;
    }

    public static class Result{
        private AppReview appReview;
        private AppDetail appDetail;
        private Star starRating;

        public AppReview getAppReview() {
            return appReview;
        }
        public AppDetail getAppDetail() {
            return appDetail;
        }
        public AppDetailResponse.Result.Star getStar() {
            return starRating;
        }

        public static class AppReview {
            private String appRating;
            private String appReview;
            private String appRateReviewId;
            private String updatedAt;

            public String getAppRating() {
                return appRating;
            }

            public String getAppReview() {
                return appReview;
            }

            public String getAppRateReviewId() {
                return appRateReviewId;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }
        }

        public static class AppDetail {
            private String appDetailId;
            private Application application;
            private List<screenshots> appImage;
            private String shortDescription;
            private String fullDescription;
            private String whats_new;
            private String createdAt;

            public String getAppDetailId() {
                return appDetailId;
            }

            public Application getApplication() {
                return application;
            }

            public List<screenshots> getScreenshots() {
                return appImage;
            }

            public String getShort_description() {
                return shortDescription;
            }

            public String getWhats_new() {
                return whats_new;
            }

            public String getFullDescription() {
                return fullDescription;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public static class screenshots implements Serializable {
                private int appImagesId;
                private String imageType;
                private String appImageUrl;

                public int getAppImagesId() {
                    return appImagesId;
                }

                public String getImageType() {
                    return imageType;
                }

                public String getAppImageUrl() {
                    return appImageUrl;
                }
            }
        }

        public static class Star implements Serializable{
            private long star5;
            private long star4;
            private long star3;
            private long star2;
            private long star1;

            public long getStar5() {
                return star5;
            }

            public long getStar4() {
                return star4;
            }

            public long getStar3() {
                return star3;
            }

            public long getStar2() {
                return star2;
            }

            public long getStar1() {
                return star1;
            }
        }

    }

    public static class Application implements Serializable {
        private String applicationId;
        private String packageName;
        private String appIconUrl;
        private String appName;
        private String title;
        private Float appPrice;
        private AppCategory appCategory;
        private AppType applicationType;

        private String company;

        private long totalDownloads;
        private String download_unit;

        private String appPermission;

        private List<AppPermissions.Permission> permissions;

        private String latestVersion;
        private String latestVersionNo;
        private List<review> review;
        private String website;
        private String email;
        private String averageRating;
        private String whats_new;
        private String apkFilesize;
        private String updatedAt;
        private String privacyPolicyUrl;
        private String paymentStatus;

        public String getLatestVersionNo() {
            return latestVersionNo;
        }

        public String getApplicationId() {
            return applicationId;
        }

        public AppType getApplicationType() {
            return applicationType;
        }
        public AppCategory getAppCategory() {
            return appCategory;
        }

        public String getDownload_unit() {
            return download_unit;
        }

        public String getPackage_name() {
            return packageName;
        }

        public String getApp_icon_url() {
            return appIconUrl;
        }


        public String getCompany() {
            return company;
        }
        public String getApp_name() {
            return appName;
        }

        public String getTitle() {
            return title;
        }

        //use later
        //public String getPrice_type() {
//            return price_type;
//        }

        public Float getAppPrice() {
            return appPrice;
        }

        public long getTotalDownloads() {
            return totalDownloads;
        }

        public String getApp_version() {
            return latestVersion;
        }

        public String getAppPermission() {
            return appPermission;
        }

        public List<AppPermissions.Permission> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<AppPermissions.Permission> permissions) {
            this.permissions = permissions;
        }

        public List<Application.review> getReview() {
            return review;
        }

        public String getWebsite() {
            return website;
        }

        public String getEmail() {
            return email;
        }

        public String getPrivacyPolicyUrl() {
            return privacyPolicyUrl;
        }

        public String getRating() {
            return averageRating;
        }

        public String getWhats_new() {
            return whats_new;
        }

        public String getApkFilesize() {
            return apkFilesize;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public static class AppCategory {
            private String appCategoryId;
            private String categoryType;
            private String category_icon;

            public String getCategory_id() {
                return appCategoryId;
            }
            public String getCategory_icon() {
                return category_icon;
            }

            //use later
//            public String getCategory_name() {
//                return category_name;
//            }
        }

        public static class AppType {

            private String appTypeId;
            private String  appTypeName;
            private String appTypeIcon;

            public String getAppTypeId() {
                return appTypeId;
            }

            public String getAppTypeName() {
                return appTypeName;
            }

            public String getAppTypeIcon() {
                return appTypeIcon;
            }
        }

        public static class review {
            private int review_id;
            private String review;
            private double rating;
            private String created_at;

            public int getReview_id() {
                return review_id;
            }

            public String getReview() {
                return review;
            }

            public double getRating() {
                return rating;
            }

            public String getCreated_at() {
                return created_at;
            }

        }


    }


}
