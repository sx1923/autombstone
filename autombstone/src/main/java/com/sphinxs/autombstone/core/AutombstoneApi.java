package com.sphinxs.autombstone.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动墓碑化框架对外暴露的管理类
 * Created by sphinxsun on 2018-05-07
 */
public class AutombstoneApi {

    private final static String AUTOMBSTONE_END = "_Autombstone";

    /**
     * 自动墓碑化Map
     * Key: Activity
     * Value：Activity_Autombstone
     */
    private static Map<String, Autombstone> sAutombstoneMap = new HashMap<>();

    /**
     * 数据保存
     *
     * @param target
     * @param outState
     */
    @NonNull
    public static void save(@NonNull Activity target, @NonNull Bundle outState) {
        Autombstone autombstone = findAutombstone(target);
        if (autombstone != null) {
            autombstone.save(target, outState);
        }
    }

    /**
     * 数据恢复
     *
     * @param target
     * @param savedInstanceState
     */
    @NonNull
    public static void restore(@NonNull Activity target, @NonNull Bundle savedInstanceState) {
        Autombstone autombstone = findAutombstone(target);
        if (autombstone != null) {
            autombstone.restore(target, savedInstanceState);
        }

    }

    private static Autombstone findAutombstone(Activity target) {
        String clsName = target.getClass().getName();
        Autombstone autombstone = sAutombstoneMap.get(clsName);
        if (autombstone != null) {
            return autombstone;
        }
        String autombstoneClassName = clsName + AUTOMBSTONE_END;
        try {
            Class<?> findClass = Class.forName(autombstoneClassName);
            autombstone = (Autombstone) findClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find class " + autombstoneClassName, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to new instance " + autombstoneClassName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to new instance " + autombstoneClassName, e);
        }
        sAutombstoneMap.put(clsName, autombstone);
        return autombstone;
    }

}
