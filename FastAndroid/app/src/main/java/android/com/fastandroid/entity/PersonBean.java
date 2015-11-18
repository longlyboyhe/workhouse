package android.com.fastandroid.entity;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.entity
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/9 14:03
 * 邮箱：longlyboyhe@126.com
 */
public class PersonBean {
    private String username;
    private String password;

    public PersonBean() {
    }

    public PersonBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
