package android.com.fastandroid.permission;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.permission
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/17 17:49
 * 邮箱：longlyboyhe@126.com
 */
public class SinglePermission {
    private String mPermissionName;
    private boolean mRationalNeeded = false;
    private String mReason;

    public SinglePermission(String permissionName) {
        mPermissionName = permissionName;
    }

    public SinglePermission(String permissionName, String reason) {
        mPermissionName = permissionName;
        mReason = reason;
    }

    public boolean isRationalNeeded() {
        return mRationalNeeded;
    }

    public void setRationalNeeded(boolean rationalNeeded) {
        mRationalNeeded = rationalNeeded;
    }

    public String getReason() {
        return mReason == null ? "" : mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }

    public String getPermissionName() {
        return mPermissionName;
    }

    public void setPermissionName(String permissionName) {
        mPermissionName = permissionName;
    }
}
