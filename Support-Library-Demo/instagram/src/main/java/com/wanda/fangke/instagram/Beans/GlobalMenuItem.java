package com.wanda.fangke.instagram.Beans;

/**
 * Created by fangke on 2015/6/15.
 */
public class GlobalMenuItem {

    public int iconResId;
    public String label;
    public boolean isDivider;

    private GlobalMenuItem() {
    }

    public GlobalMenuItem(int iconResId, String label) {
        this.iconResId = iconResId;
        this.label = label;
        this.isDivider = false;
    }

    public static GlobalMenuItem dividerMenuItem() {
        GlobalMenuItem globalMenuItem = new GlobalMenuItem();
        globalMenuItem.isDivider = true;
        return globalMenuItem;
    }
}
