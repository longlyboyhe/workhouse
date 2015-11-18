package android.com.fastandroid.ui.interfaces;

import android.com.fastandroid.entity.PersonBean;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.ui.interfaces
 * 当前类作用：登录页面 相关操作 功能接口
 * 作者：longlyboyhe on 2015/11/10 10:59
 * 邮箱：longlyboyhe@126.com
 */
public interface ILoginView {
    //获取用户名
    String getUserName();
    //获取密码
    String getPassword();

    void showSuccessInfo(PersonBean personBean);
    void showFailedInfo();
}
