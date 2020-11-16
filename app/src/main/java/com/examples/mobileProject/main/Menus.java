package com.examples.mobileProject.main;

import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class Menus {
    ConstraintLayout layout;
    ImageView img ;
    Fragment fragment;
    int index;

    public Menus(ConstraintLayout layout, ImageView img, Fragment fragment, int index) {
        this.layout = layout;
        this.img = img;
        this.fragment = fragment;
        this.index = index;
    }
}