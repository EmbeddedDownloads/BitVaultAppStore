package com.bitvault.appstore.webservice.response;

import java.util.ArrayList;

/**
 * Created by anuj on 4/5/17.
 * version 1.0.0
 * All review of application
 */

public class AppReviewsModel {

    private String title;
    private String total_rating_count;
    private String _5star_rating;
    private String _4star_rating;
    private String _3star_rating;
    private String _2star_rating;
    private String _1star_rating;
    private String average_rating;

    private ArrayList<ReviewModel> result;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal_rating_count() {
        return total_rating_count;
    }

    public void setTotal_rating_count(String total_rating_count) {
        this.total_rating_count = total_rating_count;
    }

    public String get_5star_rating() {
        return _5star_rating;
    }

    public void set_5star_rating(String _5star_rating) {
        this._5star_rating = _5star_rating;
    }

    public String get_4star_rating() {
        return _4star_rating;
    }

    public void set_4star_rating(String _4star_rating) {
        this._4star_rating = _4star_rating;
    }

    public String get_3star_rating() {
        return _3star_rating;
    }

    public void set_3star_rating(String _3star_rating) {
        this._3star_rating = _3star_rating;
    }

    public String get_2star_rating() {
        return _2star_rating;
    }

    public void set_2star_rating(String _2star_rating) {
        this._2star_rating = _2star_rating;
    }

    public String get_1star_rating() {
        return _1star_rating;
    }

    public void set_1star_rating(String _1star_rating) {
        this._1star_rating = _1star_rating;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public ArrayList<ReviewModel> getApp_reviews() {
        return result;
    }



}
