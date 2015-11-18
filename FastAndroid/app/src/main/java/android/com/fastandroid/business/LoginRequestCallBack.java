package android.com.fastandroid.business;

import android.com.fastandroid.entity.PersonBean;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.business
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/9 14:11
 * 邮箱：longlyboyhe@126.com
 */
public interface LoginRequestCallBack {
    //登录成功回调方法
    void loginSuccess(PersonBean personBean);
    //登录失败回调方法
    void loginFailed();
}
