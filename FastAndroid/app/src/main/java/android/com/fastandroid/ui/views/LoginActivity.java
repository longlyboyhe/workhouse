package android.com.fastandroid.ui.views;

import android.com.fastandroid.R;
import android.com.fastandroid.entity.PersonBean;
import android.com.fastandroid.presenter.LoginPresenter;
import android.com.fastandroid.ui.base.BaseActivity;
import android.com.fastandroid.ui.interfaces.ILoginView;
import android.com.fastandroid.utils.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.ui.views
 * 当前类作用：登录界面，MVP开发模式登录实例
 * 作者：longlyboyhe on 2015/11/10 11:01
 * 邮箱：longlyboyhe@126.com
 */
public class LoginActivity extends BaseActivity implements ILoginView {
    private static  final  String TAG="LoginActivity";

    private EditText ed_username;
    private EditText ed_password;
    private Button btn_login;
    private LoginPresenter mLoginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ed_username=(EditText)this.findViewById(R.id.ed_username);
        ed_password=(EditText)this.findViewById(R.id.ed_password);
        btn_login=(Button)this.findViewById(R.id.btn_login);
        mLoginPresenter=new LoginPresenter(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.loginSystem();
            }
        });
    }
    /**
     * 进行返回用户名信息
     * @return
     */
    @Override
    public String getUserName() {
        return ed_username.getText().toString().trim();
    }
    /**
     * 进行返回用户密码信息
     * @return
     */
    @Override
    public String getPassword() {
        return ed_password.getText().toString().trim();
    }
    /**
     * 登录成功 回调
     * @param personBean
     */
    @Override
    public void showSuccessInfo(PersonBean personBean) {
        Log.d(TAG, "showSuccessInfo:" + personBean.toString());
        showToastMsgShort("登录成功:"+personBean.toString());
    }
    /**
     * 登录失败 回调
     */
    @Override
    public void showFailedInfo() {
        Log.d(TAG, "showFailedInfo...");
        showToastMsgShort("登录失败...");
    }
}
