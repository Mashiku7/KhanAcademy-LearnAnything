package com.andrewpham.android.khanacademy_learnanything.controllers;

import android.support.v4.app.Fragment;

public class WebpageActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new WebpageFragment();
    }
}
