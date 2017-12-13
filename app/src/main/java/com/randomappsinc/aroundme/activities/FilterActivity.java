package com.randomappsinc.aroundme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.randomappsinc.aroundme.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.close)
    public void closeFilter() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
