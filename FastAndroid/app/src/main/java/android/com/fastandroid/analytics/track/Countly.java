package android.com.fastandroid.analytics.track;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.analytics.track
 * 当前类作用：周期性维护，事件队列，网络发送队列，本地信息存储 三个事务
 * 作者：longlyboyhe on 2015/11/16 11:25
 * 邮箱：longlyboyhe@126.com
 */
public class Countly {
    private static Countly sharedInstance_;
    private Timer timer_;
    private ConnectionQueue queue_;
    private EventQueue eventQueue_;
    private boolean isVisible_;
    private double unsentSessionLength_;
    private double lastTime_;
    private int activityCount_;
    private CountlyStore countlyStore_;

    private String UPLOAD_URL = ""; //上传路径

    protected static final String SDK_VERSION ="1.0.0.0";
    protected static final int SESSION_DURATION_WHEN_TIME_ADJUSTED = 15;
    protected static final int MAX_CONNECTIONS_ALLOWED = 100; //最多缓存记录的条数
    protected static final int HALF_CONNECTIONS_CLEANED = 35; //最多

    protected static final int TIMER_DURATION_DELAY = 20;
    protected static final int TIMER_DURATION_LONG = 80;
    protected static final int TIMER_DURATION_SHORT = 20;
    protected static final int TIMER_DURATION = TIMER_DURATION_LONG;

    public final static boolean LOG = false; //Display or not debug message

    /**
     Countly实例，在调用的时候只使用 sharedInstance 这个名字
     使用时只有一个实例存在
     */
    public static Countly sharedInstance() {
        if (sharedInstance_ == null)
            sharedInstance_ = new Countly();
        return sharedInstance_;
    }


    private Countly() {
        queue_ = new ConnectionQueue();
        timer_ = new Timer();
        timer_.schedule(new TimerTask() {
            @Override
            public void run() {
                onTimer();
            }
        }, TIMER_DURATION_DELAY * 1000, TIMER_DURATION * 1000);

        isVisible_ = false;
        unsentSessionLength_ = 0;
        activityCount_ = 0;
    }

    public void init(Context context, String serverURL, String appKey) {
//        OpenUDID_manager.sync(context);
        countlyStore_ = new CountlyStore(context);
        queue_.setContext(context);
        queue_.setServerURL(serverURL);
        queue_.setAppKey(appKey);
        queue_.setCountlyStore(countlyStore_);
        UPLOAD_URL = serverURL;
        eventQueue_ = new EventQueue(countlyStore_);
    }

    public void onStart() {
        activityCount_++;
        if (activityCount_ == 1)
            onStartHelper();
    }

    public void onStop() {
        activityCount_--;
        if (activityCount_ == 0)
            onStopHelper();
    }

    /**
     * 第一次启动跟踪时，队列和时间初始化
     */
    public void onStartHelper() {
        lastTime_ = System.currentTimeMillis() / 1000.0;
        queue_.beginSession();
        isVisible_ = true;
    }

    /**
     * 所有activity完成跟踪，则记录所有时间，计算完成时间
     */
    public void onStopHelper() {
        //将自定义事件中所有事件推入总的队列里
        if (eventQueue_.size() > 0)
            queue_.recordEvents(eventQueue_.events());
        double currTime = System.currentTimeMillis() / 1000.0;
        unsentSessionLength_ += currTime - lastTime_;
        int duration = (int) unsentSessionLength_;
        queue_.endSession(duration);
        unsentSessionLength_ -= duration;
        isVisible_ = false;
    }

    /**
     * 记录崩溃事件，传入错误信息string即可
     * @param errorMessage
     * 关键词：ErrorMessage　，　key:crash
     */
    public void recordCrashEvent(String errorMessage){
        HashMap<String,String> map = new HashMap<String,String>();
        errorMessage = errorMessage.replaceAll(";","_");
        map.put("ErrorMessage", errorMessage);
        recordEventWithMetrics("crash",map, 1);
    }


    /**
     * 记录事件，如果事件队列里有超过10个事件则发出去
     * @param key
     */

    public void recordEvent(String key) {
        eventQueue_.recordEvent(key);

        if (eventQueue_.size() >= 10)
            queue_.recordEvents(eventQueue_.events());
    }

    public void recordEvent(String key, int count) {
        eventQueue_.recordEvent(key, count);

        if (eventQueue_.size() >= 10)
            queue_.recordEvents(eventQueue_.events());
    }

    public void recordEvent(String key, int count, double sum) {
        eventQueue_.recordEvent(key, count, sum);

        if (eventQueue_.size() >= 10)
            queue_.recordEvents(eventQueue_.events());
    }

    public void recordEvent(String key, Map<String, String> segmentation, int count) {
        eventQueue_.recordEvent(key, segmentation, count);

        if (eventQueue_.size() >= 10)
            queue_.recordEvents(eventQueue_.events());
    }

    /**
     * 让需要发送的自定义时间内加入metrics信息
     * @param key
     * @param segmentation
     * @param count
     */
    public void recordEventWithMetrics(String key, Map<String, String> segmentation, int count) {
        eventQueue_.recordEvent(key, segmentation, count);

        if (eventQueue_.size() >= 10)
            queue_.recordEventsWithMetrics(eventQueue_.events());
    }


    public void recordEvent(String key, Map<String, String> segmentation, int count, double sum) {
        eventQueue_.recordEvent(key, segmentation, count, sum);

        if (eventQueue_.size() >= 10)
            queue_.recordEvents(eventQueue_.events());
    }

    /**
     * 定时计算时间更新session信息，并将发送到服务器
     */
    private void onTimer() {
        if (isVisible_ == false)
            return;

        double currTime = System.currentTimeMillis() / 1000.0;
        unsentSessionLength_ += currTime - lastTime_;
        lastTime_ = currTime;

        int duration = (int) unsentSessionLength_;
        queue_.updateSession(duration);
        unsentSessionLength_ -= duration;

        if (eventQueue_.size() > 0)
            queue_.recordEvents(eventQueue_.events());
    }
}
