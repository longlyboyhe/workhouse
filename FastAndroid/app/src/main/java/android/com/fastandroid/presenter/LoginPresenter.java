package android.com.fastandroid.presenter;

import android.com.fastandroid.business.IPersonBiz;
import android.com.fastandroid.business.LoginRequestCallBack;
import android.com.fastandroid.business.imp.PersonBizImp;
import android.com.fastandroid.entity.PersonBean;
import android.com.fastandroid.ui.interfaces.ILoginView;
import android.com.fastandroid.utils.Log;
import android.os.Handler;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.presenter
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/10 11:02
 * 邮箱：longlyboyhe@126.com
 */
public class LoginPresenter {
    private static final  String TAG="LoginPresenter";
    private ILoginView mLoginView;
    private IPersonBiz mPersonBiz;

    private Handler mHandler=new Handler();

    public LoginPresenter(ILoginView view) {
        mLoginView = view;
        mPersonBiz = new PersonBizImp();
    }

    public void loginSystem(){
        mPersonBiz.login(mLoginView.getUserName(), mLoginView.getPassword(), new LoginRequestCallBack() {
            /**
             * 登录成功
             * @param personBean
             */
            @Override
            public void loginSuccess(final PersonBean personBean) {
                Log.d(TAG, "登录成功:" + personBean.toString());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.showSuccessInfo(personBean);
                    }
                });
            }
            /**
             * 登录失败
             */
            @Override
            public void loginFailed() {
                Log.d(TAG, "登录失败...");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.showFailedInfo();
                    }
                });
            }
        });
    }
}
