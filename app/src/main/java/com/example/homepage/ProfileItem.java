package com.example.homepage;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public class ProfileItem {

    @DrawableRes
    private final int icon;
    @ColorRes
    private final int iconBackgroundColor;
    @ColorRes
    private final int iconTintColor;
    private final String label;
    private final String value;

    public ProfileItem(int icon, int iconBackgroundColor, int iconTintColor, String label, String value) {
        this.icon = icon;
        this.iconBackgroundColor = iconBackgroundColor;
        this.iconTintColor = iconTintColor;
        this.label = label;
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public int getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public int getIconTintColor() {
        return iconTintColor;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}