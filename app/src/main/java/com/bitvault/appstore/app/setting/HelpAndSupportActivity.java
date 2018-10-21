package com.bitvault.appstore.app.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bitvault.appstore.R;

/**
 * Created by anuj on 9/5/17.
 * version 1.0.0
 * help and support screen
 */

public class HelpAndSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_help_and_support);
    }


    /**
     * to close activity
     * @param view
     */
    public void onCancelClicked(View view){
        finish();
    }

}
