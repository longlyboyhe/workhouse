package android.com.fastandroid.ui.base;

import android.app.Activity;
import android.com.fastandroid.utils.Log;
import android.com.fastandroid.utils.ManagerActivity;
import android.os.Bundle;
import android.view.Window;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.ui.base
 * 当前类作用：当前类注释:Activity框架封装类
 * 作者：longlyboyhe on 2015/11/10 10:48
 * 邮箱：longlyboyhe@126.com
 */
public class BaseFrameActvity extends Activity {
    private static final String TAG="lifescycle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除系统标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d(TAG, "BaseActivity onCreate Invoke...");
        ManagerActivity.getInstance().addActivity(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "BaseActivity onStart Invoke...");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "BaseActivity onRestart Invoke...");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "BaseActivity onResume Invoke...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "BaseActivity onPause Invoke...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "BaseActivity onStop Invoke...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BaseActivity onDestroy Invoke...");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "BaseActivity onLowMemory Invoke...");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "BaseActivity onBackPressed Invoke...");
        ManagerActivity.getInstance().removeActivity(this);
    }
}
