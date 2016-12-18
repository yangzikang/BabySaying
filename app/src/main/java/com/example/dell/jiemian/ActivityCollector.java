/************************************************
 * 对activity进行管理
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;


/************************************************
 * 对menuActivity以前的活动进行保存，退出的时候直接删除所有
 ***********************************************/
public class ActivityCollector {
    public static List<Activity>activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        if(!activity.isFinishing()){
            activity.finish();
        }
        activities.remove(activity);
    }
    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
