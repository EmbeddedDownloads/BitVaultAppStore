package com.bitvault.appstore.webservice.response;

/**
 * Created by anuj on 4/5/17.
 * version 1.0.0
 * Reviews response of an application
 */

public class ReviewModel {

    //remove later
    //private String applicationId;
    //private String createdAt;

    //remove later
    private String review_subject;
    private String name;

    private String appReview;
    private String appRating;
    private String updatedAt;

    private String replyResponse;
    private String replyFrom;
    private String replyUpdatedAt;

    // remove later
//    public String getApplicationId() {
//        return applicationId;
//    }
//    public String getCreatedAt() {
//        return createdAt;
//    }

    //remove later
    public String getReview_subject() {
        return review_subject;
    }

        public String getName() {
        return name;
    }



    public String getAppReview() {
        return appReview;
    }

    public String getAppRating() {
        return appRating;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }


    public String getReplyResponse() {
        return replyResponse;
    }

    public String getReplyFrom() {
        return replyFrom;
    }

    public String getReplyupdatedAt() {
        return replyUpdatedAt;
    }

}
