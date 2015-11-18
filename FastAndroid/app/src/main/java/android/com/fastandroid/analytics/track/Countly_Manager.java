package android.com.fastandroid.analytics.track;

import android.com.fastandroid.utils.Log;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.analytics.track
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/16 16:01
 * 邮箱：longlyboyhe@126.com
 */
public class Countly_Manager {
    public final static String TAG = "OpenUDID";
    public final static String PREF_KEY = "openudid";
    public final static String PREFS_NAME = "openudid_prefs";
    private final Context mContext; //Application context
    private List<ResolveInfo> mMatchingIntents; //List of available OpenUDID Intents
    private Map<String, Integer> mReceivedOpenUDIDs; //Map of OpenUDIDs found so far

    private final SharedPreferences mPreferences; //Preferences to store the OpenUDID
    private final Random mRandom;

    private Countly_Manager(Context context) {
        mPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mContext = context;
        mRandom = new Random();
        mReceivedOpenUDIDs = new HashMap<String, Integer>();
    }

    /**
     * The Method the call at the init of your app
     *
     * @param context you current context
     */
    public static void sync(Context context) {
        //Initialise the Manager
        Countly_Manager manager = new Countly_Manager(context);

        //Try to get the openudid from local preferences
        String OpenUDID = manager.mPreferences.getString(PREF_KEY, null);
        if (OpenUDID == null) //Not found
        {
            //Get the list of all OpenUDID services available (including itself)
            manager.mMatchingIntents = context.getPackageManager().queryIntentServices(new Intent("org.OpenUDID.GETUDID"), 0);
            Log.d(TAG, manager.mMatchingIntents.size() + " services matches OpenUDID");

            if (manager.mMatchingIntents != null)
                //Start services one by one
                manager.startService();

        } else {//Got it, you can now call getOpenUDID()
            Log.d(TAG, "OpenUDID: " + OpenUDID);
            mInitialized = true;
        }
    }

    /*
	 * Start the oldest service
	 */
    private void startService() {
        if (mMatchingIntents.size() > 0) { //There are some Intents untested
            Log.d(TAG, "Trying service " + mMatchingIntents.get(0).loadLabel(mContext.getPackageManager()));

            final ServiceInfo servInfo = mMatchingIntents.get(0).serviceInfo;
            final Intent i = new Intent();
            i.setComponent(new ComponentName(servInfo.applicationInfo.packageName, servInfo.name));
            mMatchingIntents.remove(0);
            try	{	// try added by Lionscribe
                mContext.bindService(i, this,  Context.BIND_AUTO_CREATE);
            }
            catch (SecurityException e) {
                startService();	// ignore this one, and start next one
            }
        } else { //No more service to test

            getMostFrequentOpenUDID(); //Choose the most frequent

            if (OpenUDID == null) //No OpenUDID was chosen, generate one
                generateOpenUDID();
            Log.d(TAG, "OpenUDID: " + OpenUDID);
            storeOpenUDID();//Store it locally
            mInitialized = true;
        }
    }

    private void getMostFrequentOpenUDID() {
        if (mReceivedOpenUDIDs.isEmpty() == false) {
            final TreeMap<String,Integer> sorted_OpenUDIDS = new TreeMap(new ValueComparator());
            sorted_OpenUDIDS.putAll(mReceivedOpenUDIDs);
            OpenUDID = sorted_OpenUDIDS.firstKey();
        }
    }

    /*
	 * Used to sort the OpenUDIDs collected by occurrence
	 */
    private class ValueComparator implements Comparator {
        public int compare(Object a, Object b) {

            if(mReceivedOpenUDIDs.get(a) < mReceivedOpenUDIDs.get(b)) {
                return 1;
            } else if(mReceivedOpenUDIDs.get(a) == mReceivedOpenUDIDs.get(b)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}
