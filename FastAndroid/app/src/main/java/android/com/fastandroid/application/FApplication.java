package android.com.fastandroid.application;

import android.app.Application;
import android.com.fastandroid.analytics.crash.CustomCrash;
import java.util.HashMap;
import java.util.Objects;


/**
 * 当前类注释:自定义全局 application 主要进全局引用,行存储全局变量,全局配置/设置,初始化等相关工作
 * 项目名：FastAndroid
 * 包名：android.com.fastandroid.widget
 * 作者：longlyboyhe on 15/10/23 08:41
 * 邮箱：longlyboyhe@126.com
 * QQ： 1462780453
 * 公司：技术猿
 */
public class FApplication extends Application{
    private HashMap<String,Objects> mTemp=new HashMap<String,Objects>();
    private static FApplication instance;
    public  static FApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        this.instance=this;
        //初始化崩溃日志收集器
        CustomCrash mCustomCrash=CustomCrash.getInstance();
        //mCustomCrash.setCustomCrashInfo(this);
    }
}
