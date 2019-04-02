package com.example.com.zy_demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.example.com.zy_demo.ViewPager.ForgotAdapter;
import com.example.com.zy_demo.ViewPager.PagerAdapter;

public class ForgotActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ViewPager viewPager = findViewById(R.id.forgot_viewpager);
        ForgotAdapter forgotAdapter = new ForgotAdapter(this);
        forgotAdapter.viewPager = viewPager;
        viewPager.setAdapter(forgotAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(10);
    }
}
