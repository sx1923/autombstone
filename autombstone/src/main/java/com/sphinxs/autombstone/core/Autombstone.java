package com.sphinxs.autombstone.core;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import java.lang.annotation.Annotation;

/**
 * 实现自动墓碑化框架的接口
 * Created by sphinxsun on 2018-05-07
 */
public interface Autombstone<T extends Activity> {

    /**
     * Activity数据的保存
     *
     * @param activity 源Activity
     * @param outState
     */
    void save(T activity, Bundle outState);

    /**
     * Activity数据的恢复
     *
     * @param activity
     * @param savedInstanceState
     */
    void restore(T activity, Bundle savedInstanceState);
}
