package com.bitvault.appstore.webservice.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by anuj on 2/5/17.
 * version 1.0.0
 * AppFullDescription response data
 */

public class AppDescriptionInfo implements Parcelable{

    private String title;
    private String short_description;
    private String full_description;
    private String whats_new;
    private String version;
    private String totalDownloads;
    private String offered_by;
    private String email;
    private String developer_address;
    private String apkFilesize;
    private String updated_date;

    public AppDescriptionInfo(String title, String short_description, String full_description, String whats_new, String version, String totalDownloads, String offered_by, String email, String developer_address, String apkFilesize, String updated_date) {
        this.title = title;
        this.short_description = short_description;
        this.full_description = full_description;
        this.whats_new = whats_new;
        this.version = version;
        this.totalDownloads = totalDownloads;
        this.offered_by = offered_by;
        this.email = email;
        this.developer_address = developer_address;
        this.apkFilesize = apkFilesize;
        this.updated_date = updated_date;
    }

    protected AppDescriptionInfo(Parcel in) {
        title = in.readString();
        short_description = in.readString();
        full_description = in.readString();
        whats_new = in.readString();
        version = in.readString();
        totalDownloads = in.readString();
        offered_by = in.readString();
        email = in.readString();
        developer_address = in.readString();
        apkFilesize = in.readString();
        updated_date = in.readString();
    }

    public static final Creator<AppDescriptionInfo> CREATOR = new Creator<AppDescriptionInfo>() {
        @Override
        public AppDescriptionInfo createFromParcel(Parcel in) {
            return new AppDescriptionInfo(in);
        }

        @Override
        public AppDescriptionInfo[] newArray(int size) {
            return new AppDescriptionInfo[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getFull_description() {
        return full_description;
    }

    public String getWhats_new() {
        return whats_new;
    }

    public void setWhats_new(String whats_new) {
        this.whats_new = whats_new;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(String totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public String getApkFilesize() {
        return apkFilesize;
    }

    public void setApkFilesize(String apkFilesize) {
        this.apkFilesize = apkFilesize;
    }

    public String getOffered_by() {
        return offered_by;
    }

    public void setOffered_by(String offered_by) {
        this.offered_by = offered_by;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeveloper_address() {
        return developer_address;
    }

    public void setDeveloper_address(String developer_address) {
        this.developer_address = developer_address;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTitle());
        parcel.writeString(getShort_description());
        parcel.writeString(getFull_description());
        parcel.writeString(getWhats_new());
        parcel.writeString(getVersion());
        parcel.writeString(getTotalDownloads());
        parcel.writeString(getOffered_by());
        parcel.writeString(getEmail());
        parcel.writeString(getDeveloper_address());
        parcel.writeString(getApkFilesize());
        parcel.writeString(getUpdated_date());

    }
}
