package android.com.fastandroid.ui.base;

import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.ui.base
 * 当前类作用：基类Actvity 主要封装一些工具类的使用,公共方法,配置
 * 作者：longlyboyhe on 2015/11/10 10:45
 * 邮箱：longlyboyhe@126.com
 */
public class BaseActivity extends  BaseFrameActvity{
    /**
     * 获取当前view的LayoutInflater实例
     * @return
     */
    protected LayoutInflater getLayouInflater() {
        LayoutInflater _LayoutInflater = LayoutInflater.from(this);
        return _LayoutInflater;
    }

    /**
     * 弹出toast 显示时长short
     * @param pMsg
     */
    protected void showToastMsgShort(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 弹出toase 显示时长long
     * @param pMsg
     */
    protected void showToastMsgLong(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
    }
    /**
     * 根据传入的类(class)打开指定的activity
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        Intent _Intent = new Intent();
        _Intent.setClass(this, pClass);
        startActivity(_Intent);
    }

    protected void openActivityByIntent(Intent pIntent){
        startActivity(pIntent);
    }

}
