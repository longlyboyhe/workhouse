package android.com.fastandroid.utils;

import android.com.fastandroid.application.FApplication;
import android.com.fastandroid.data.spreference.SharedPreferencesHelper;
import android.content.Context;

/**
 * 当前类注释:查询与设置引导界面标志值工具类
 * 主要用于引导界面，APP新版本第一次打开 才会进行使用引导界面
 * 项目名：FastAndroid
 * 包名：android.com.fastandroid.utils
 * 作者：江清清 on 15/10/22 09:47
 * 邮箱：longlyboyhe@126.com
 */
public class GuideUtils {
    private static final  String KEY_GUIDE_ACTIVITY="key_guide_activity";
    /**
     * 根据版本code值 判断是否已经引导过了
     * @param context  上下文
     * @param versionCode  版本值
     * @return  引导过返回true,否则返回false
     */
    public static boolean activityIsGuided(Context context,int versionCode){
        SharedPreferencesHelper mSharedPreferencesHelper=SharedPreferencesHelper.getInstance(FApplication.getInstance());
        if(mSharedPreferencesHelper.getIntValue(KEY_GUIDE_ACTIVITY)==versionCode){
            return true;
        }
        return false;
    }
    /**
     * 设置code值，表明已经引导过
     * @param context
     * @param versionCode
     */
    public static void setIsGuided(Context context,int versionCode){
        SharedPreferencesHelper mSharedPreferencesHelper=SharedPreferencesHelper.getInstance(FApplication.getInstance());
        mSharedPreferencesHelper.putIntValue(KEY_GUIDE_ACTIVITY, versionCode);
    }
}
