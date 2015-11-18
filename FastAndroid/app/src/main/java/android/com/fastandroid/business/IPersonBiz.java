package android.com.fastandroid.business;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.business
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/9 14:05
 * 邮箱：longlyboyhe@126.com
 */
public interface IPersonBiz {
    void login(String username,String password,LoginRequestCallBack valueCallBack);
}
