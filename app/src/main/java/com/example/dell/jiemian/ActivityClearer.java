package com.example.dell.jiemian;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/9/27.
 */
public class ActivityClearer {

    public static Map<String,Activity> map = new HashMap();

    public static void addActivity(String name,Activity activity){
        map.put(name,activity);
    }

    public static void removeActivity(String name){
        if(!map.get(name).isFinishing()) {
            map.get(name).finish();

        }
        System.out.println("释放");
    }
}
