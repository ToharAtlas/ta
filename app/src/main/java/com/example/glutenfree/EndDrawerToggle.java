package com.example.glutenfree;

import android.content.res.Configuration;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class EndDrawerToggle implements DrawerLayout.DrawerListener {

    private final DrawerLayout drawerLayout;
    private final AppCompatImageButton toggleButton;
    private final int openDrawerContentDescRes;
    private final int closeDrawerContentDescRes;

    private DrawerArrowDrawable arrowDrawable;

    public EndDrawerToggle(DrawerLayout drawerLayout, Toolbar toolbar,
                           int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        this.drawerLayout = drawerLayout;

        this.openDrawerContentDescRes = openDrawerContentDescRes;
        this.closeDrawerContentDescRes = closeDrawerContentDescRes;

        toggleButton = new AppCompatImageButton(toolbar.getContext(), null, R.drawable.icon_menu);
        // toggleButton.setBackgroundResource(R.color.white);
        toggleButton.setColorFilter(R.color.design_default_color_background);

        toolbar.addView(toggleButton, new Toolbar.LayoutParams(GravityCompat.START));
        toggleButton.setOnClickListener(v -> toggle());

        loadDrawerArrowDrawable();
    }

    public void syncState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            setPosition(1f);

        } else {
            setPosition(0f);

        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        loadDrawerArrowDrawable();
        syncState();
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        setPosition(Math.min(1f, Math.max(0f, slideOffset)));
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        setPosition(1f);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        setPosition(0f);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    private void loadDrawerArrowDrawable() {
        arrowDrawable = new DrawerArrowDrawable(toggleButton.getContext());
        arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_START);
        toggleButton.setImageDrawable(arrowDrawable);
    }

    private void toggle() {
        final int drawerLockMode = drawerLayout.getDrawerLockMode(GravityCompat.START);
        if (drawerLayout.isDrawerVisible(GravityCompat.START)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void setPosition(float position) {
        if (position == 1f) {
            arrowDrawable.setVerticalMirror(true);
            setContentDescription(closeDrawerContentDescRes);
        } else if (position == 0f) {
            arrowDrawable.setVerticalMirror(false);
            setContentDescription(openDrawerContentDescRes);
        }
        arrowDrawable.setProgress(position);

    }

    private void setContentDescription(int resId) {
        toggleButton.setContentDescription(toggleButton.getContext().getText(resId));
    }
}
