package android.com.fastandroid.analytics.track;

import android.com.fastandroid.utils.Log;
import android.content.Context;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.analytics.track
 * 当前类作用：连接队列，主要控制网络发送
 * 作者：longlyboyhe on 2015/11/16 11:27
 * 邮箱：longlyboyhe@126.com
 */
public class ConnectionQueue {
    private CountlyStore store_;
    private Thread thread_ = null;
    private String appKey_;
    private Context context_;
    private String serverURL_;

    public void setAppKey(String appKey) {
        appKey_ = appKey;
    }

    public void setContext(Context context) {
        context_ = context;
    }

    public void setServerURL(String serverURL) {
        serverURL_ = serverURL;
    }

    public void setCountlyStore(CountlyStore countlyStore) {
        store_ = countlyStore;
    }

    public void beginSession() {
        String data;
        data = "app_key=" + appKey_;
        data += "&" + "device_id=" + DeviceInfo.generateOpenUDID(context_);
        data += "&" + "timestamp=" + (long) (System.currentTimeMillis() / 1000.0);
        data += "&" + "sdk_version=" + Countly.SDK_VERSION;
        data += "&" + "begin_session=" + "1";
        data += "&" + "metrics=" + DeviceInfo.getMetrics(context_);

        store_.addConnection(data);

        tick();
    }

    public void updateSession(int duration) {
        String data;
        data = "app_key=" + appKey_;
        data += "&" + "device_id=" + DeviceInfo.generateOpenUDID(context_);
        data += "&" + "timestamp=" + (long) (System.currentTimeMillis() / 1000.0);
        data += "&" + "session_duration=" + (duration > 0 ? duration : Countly.SESSION_DURATION_WHEN_TIME_ADJUSTED);
        store_.addConnection(data);

        tick();
    }

    public void endSession(int duration) {
        String data;
        data = "app_key=" + appKey_;
        data += "&" + "device_id=" + DeviceInfo.generateOpenUDID(context_);
        data += "&" + "timestamp=" + (long) (System.currentTimeMillis() / 1000.0);
        data += "&" + "end_session=" + "1";
        data += "&" + "session_duration=" + (duration > 0 ? duration : Countly.SESSION_DURATION_WHEN_TIME_ADJUSTED);

        store_.addConnection(data);

        tick();
    }

    /**
     * 注意这个方法，是唯一能导致自定义的event（即不是connection）上传到服务器的
     *
     * @param events
     */
    public void recordEvents(String events) {
        String data;
        data = "app_key=" + appKey_;
        data += "&" + "device_id=" + DeviceInfo.generateOpenUDID(context_);
        data += "&" + "timestamp=" + (long) (System.currentTimeMillis() / 1000.0);
        data += "&" + "events=" + events;
        if ((events.indexOf("crash") > 0) || (events.indexOf("feedback") > 0))
            data += "&" + "metrics=" + DeviceInfo.getMetrics(context_);

        store_.addConnection(data);

        tick();
    }

    /**
     * 注意这个方法，是唯一能导致自定义的event（即不是connection）上传到服务器的
     *
     * @param events
     */
    public void recordEventsWithMetrics(String events) {
        String data;
        data = "app_key=" + appKey_;
        data += "&" + "device_id=" + DeviceInfo.generateOpenUDID(context_);
        data += "&" + "timestamp=" + (long) (System.currentTimeMillis() / 1000.0);
        data += "&" + "events=" + events;
        data += "&" + "metrics=" + DeviceInfo.getMetrics(context_);
        store_.addConnection(data);
        tick();
    }

    /**
     * 记录事件的心跳
     * 能够上传的永远只有connection 上边的函数recordEvents 也是把所有的event信息加入到connection里。
     */
    private void tick() {
        if (thread_ != null && thread_.isAlive())
            return;

        if (store_.isEmptyConnections())
            return;

        thread_ = new Thread() {
            @Override
            public void run() {
                //uploadByGetOneByOne();
                uploadByPostAll();
            }
        };

        thread_.start();
    }

    /**
     * 用post方法一个连接上传全部数据，如果发送不成功则考虑清除数据
     */
    private synchronized void uploadByPostAll() {
        String content = store_.connectionsString();
        if (content != null)
            Log.d("post", content);
        int success = 0;
//                UploadUtils.doUploadString(context_, content);
        if (success == 1) {
            store_.removeAllConnection();
        } else {
            //如果发送不成功而且留存的数量较大则清除掉前一半
            String[] sessions = store_.connections();
            if (sessions.length > Countly.MAX_CONNECTIONS_ALLOWED) {
                if (Countly.LOG) Log.d("Clean", "####################");
                for (int i = 0; i < Countly.HALF_CONNECTIONS_CLEANED; i++) {
                    store_.removeConnection(sessions[i]); //发送完成后删除
                }
            }
        }
    }

    /**
     * 用get方法每次上传一个，多次连接并全部上传完
     */
    private synchronized void uploadByGetOneByOne() {
//        while (true) {
//            String[] sessions = store_.connections();
//            if (sessions.length == 0)
//                break;
//            String initial = sessions[0], replaced = initial;
//            int index = replaced.indexOf("REPLACE_UDID");
//            if (index != -1) {
//                if (OpenUDID_manager.isInitialized() == false)
//                    break;
//                replaced = replaced.replaceFirst("REPLACE_UDID", OpenUDID_manager.getOpenUDID());
//            }
//            /**
//             * 发送事件
//             */
//            try {
//                //if (Countly.LOG) Log.d("Countly try upload ->", serverURL_ +"/i?"+ replaced);
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet method = new HttpGet(new URI(serverURL_ + "/i?" + replaced));
//                HttpResponse response = httpClient.execute(method);
//                InputStream input = response.getEntity().getContent();
//                while (input.read() != -1)
//                    ;
//                httpClient.getConnectionManager().shutdown();
//                store_.removeConnection(initial); //发送完成后删除
//            } catch (Exception e) {
//                Log.d("Countly", "error ->" + initial);
//                break;
//            }
//        }
    }
}
