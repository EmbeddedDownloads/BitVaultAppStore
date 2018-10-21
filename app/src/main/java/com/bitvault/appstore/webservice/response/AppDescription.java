package com.bitvault.appstore.webservice.response;

import java.util.ArrayList;

/**
 * Created by anuj on 3/5/17.
 * version 1.0.0
 *  App full description of application response
 */

public class AppDescription {


    public ArrayList<AppDescriptionInfo> getApp_description() {
        return app_description;
    }

    public void setApp_description(ArrayList<AppDescriptionInfo> app_description) {
        this.app_description = app_description;
    }

    public ArrayList<AppDescriptionInfo> app_description;


}
