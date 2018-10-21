package com.bitvault.appstore.app.dashboard;

/*
 * Created by Dheeraj Bansal on 13/2/17.
 * version 1.0.0
 */

/**
 * drawer item
 */
public class DrawerItem {


    private String name;
    private String icon;

    /**
     * icon and name of drawer item
     * @param icon
     * @param name
     */
    public DrawerItem(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    /**
     * get icon
     * @return
     */
    public String getIcon() {
        return icon;
    }

    /**
     * get name
     * @return
     */
    public String getName() {
        return name;
    }


}
