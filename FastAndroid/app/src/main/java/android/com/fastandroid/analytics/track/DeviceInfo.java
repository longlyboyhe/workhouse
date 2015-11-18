package android.com.fastandroid.analytics.track;

import android.com.fastandroid.utils.Log;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Locale;

/**
 * 项目名称：FastAndroid
 * 包名：PACKAGE_NAME
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/16 11:24
 * 邮箱：longlyboyhe@126.com
 */
public class DeviceInfo {
    private static String TAG = "DeviceInfo";
    //---------------自定义meta-------------------

    private static String DEFAULT_CHANNEL = "1000";
    private static String DEFAULT_APPVERSION = "1.0.0.0";

    /**
     * 获取发布渠道信息
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        String msg = DEFAULT_CHANNEL;
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getInt("channel") + "";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msg = "0000";
        }
        return msg;
    }


//---------------唯一标识UDID-------------------

    /*
	 * Generate a new OpenUDID
	 */
    public static String generateOpenUDID(Context context) {
        Log.d(TAG, "Generating openUDID");
        //Try to get the ANDROID_ID
        String OpenUDID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (OpenUDID == null || OpenUDID.equals("9774d56d682e549c") || OpenUDID.length() < 15 ) {
            //if ANDROID_ID is null, or it's equals to the GalaxyTab generic ANDROID_ID or bad, generates a new one
            final SecureRandom random = new SecureRandom();
            OpenUDID = new BigInteger(64, random).toString(16);
        }
        return OpenUDID;
    }


//---------------系统固有信息-------------------

    /**
     * get current connected network type
     *
     * @return
     */
    public static String getNetType(Context context) {
        String type = null;
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMan.getActiveNetworkInfo();
        if (info != null) // TYPE_MOBILE
        {
            switch (info.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    switch (info.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                            type = "EDGE";
                            break;
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                            type = "CDMA";
                            break;
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                            type = "GPRS";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            type = "EVDO_0";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            type = "UNKOWN";
                            break;
                    }
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    type = "wifi";
                    break;
            }
        } else
            type = "outofnetwork";
        return type;
    }

    /**
     * 系统类型
     *
     * @return
     */
    public static String getOS() {
        return "Android";
    }

    /**
     * 系统版本号
     *
     * @return
     */
    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }


    /**
     * 手机型号
     *
     * @return
     */
    public static String getDevice() {
        return android.os.Build.MODEL;
    }

    /**
     * 分辨率
     *
     * @param context
     * @return like “480x800”
     */
    static String getResolution(final Context context) {
        // user reported NPE in this method; that means either getSystemService or getDefaultDisplay
        // were returning null, even though the documentation doesn't say they should do so; so now
        // we catch Throwable and return empty string if that happens
        String resolution = "";
        try {
            final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            final Display display = wm.getDefaultDisplay();
            final DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            resolution = metrics.widthPixels + "x" + metrics.heightPixels;
        } catch (Throwable t) {

            Log.i(TAG, "Device resolution cannot be determined");

        }
        return resolution;
    }

    /**
     * 获取屏幕密度分级
     * @param context
     * @return
     */
    public static String getDensity(final Context context) {
        String densityStr = "";
        final int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                densityStr = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                densityStr = "MDPI";
                break;
            case DisplayMetrics.DENSITY_TV:
                densityStr = "TVDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                densityStr = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                densityStr = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_400:
                densityStr = "XMHDPI";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                densityStr = "XXHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                densityStr = "XXXHDPI";
                break;
        }
        return densityStr;
    }


    /**
     * 运营商名
     *
     * @param context
     * @return
     */
    public static String getCarrier(final Context context) {
        String carrier = "";
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            carrier = manager.getNetworkOperatorName();
        }
        if (carrier == null || carrier.length() == 0) {
            carrier = "";
            Log.i(TAG, "No carrier found");
        }
        return carrier;
    }

    /**
     * 获得本地化信息
     * @return “语言_国家”
     */
    public static String getLocale() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    /**
     * app 版本
     *
     * @param context
     * @return
     */
    public static String getAppVersion(final Context context) {
        String result = DEFAULT_APPVERSION;
        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "No app version found");
        }
        return result;
    }


    /**
     * Returns the package name of the app that installed this app
     */
    static String getStore(final Context context) {
        String result = "";
        if (android.os.Build.VERSION.SDK_INT >= 3) {
            try {
                result = context.getPackageManager().getInstallerPackageName(context.getPackageName());
            } catch (Exception e) {
                Log.i(TAG, "Can't get Installer package");
            }
            if (result == null || result.length() == 0) {
                result = "";
                Log.i(TAG, "No store found");
            }
        }
        return result;
    }

    /**
     * 把设备和app信息组装成json
     *
     * @param context
     * @return
     */
    public static String getMetrics(Context context) {
        String result = "";
        JSONObject json = new JSONObject();

        try {
            json.put("_device", getDevice());
            json.put("_os", getOS());
            json.put("_os_version", getOSVersion());
            json.put("_carrier", getCarrier(context));
            json.put("_resolution", getResolution(context));
            json.put("_density", getDensity(context));
            json.put("_locale", getLocale());
            json.put("_app_version", getAppVersion(context));
            json.put("_channel", getChannel(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result = json.toString();


        //Log.d("metric origin",result);

        try {
           //确认编码为utf-8字符
            result = java.net.URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }

        return result;
    }
}
