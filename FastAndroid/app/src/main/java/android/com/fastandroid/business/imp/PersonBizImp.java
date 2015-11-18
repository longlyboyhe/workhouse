package android.com.fastandroid.business.imp;

import android.com.fastandroid.business.IPersonBiz;
import android.com.fastandroid.business.LoginRequestCallBack;
import android.com.fastandroid.entity.PersonBean;
import android.com.fastandroid.utils.Log;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.business.imp
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/9 14:06
 * 邮箱：longlyboyhe@126.com
 */
public class PersonBizImp implements IPersonBiz{
    private static final  String TAG="PersonBizImp";
    @Override
    public void login(final String username, final String password, final LoginRequestCallBack valueCallBack) {
        Log.d(TAG, "username:" + username + ",password:" + password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //进行开始登录,这边应该进行请求服务器，进行数据验证
//                Observable<PersonBean>  result = RetrofitHelp.getApi().listGankDay();
                if(username.equals("longlyboyhe")&&password.equals("12345")){
                    valueCallBack.loginSuccess(new PersonBean(username,password));
                }else{
                    valueCallBack.loginFailed();
                }
            }
        }).start();
    }
}
