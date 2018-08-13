package com.sphinxs.autombstone;

import android.app.Activity;
import android.os.Bundle;

import com.sphinxs.autombstone.core.AutombstoneApi;

/**
 * 自动墓碑化Activity基类
 */
public class AutombstoneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            AutombstoneApi.restore(this, savedInstanceState);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        AutombstoneApi.restore(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AutombstoneApi.save(this, outState);
    }
}
